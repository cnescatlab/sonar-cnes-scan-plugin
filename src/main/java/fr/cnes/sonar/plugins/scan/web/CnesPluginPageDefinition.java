package fr.cnes.sonar.plugins.scan.web;

import org.sonar.api.web.page.Context;
import org.sonar.api.web.page.Page;
import org.sonar.api.web.page.Page.Scope;
import org.sonar.api.web.page.PageDefinition;

import static fr.cnes.sonar.plugins.scan.utils.StringManager.*;

/**
 * Define web pages of the plugin
 * @author begarco
 */
public class CnesPluginPageDefinition implements PageDefinition {

    /**
     * Define concretely the pages to add through the plugin
     * @param context Execution context of the plugin
     */
    @Override
    public void define(Context context) {
        context
            // add a new page for scan + reporting
            .addPage(Page.builder(string(CNES_PAGE_ANALYSIS_KEY))
                    // set its name
                .setName(string(CNES_PAGE_ANALYSIS_NAME))
                    // set its scope (where it is displayed)
                .setScope(Scope.GLOBAL).build())
            // add a new page for reporting
            .addPage(Page.builder(string(CNES_PAGE_REPORT_KEY))
                    // set its name
                    .setName(string(CNES_PAGE_REPORT_NAME))
                    // set its scope (where it is displayed)
                    .setScope(Scope.GLOBAL).build())
            // add a new page for help
            .addPage(Page.builder(string(CNES_PAGE_HELP_KEY))
                    // set its name
                    .setName(string(CNES_PAGE_HELP_NAME))
                    // set its scope (where it is displayed)
                    .setScope(Scope.GLOBAL).build());
    }
}
