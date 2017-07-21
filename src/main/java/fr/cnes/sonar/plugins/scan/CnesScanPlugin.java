package fr.cnes.sonar.plugins.scan;

import fr.cnes.sonar.plugins.scan.utils.StringManager;
import fr.cnes.sonar.plugins.scan.web.CnesPluginPageDefinition;
import fr.cnes.sonar.plugins.scan.ws.CnesWs;
import org.sonar.api.Plugin;
import org.sonar.api.config.PropertyDefinition;

import static fr.cnes.sonar.plugins.scan.utils.StringManager.string;

/**
 * This class is the entry point for all extensions. It is referenced in pom.xml.
 * @author begarco
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

        // define a property to retrieve the timeout wanted by the user
        // this property can be define through sonar-project.properties
        // define the builder
        PropertyDefinition.Builder builder = PropertyDefinition.builder(string(StringManager.TIMEOUT_PROP_DEF_KEY));
        // set attributes
        builder.name(string(StringManager.TIMEOUT_PROP_DEF_NAME));
        builder.description(string(StringManager.TIMEOUT_PROP_DEF_DESC));
        builder.defaultValue(string(StringManager.TIMEOUT_PROP_DEF_DEFAULT));
        // build the property
        PropertyDefinition propertyDefinition = builder.build();
        // add it to SonarQube context
        context.addExtension(propertyDefinition);
    }
}
