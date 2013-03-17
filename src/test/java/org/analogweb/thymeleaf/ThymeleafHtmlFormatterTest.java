package org.analogweb.thymeleaf;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.analogweb.ResponseContext;
import org.analogweb.ResponseContext.ResponseWriter;
import org.analogweb.core.DefaultResponseWriter;
import org.analogweb.core.response.Html.HtmlTemplate;
import org.analogweb.servlet.ServletRequestContext;
import org.analogweb.util.Maps;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

public class ThymeleafHtmlFormatterTest {

    private ThymeleafHtmlFormatter formatter;

    private ServletRequestContext requestContext;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletContext servletContext;
    private ResponseContext responseContext;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        formatter = new ThymeleafHtmlFormatter();
        requestContext = mock(ServletRequestContext.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        servletContext = mock(ServletContext.class);
        responseContext = mock(ResponseContext.class);

        when(requestContext.getServletRequest()).thenReturn(request);
        when(requestContext.getServletResponse()).thenReturn(response);
        when(requestContext.getServletContext()).thenReturn(servletContext);

        TemplateEngine engine = new TemplateEngine();
        TemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("org/analogweb/thymeleaf/");
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");
        engine.setTemplateResolver(resolver);
        formatter.setTemplateEngine(engine);
    }

    @Test
    public void testformatAndWriteInto() throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        ResponseWriter writer = new DefaultResponseWriter();
        when(responseContext.getResponseWriter()).thenReturn(writer);

        // Thymeleaf relatives.
        when(request.getLocale()).thenReturn(Locale.getDefault());
        when(request.getAttributeNames()).thenReturn(
                Collections.enumeration(Collections.emptyList()));
        when(request.getParameterNames()).thenReturn(
                Collections.enumeration(Collections.emptyList()));
        when(request.getContextPath()).thenReturn("/boo");
        when(servletContext.getAttributeNames()).thenReturn(
                Collections.enumeration(Collections.emptyList()));

        Map<String, Object> context = Maps.newHashMap("message", (Object) "これはテストです.");
        HtmlTemplate source = new HtmlTemplate("ThymeleafHtmlFormatterTest-1", context);

        formatter.formatAndWriteInto(requestContext, responseContext, "UTF-8", source);
        writer.getEntity().writeInto(out);
        String expected = "<!DOCTYPE html>" + "\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">" + "\n" + "<head>" + "\n"
                + "    <meta charset=\"utf-8\" />" + "\n"
                + "    <link href=\"/boo/css/base.css\" rel=\"stylesheet\" />" + "\n"
                + "    <script type=\"text/javascript\" src=\"/boo/js/lib/jquery.js\"></script>"
                + "\n" + "    <title>Thymeleaf</title>" + "\n" + "</head>" + "\n" + "<body>" + "\n"
                + "<div class=\"container\">" + "\n" + "    <div class=\"content\">" + "\n"
                + "        <div class=\"page-header\">" + "\n" + "            <h1>これはテストです.</h1>"
                + "\n" + "        </div>" + "\n" + "    </div>" + "\n" + "</div>" + "\n"
                + "</body>" + "\n" + "</html>";
        assertThat(new String(out.toByteArray()), is(expected));
    }

    @Test
    public void testformatAndWriteIntoWithEmptyTemplateEngine() throws Exception {
        thrown.expect(TemplateInputException.class);

        // initialize TemplateEngine
        formatter.setTemplateEngine(null);

        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        ResponseWriter writer = new DefaultResponseWriter();
        when(responseContext.getResponseWriter()).thenReturn(writer);

        // Thymeleaf relatives.
        when(request.getLocale()).thenReturn(Locale.getDefault());
        when(request.getAttributeNames()).thenReturn(
                Collections.enumeration(Collections.emptyList()));
        when(request.getParameterNames()).thenReturn(
                Collections.enumeration(Collections.emptyList()));
        when(request.getContextPath()).thenReturn("/boo");
        when(servletContext.getAttributeNames()).thenReturn(
                Collections.enumeration(Collections.emptyList()));

        Map<String, Object> context = Maps.newEmptyHashMap();
        HtmlTemplate source = new HtmlTemplate("ThymeleafHtmlFormatterTest-1", context);

        formatter.formatAndWriteInto(requestContext, responseContext, "UTF-8", source);
        writer.getEntity().writeInto(out);

    }

    @Test
    public void testformatAndWriteIntoWithoutHtmlTemplateSource() throws Exception {

        Object source = null;
        // nothing to do.
        formatter.formatAndWriteInto(requestContext, responseContext, "UTF-8", source);
    }

}
