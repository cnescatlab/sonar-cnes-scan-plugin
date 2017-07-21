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

        // create a page
        Page.Builder page = Page.builder(string(CNES_PAGE_ANALYSIS_KEY));
        // set its name
        page.setName(string(CNES_PAGE_ANALYSIS_NAME));
        // set its scope (where it is displayed)
        page.setScope(Scope.GLOBAL);
        // add a new page for scan + reporting
        context.addPage(page.build());


        // create a page
        page = Page.builder(string(CNES_PAGE_REPORT_KEY));
        // set its name
        page.setName(string(CNES_PAGE_REPORT_NAME));
        // set its scope (where it is displayed)
        page.setScope(Scope.GLOBAL);
        // add a new page for reporting
        context.addPage(page.build());


        // create a page
        page = Page.builder(string(CNES_PAGE_HELP_KEY));
        // set its name
        page.setName(string(CNES_PAGE_HELP_NAME));
        // set its scope (where it is displayed)
        page.setScope(Scope.GLOBAL);
        // add a new page for help
        context.addPage(page.build());

    }
}
