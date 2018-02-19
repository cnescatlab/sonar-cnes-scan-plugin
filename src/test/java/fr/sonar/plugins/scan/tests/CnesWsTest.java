package fr.sonar.plugins.scan.tests;

import fr.cnes.sonar.plugins.scan.ws.CnesWs;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.server.ws.WebService;
import org.sonar.api.server.ws.WsTester;

import static org.junit.Assert.*;

/**
 * Test for the CnesWs class
 * @author lequal
 */
public class CnesWsTest {

	/**
	 * Just a controller to test web service with stubbed data
	 */
    private WebService.Controller controller;

    /**
     * Executed each time before running a single test
     */
    @Before
    public void prepare() {
        final WebService ws = new CnesWs();

        // WsTester is available in the Maven artifact
        // org.codehaus.sonar:sonar-plugin-api
        // with type "test-jar"
        final WsTester tester = new WsTester(ws);
        controller = tester.controller("api/cnes");
    }

    /**
     * Check that the controller has correct parameters
     */
    @Test
    public void controllerTest() {
        assertNotNull(controller);
        assertEquals("api/cnes", controller.path());
        assertFalse(controller.description().isEmpty());
        assertEquals(3, controller.actions().size());
    }

    /**
     * Check analyze web service
     * Assert that the key, name and parameters' number is correct
     */
    @Test
    public void analyzeWebServiceTest() {
        final WebService.Action getTree = controller.action("analyze");
        assertNotNull(getTree);
        assertEquals("analyze", getTree.key());
        assertEquals(4, getTree.params().size());
    }

    /**
     * Check report web service
     * Assert that the key, name and parameters' number is correct
     */
    @Test
    public void reportWebServiceTest() {
        final WebService.Action getTree = controller.action("report");
        assertNotNull(getTree);
        assertEquals("report", getTree.key());
        assertEquals(2, getTree.params().size());
    }

    /**
     * Check create_project web service
     * Assert that the key, name and parameters' number is correct
     */
    @Test
    public void createProjectWebServiceTest() {
        final WebService.Action getTree = controller.action("create_project");
        assertNotNull(getTree);
        assertEquals("create_project", getTree.key());
        assertEquals(4, getTree.params().size());
    }
}
