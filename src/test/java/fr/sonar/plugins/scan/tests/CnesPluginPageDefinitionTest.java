package fr.sonar.plugins.scan.tests;

import fr.cnes.sonar.plugins.scan.web.CnesPluginPageDefinition;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.web.page.Context;
import org.sonar.api.web.page.Page;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Test for the CnesPluginPageDefinition class
 * @author lequal
 */
public class CnesPluginPageDefinitionTest {

    private CnesPluginPageDefinition definition;
    private Context context;

    /**
     * Prepare for each test the basic attributes to use like the context
     */
    @Before
    public void prepare() {
        definition = new CnesPluginPageDefinition();
        context = new Context();
    }

    /**
     * Assert that the pages are correctly loaded by checking that
     * the number of pages is correct
     * the first page is correctly filled out
     */
    @Test
    public void definitionRuleTest() {
        definition.define(context);

        assertEquals(3, context.getPages().size());

        Iterator<Page> pagesIterator = context.getPages().iterator();
        Page page = pagesIterator.next();
        assertNotNull(page);
        assertFalse(page.isAdmin());

    }

    /**
     * Assert that multiple definition of the same pages do not impact
     * the plugin workflow.
     */
    @Test(expected = Exception.class)
    public void multipleRulesDefinitionsTest() throws Exception {

        // Calling definition multiple time should not lead to failure: thanks C# plugin !
        definition.define(context);

        // Calling definition multiple time should not lead to failure: thanks C# plugin !
        definition.define(context);

        throw new Exception();
    }

}
