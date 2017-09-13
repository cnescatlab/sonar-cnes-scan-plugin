/*
 * This file is part of cnesscan.
 *
 * cnesscan is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * cnesscan is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with cnesscan.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.cnes.sonar.plugins.scan;

import fr.cnes.sonar.plugins.scan.utils.StringManager;
import fr.cnes.sonar.plugins.scan.web.CnesPluginPageDefinition;
import fr.cnes.sonar.plugins.scan.ws.CnesWs;
import org.sonar.api.Plugin;
import org.sonar.api.config.PropertyDefinition;

/**
 * This class is the entry point for all extensions. It is referenced in pom.xml.
 * @author lequal
 */
public class CnesScanPlugin implements Plugin {

    /**
     * Definition of the plugin:
     * add pages, web services, rules, sensor, etc.
     *
     * @param context Execution context of the plugin
     */
    @Override
    public void define(final Context context) {
        // scan web service extension
        context.addExtension(CnesWs.class);

        // scan web extensions
        context.addExtension(CnesPluginPageDefinition.class);

        // define a property to retrieve the timeout wanted by the user
        // this property can be define through sonar-project.properties
        // define the builder
        final PropertyDefinition.Builder builder = PropertyDefinition.builder(
                StringManager.string(StringManager.TIMEOUT_PROP_DEF_KEY));
        // set attributes
        builder.name(StringManager.string(StringManager.TIMEOUT_PROP_DEF_NAME));
        builder.description(StringManager.string(StringManager.TIMEOUT_PROP_DEF_DESC));
        builder.defaultValue(StringManager.string(StringManager.TIMEOUT_PROP_DEF_DEFAULT));
        // build the property
        final PropertyDefinition propertyDefinition = builder.build();
        // add it to SonarQube context
        context.addExtension(propertyDefinition);
    }
}
