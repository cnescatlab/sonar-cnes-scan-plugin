package fr.cnes.sonar.plugins.analysis;

import fr.cnes.sonar.plugins.analysis.rules.python.CnesPythonRulesDefinition;
import fr.cnes.sonar.plugins.analysis.web.CnesPluginPageDefinition;
import fr.cnes.sonar.plugins.analysis.ws.CnesWs;
import org.sonar.api.Plugin;

/**
 * This class is the entry point for all extensions. It is referenced in pom.xml.
 * @author garconb
 */
public class CnesPlugin implements Plugin {

    /**
     * Definition of the plugin
     * @param context Execution context of the plugin
     */
    @Override
    public void define(Context context) {
        // analysis web service extension
        context.addExtension(CnesWs.class);

        // analysis web extensions
        context.addExtension(CnesPluginPageDefinition.class);

        // add new python rules (checker-cnes and other pylint rules)
        context.addExtension(CnesPythonRulesDefinition.class);
    }
}
