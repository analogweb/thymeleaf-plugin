package org.analogweb.thymeleaf;

import org.analogweb.ModulesBuilder;
import org.analogweb.PluginModulesConfig;
import org.analogweb.core.response.Html;
import org.analogweb.util.MessageResource;
import org.analogweb.util.PropertyResourceBundleMessageResource;
import org.analogweb.util.logging.Log;
import org.analogweb.util.logging.Logs;

/**
 * @author y2k2mt
 */
public class ThymeleafPluginModulesConfig implements PluginModulesConfig {

    public static final MessageResource PLUGIN_MESSAGE_RESOURCE = new PropertyResourceBundleMessageResource(
            "org.analogweb.thymeleaf.analog-messages");
    private static final Log log = Logs.getLog(ThymeleafPluginModulesConfig.class);

    @Override
    public ModulesBuilder prepare(ModulesBuilder builder) {
        log.log(PLUGIN_MESSAGE_RESOURCE, "ITYB000001");
        builder.addResponseFormatterClass(Html.class, ThymeleafHtmlFormatter.class);
        return builder;
    }
}
