package org.analogweb.thymeleaf;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertThat;

import org.analogweb.ModulesBuilder;
import org.analogweb.core.direction.Html;
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

        when(builder.addDirectionFormatterClass(Html.class, ThymeleafHtmlFormatter.class))
                .thenReturn(builder);
        ModulesBuilder actual = config.prepare(builder);

        assertThat(actual, is(builder));

        verify(builder).addDirectionFormatterClass(Html.class, ThymeleafHtmlFormatter.class);
    }

}
