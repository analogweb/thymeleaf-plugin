package org.analogweb.thymeleaf;

import static org.analogweb.thymeleaf.ThymeleafPluginModulesConfig.PLUGIN_MESSAGE_RESOURCE;

import java.io.IOException;
import java.io.OutputStreamWriter;

import org.analogweb.*;
import org.analogweb.core.FormatFailureException;
import org.analogweb.core.response.Html.HtmlTemplate;
import org.analogweb.util.logging.Log;
import org.analogweb.util.logging.Logs;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

/**
 * @author y2k2mt
 */
public class ThymeleafHtmlFormatter implements ResponseFormatter {

    private static final Log log = Logs.getLog(ThymeleafHtmlFormatter.class);
    private TemplateEngine engine;

    protected TemplateEngine initDefaultTemplateEngine() {
        final TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(createDefaultTemplateResolver());
        return engine;
    }

    protected ITemplateResolver createDefaultTemplateResolver() {
        final ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setCacheTTLMs(360000L);
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

    @Override
    public ResponseEntity formatAndWriteInto(final RequestContext context, ResponseContext writeTo,
            String charset, Object source) throws FormatFailureException {
        if (source instanceof HtmlTemplate) {
            final HtmlTemplate templateSource = (HtmlTemplate) source;
            final IContext iContext = createIContext(context, templateSource);
            log.log(PLUGIN_MESSAGE_RESOURCE, "DTYB000001", context);
            final TemplateEngine engine = getTemplateEngine();
            log.log(PLUGIN_MESSAGE_RESOURCE, "DTYB000002", engine);
            return new ResponseEntity() {

                @Override
                public void writeInto(WritableBuffer responseBody) throws IOException {
                    final OutputStreamWriter writer = new OutputStreamWriter(responseBody.asOutputStream());
                    engine.process(templateSource.getTemplateResource(), iContext, writer);
                    writer.flush();
                }

                @Override
                public long getContentLength() {
                    return -1;
                }
            };
        } else {
            log.log(PLUGIN_MESSAGE_RESOURCE, "WTYB000001", HtmlTemplate.class.getCanonicalName());
            log.log(PLUGIN_MESSAGE_RESOURCE, "WTYB000002");
            return null;
        }
    }

    protected TemplateEngine getTemplateEngine() {
        if (this.engine == null) {
            this.engine = initDefaultTemplateEngine();
        }
        return this.engine;
    }

    protected IContext createIContext(RequestContext request, HtmlTemplate templateSource) {
        final Context context = new Context();
        context.setVariables(templateSource.getContext());
        return context;
    }

    public void setTemplateEngine(TemplateEngine engine) {
        this.engine = engine;
    }
}
