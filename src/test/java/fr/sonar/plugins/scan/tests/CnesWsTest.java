package fr.sonar.plugins.scan.tests;

import fr.cnes.sonar.plugins.scan.utils.StringManager;
import fr.cnes.sonar.plugins.scan.ws.CnesWs;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Configuration;
import org.sonar.api.config.internal.MapSettings;
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

    private CnesWs cws;

    private MapSettings settings = new MapSettings();
    /**
     * Executed each time before running a single test
     */
    @Before
    public void prepare() {
        /*
         * Enter parameters to verify
         */
        settings.setProperty(StringManager.string(StringManager.HOME_PROP_DEF_KEY),StringManager.string(StringManager.HOME_PROP_DEF_DEFAULT));

        cws = new CnesWs(settings.asConfig());
        final WebService ws = cws;
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
        assertEquals(4, controller.actions().size());
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

    /**
     * Check create_project web service
     * Assert that the key, name and parameters' number is correct
     */
    @Test
    public void configurationWebServiceTest() {
        final WebService.Action getTree = controller.action("configuration");
        assertNotNull(getTree);
        assertEquals("configuration", getTree.key());
        assertEquals(0, getTree.params().size());
    }

    /**
     * Check configuration injection on Constructor
     * Assert that configuration injected can be retrieved.
     */
    @Test
    public void verifyConfiguration() {
        //assertTrue(cws.getConfiguration().getStringArray(StringManager.string(StringManager.HOME_PROP_DEF_KEY)).length==1);
        Configuration config = cws.getConfiguration();
        assertTrue(config.hasKey(StringManager.string(StringManager.HOME_PROP_DEF_KEY)));
        assertEquals(StringManager.string(StringManager.HOME_PROP_DEF_DEFAULT), config.get(StringManager.string(StringManager.HOME_PROP_DEF_KEY)).get());


    }
}
