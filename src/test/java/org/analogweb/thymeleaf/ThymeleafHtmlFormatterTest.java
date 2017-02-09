package org.analogweb.thymeleaf;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import org.analogweb.RequestContext;
import org.analogweb.ResponseContext;
import org.analogweb.core.DefaultWritableBuffer;
import org.analogweb.core.response.Html.HtmlTemplate;
import org.analogweb.util.Maps;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class ThymeleafHtmlFormatterTest {

    private ThymeleafHtmlFormatter formatter;
    private RequestContext requestContext;
    private ResponseContext responseContext;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        formatter = new ThymeleafHtmlFormatter();
        requestContext = mock(RequestContext.class);
        responseContext = mock(ResponseContext.class);
        final TemplateEngine engine = new TemplateEngine();
        final ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("org/analogweb/thymeleaf/");
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");
        engine.setTemplateResolver(resolver);
        formatter.setTemplateEngine(engine);
    }

    @Test
    public void testformatAndWriteInto() throws Exception {
        final ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        final Map<String, Object> context = Maps.newHashMap("message", (Object) "This is TEST.");
        context.put("contextPath", "/boo");
        final HtmlTemplate source = new HtmlTemplate("ThymeleafHtmlFormatterTest-1", context);
        formatter.formatAndWriteInto(requestContext, responseContext, "UTF-8", source).writeInto(
                DefaultWritableBuffer.writeBuffer(out));
        final String expected = "<!DOCTYPE html>" + "\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">" + "\n" + "<head>" + "\n"
                + "    <meta charset=\"utf-8\"/>" + "\n"
                + "    <link href=\"/boo/css/base.css\" rel=\"stylesheet\"/>" + "\n"
                + "    <script type=\"text/javascript\" src=\"/boo/js/lib/jquery.js\"></script>"
                + "\n" + "    <title>Thymeleaf</title>" + "\n" + "</head>" + "\n" + "<body>" + "\n"
                + "<div class=\"container\">" + "\n" + "    <div class=\"content\">" + "\n"
                + "        <div class=\"page-header\">" + "\n"
                + "            <h1>This is TEST.</h1>" + "\n" + "        </div>" + "\n"
                + "    </div>" + "\n" + "</div>" + "\n" + "</body>" + "\n" + "</html>";
        assertThat(new String(out.toByteArray()), is(expected));
    }

    @Test
    public void testformatAndWriteIntoWithEmptyTemplateEngine() throws Exception {
        thrown.expect(TemplateInputException.class);
        // initialize TemplateEngine
        formatter.setTemplateEngine(null);
        final ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        final Map<String, Object> context = Maps.newEmptyHashMap();
        final HtmlTemplate source = new HtmlTemplate("ThymeleafHtmlFormatterTest-1", context);
        formatter.formatAndWriteInto(requestContext, responseContext, "UTF-8", source).writeInto(
                DefaultWritableBuffer.writeBuffer(out));
    }

    @Test
    public void testformatAndWriteIntoWithoutHtmlTemplateSource() throws Exception {
        final Object source = null;
        // nothing to do.
        formatter.formatAndWriteInto(requestContext, responseContext, "UTF-8", source);
    }
}
