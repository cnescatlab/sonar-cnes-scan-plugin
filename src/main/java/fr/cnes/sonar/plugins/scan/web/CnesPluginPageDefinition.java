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
package fr.cnes.sonar.plugins.scan.web;

import fr.cnes.sonar.plugins.scan.utils.StringManager;
import org.sonar.api.web.page.Context;
import org.sonar.api.web.page.Page;
import org.sonar.api.web.page.Page.Scope;
import org.sonar.api.web.page.PageDefinition;

import java.util.logging.Logger;

/**
 * Define web pages of the plugin
 * @author lequal
 */
public class CnesPluginPageDefinition implements PageDefinition {

    protected static final Logger LOGGER = Logger.getLogger(CnesPluginPageDefinition.class.getName());
    /**
     * Define concretely the pages to add through the plugin
     * @param context Execution context of the plugin
     */
    @Override
    public void define(final Context context) {

        LOGGER.warning("Defining CNES plugin pages");

        // create a page
        Page.Builder page = Page.builder(
                StringManager.string(StringManager.CNES_PAGE_ANALYSIS_KEY));
        // set its name
        page.setName(StringManager.string(StringManager.CNES_PAGE_ANALYSIS_NAME));
        // set its scope (where it is displayed)
        page.setScope(Scope.GLOBAL);
        // add a new page for scan + reporting
        context.addPage(page.build());

    }
}
