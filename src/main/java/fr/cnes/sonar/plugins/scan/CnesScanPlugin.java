package fr.cnes.sonar.plugins.scan;

import fr.cnes.sonar.plugins.scan.web.CnesPluginPageDefinition;
import fr.cnes.sonar.plugins.scan.ws.CnesWs;
import org.sonar.api.Plugin;

/**
 * This class is the entry point for all extensions. It is referenced in pom.xml.
 * @author garconb
 */
public class CnesScanPlugin implements Plugin {

    /**
     * Definition of the plugin:
     * add pages, web services, rules, sensor, etc.
     *
     * @param context Execution context of the plugin
     */
    @Override
    public void define(Context context) {
        // scan web service extension
        context.addExtension(CnesWs.class);

        // scan web extensions
        context.addExtension(CnesPluginPageDefinition.class);
    }
}
