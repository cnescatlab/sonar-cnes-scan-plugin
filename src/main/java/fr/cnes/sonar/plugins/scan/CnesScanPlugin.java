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

import static fr.cnes.sonar.plugins.scan.utils.StringManager.string;


/**
 * This class is the entry point for all extensions. It is referenced in pom.xml.
 * @author lequal
 *
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


        //Create Sonarqube's home property definition
        context.addExtension(createSimplePropertyDefinition(string(StringManager.HOME_PROP_DEF_KEY), string(StringManager.HOME_PROP_DEF_NAME), string(StringManager.HOME_PROP_DEF_DESC), string(StringManager.HOME_PROP_DEF_DEFAULT)));
        //Create Sonarqube's scanner property definition
        context.addExtension(createSimplePropertyDefinition(string(StringManager.SCANNER_PROP_DEF_KEY), string(StringManager.SCANNER_PROP_DEF_NAME), string(StringManager.SCANNER_PROP_DEF_DESC), string(StringManager.SCANNER_PROP_DEF_DEFAULT)));
        //Create workspace's absolute path property definition
        context.addExtension(createSimplePropertyDefinition(string(StringManager.WORKSPACE_PROP_DEF_KEY), string(StringManager.WORKSPACE_PROP_DEF_NAME), string(StringManager.WORKSPACE_PROP_DEF_DESC), string(StringManager.WORKSPACE_PROP_DEF_DEFAULT)));
        //Create report template property definition
        context.addExtension(createSimplePropertyDefinition(string(StringManager.REPORT_TEMPLATE_PROP_DEF_KEY), string(StringManager.REPORT_TEMPLATE_PROP_DEF_NAME), string(StringManager.REPORT_TEMPLATE_PROP_DEF_DESC), string(StringManager.REPORT_TEMPLATE_PROP_DEF_DEFAULT)));
        //Create issues template file property definition
        context.addExtension(createSimplePropertyDefinition(string(StringManager.ISSUES_TEMPLATE_PROP_DEF_KEY), string(StringManager.ISSUES_TEMPLATE_PROP_DEF_NAME), string(StringManager.ISSUES_TEMPLATE_PROP_DEF_DESC), string(StringManager.ISSUES_TEMPLATE_PROP_DEF_DEFAULT)));
        //Create report's extension executable file path
        context.addExtension(createSimplePropertyDefinition(string(StringManager.REPORT_PATH_PROP_DEF_KEY), string(StringManager.REPORT_PATH_PROP_DEF_NAME), string(StringManager.REPORT_PATH_PROP_DEF_DESC), string(StringManager.REPORT_PATH_PROP_DEF_DEFAULT)));
        //Create report output destination property definition
        context.addExtension(createSimplePropertyDefinition(string(StringManager.REPORT_OUTPUT_PROP_DEF_KEY), string(StringManager.REPORT_OUTPUT_PROP_DEF_NAME), string(StringManager.REPORT_OUTPUT_PROP_DEF_DESC), string(StringManager.REPORT_OUTPUT_PROP_DEF_DEFAULT)));
        //Create pylintrc's folder property definition
        context.addExtension(createSimplePropertyDefinition(string(StringManager.PYLINTRC_PROP_DEF_KEY), string(StringManager.PYLINTRC_PROP_DEF_NAME), string(StringManager.PYLINTRC_PROP_DEF_DESC), string(StringManager.PYLINTRC_PROP_DEF_DEFAULT)));
        //Create scanner's timeout property definition
        context.addExtension(createSimplePropertyDefinition(string(StringManager.TIMEOUT_PROP_DEF_KEY), string(StringManager.TIMEOUT_PROP_DEF_NAME), string(StringManager.TIMEOUT_PROP_DEF_DESC), string(StringManager.TIMEOUT_PROP_DEF_DEFAULT)));





    }

    /**
     * Create simple property definition for the administration page (only with the following parameters) :
     * @param key of the property
     * @param name of the property
     * @param description of the property to set
     * @param defaultValue Default value of the property
     * @return a built property definition to add on an extension
     */
    private PropertyDefinition createSimplePropertyDefinition(final String key, final String name, final String description, final String defaultValue){
        PropertyDefinition.Builder builder = PropertyDefinition.builder(key);
        builder.name(name);
        builder.description(description);
        builder.defaultValue(defaultValue);
        return builder.build();
    }
}
