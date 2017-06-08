package fr.cnes.sonar.plugins.analysis.web;

import org.sonar.api.web.page.Context;
import org.sonar.api.web.page.Page;
import org.sonar.api.web.page.Page.Scope;
import org.sonar.api.web.page.PageDefinition;

/**
 * Define web pages of the plugin
 * @author garconb
 */
public class CnesPluginPageDefinition implements PageDefinition {

    /**
     * Define concretely the pages to add through the plugin
     * @param context Execution context of the plugin
     */
    @Override
    public void define(Context context) {
        context
            // add a new page for analysis + reporting
            .addPage(Page.builder("cnes/analysis")
                    // set its name
                .setName("CNES Analysis")
                    // set its scope (where it is displayed)
                .setScope(Scope.GLOBAL).build())
            // add a new page for reporting
            .addPage(Page.builder("cnes/reporting")
                    // set its name
                    .setName("CNES Reporting")
                    // set its scope (where it is displayed)
                    .setScope(Scope.GLOBAL).build())
            // add a new page for help
            .addPage(Page.builder("cnes/help")
                    // set its name
                    .setName("CNES Help")
                    // set its scope (where it is displayed)
                    .setScope(Scope.GLOBAL).build());
    }
}
