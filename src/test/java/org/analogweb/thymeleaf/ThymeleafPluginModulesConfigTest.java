package org.analogweb.thymeleaf;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertThat;

import org.analogweb.ModulesBuilder;
import org.analogweb.core.response.Html;
import org.junit.Before;
import org.junit.Test;

public class ThymeleafPluginModulesConfigTest {

    private ThymeleafPluginModulesConfig config;
    private ModulesBuilder builder;

    @Before
    public void setUp() throws Exception {
        config = new ThymeleafPluginModulesConfig();
        builder = mock(ModulesBuilder.class);
    }

    @Test
    public void testPrepare() {

        when(builder.addResponseFormatterClass(Html.class, ThymeleafHtmlFormatter.class))
                .thenReturn(builder);
        ModulesBuilder actual = config.prepare(builder);

        assertThat(actual, is(builder));

        verify(builder).addResponseFormatterClass(Html.class, ThymeleafHtmlFormatter.class);
    }

}
