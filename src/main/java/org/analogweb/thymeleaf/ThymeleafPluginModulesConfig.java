package org.analogweb.thymeleaf;

import org.analogweb.ModulesBuilder;
import org.analogweb.PluginModulesConfig;
import org.analogweb.core.response.Html;
import org.analogweb.util.MessageResource;
import org.analogweb.util.PropertyResourceBundleMessageResource;
import org.analogweb.util.logging.Log;
import org.analogweb.util.logging.Logs;

/**
 * <a href="http://www.thymeleaf.org/">Thymeleaf</a>を
 * <a href="https://github.com/analogweb">Analog Web Framework</a>
 * に統合する{@link PluginModulesConfig}です。<br/>
 * このプラグインを使用することで、{@link Html}にThymeleafの書式で作成
 * したテンプレートを指定し、レスポンスすることが可能になります。
 * @author snowgoose
 */
public class ThymeleafPluginModulesConfig implements PluginModulesConfig {

    /**
     * Thymeleafプラグインで使用する{@link MessageResource}です。
     */
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
