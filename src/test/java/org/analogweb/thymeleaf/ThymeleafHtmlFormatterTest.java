package org.analogweb.thymeleaf;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.analogweb.RequestContext;
import org.analogweb.core.direction.Html.HtmlTemplate;
import org.analogweb.exception.AssertionFailureException;
import org.analogweb.util.Maps;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

public class ThymeleafHtmlFormatterTest {

    private ThymeleafHtmlFormatter writer;

    private RequestContext requestContext;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletContext servletContext;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        writer = new ThymeleafHtmlFormatter();
        requestContext = mock(RequestContext.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        servletContext = mock(ServletContext.class);

        when(requestContext.getRequest()).thenReturn(request);
        when(requestContext.getResponse()).thenReturn(response);
        when(requestContext.getContext()).thenReturn(servletContext);

        TemplateEngine engine = new TemplateEngine();
        TemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("org/analogweb/thymeleaf/");
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");
        engine.setTemplateResolver(resolver);
        writer.setTemplateEngine(engine);
    }

    @Test
    public void testformatAndWriteInto() throws Exception {
        StringWriter responseBody = new StringWriter();
        PrintWriter pw = new PrintWriter(responseBody);

        when(response.getWriter()).thenReturn(pw);

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

        writer.formatAndWriteInto(requestContext, "UTF-8", source);
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
        assertThat(expected, is(responseBody.toString()));
    }

    @Test
    public void testformatAndWriteIntoWithEmptyTemplateEngine() throws Exception {
        thrown.expect(AssertionFailureException.class);

        writer.setTemplateEngine(null);

        StringWriter responseBody = new StringWriter();
        PrintWriter pw = new PrintWriter(responseBody);

        when(response.getWriter()).thenReturn(pw);

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

        writer.formatAndWriteInto(requestContext, "UTF-8", source);
    }

    @Test
    public void testformatAndWriteIntoWithoutHtmlTemplateSource() throws Exception {

        Object source = null;
        // nothing to do.
        writer.formatAndWriteInto(requestContext, "UTF-8", source);
    }

}
