package fr.sonar.plugins.scan.tests;

import fr.cnes.sonar.plugins.scan.web.CnesPluginPageDefinition;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.web.page.Context;
import org.sonar.api.web.page.Page;

import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * Test for the CnesPluginPageDefinition class
 * @author lequal
 */
public class CnesPluginPageDefinitionTest {

	/**
	 * Entity to test
	 */
    private CnesPluginPageDefinition definition;
    /**
     * Stubbed context
     */
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
    public void definitionPageTest() {
        definition.define(context);

        assertEquals(1, context.getPages().size());

        final Iterator<Page> pagesIterator = context.getPages().iterator();
        final Page page = pagesIterator.next();
        assertNotNull(page);
        assertFalse(page.isAdmin());

    }

    /**
     * Assert that multiple definition of the same pages do not impact
     * the plugin workflow.
     */
    public void multiplePagesDefinitionsTest() {

        // Calling definition multiple time should not lead to failure: thanks C# plugin !
        definition.define(context);

        // Calling definition multiple time should not lead to failure: thanks C# plugin !
        definition.define(context);

        assert(!definition.equals(new CnesPluginPageDefinition()));
    }

}
