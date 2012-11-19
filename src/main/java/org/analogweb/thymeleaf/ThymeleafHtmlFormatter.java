package org.analogweb.thymeleaf;

import static org.analogweb.thymeleaf.ThymeleafPluginModulesConfig.PLUGIN_MESSAGE_RESOURCE;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import org.analogweb.DirectionFormatter;
import org.analogweb.RequestContext;
import org.analogweb.ServletRequestContext;
import org.analogweb.core.direction.Html;
import org.analogweb.core.direction.Html.HtmlTemplate;
import org.analogweb.exception.FormatFailureException;
import org.analogweb.util.logging.Log;
import org.analogweb.util.logging.Logs;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

/**
 * <a href="http://www.thymeleaf.org/">Thymeleaf</a>テンプレートを
 * フォーマットする{@link DirectionFormatter}の実装です。<br/>
 * HTMLテンプレートを{@link TemplateEngine}及び{@link IContext}を
 * 使用して生成し、レスポンスに書き込みます。
 * @author snowgoose
 */
public class ThymeleafHtmlFormatter implements DirectionFormatter {

    private static final Log log = Logs.getLog(ThymeleafHtmlFormatter.class);
    private TemplateEngine engine;

    protected TemplateEngine initDefaultTemplateEngine() {
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(createDefaultTemplateResolver());
        return engine;
    }

    protected TemplateResolver createDefaultTemplateResolver() {
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
        resolver.setCacheTTLMs(360000L);
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

    @Override
    public void formatAndWriteInto(RequestContext writeTo, String charset, Object source)
            throws FormatFailureException {
        if (source instanceof HtmlTemplate) {
            HtmlTemplate templateSource = (HtmlTemplate) source;
            IContext context = createIContext(writeTo, templateSource);
            log.log(PLUGIN_MESSAGE_RESOURCE, "DTYB000001", context);
            try {
                TemplateEngine engine = getTemplateEngine();
                log.log(PLUGIN_MESSAGE_RESOURCE, "DTYB000002", engine);
                engine.process(templateSource.getTemplateResource(), context,
                        resolveWriter(writeTo));
            } catch (IOException e) {
                throw new FormatFailureException(e, source, Html.class.getName());
            }
        } else {
            log.log(PLUGIN_MESSAGE_RESOURCE, "WTYB000001", HtmlTemplate.class.getCanonicalName());
            log.log(PLUGIN_MESSAGE_RESOURCE, "WTYB000002");
        }
    }

    protected Writer resolveWriter(RequestContext context) throws IOException {
        if (context instanceof ServletRequestContext) {
            return ((ServletRequestContext) context).getServletResponse().getWriter();
        }
        return new OutputStreamWriter(context.getResponseBody());
    }

    /**
     * このインスタンスで共有される{@link TemplateEngine}を取得します。
     * @return {@link TemplateEngine}
     */
    protected TemplateEngine getTemplateEngine() {
        if (this.engine == null) {
            this.engine = initDefaultTemplateEngine();
        }
        return this.engine;
    }

    /**
     * {@link IContext}を生成します。<br/>
     * {@link RequestContext}に則った{@link WebContext}が生成されます。
     * @param request {@link RequestContext}
     * @param templateSource {@link HtmlTemplate}
     * @return {@link IContext}
     */
    protected IContext createIContext(RequestContext request, HtmlTemplate templateSource) {
        if(request instanceof ServletRequestContext){
            ServletRequestContext s = (ServletRequestContext)request;
            HttpServletRequest servletRequest = s.getServletRequest();
            WebContext context = new WebContext(servletRequest, s.getServletContext(),
                    servletRequest.getLocale(), templateSource.getContext());
            return context;
        } else {
            // TODO 
            throw new UnsupportedOperationException();
        }
    }

    /**
     * 任意の{@link TemplateEngine}を設定します。
     * @param engine {@link TemplateEngine}
     */
    public void setTemplateEngine(TemplateEngine engine) {
        this.engine = engine;
    }

}
