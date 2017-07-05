package fr.sonar.plugins.scan.tests;

import fr.cnes.sonar.plugins.scan.ws.CnesWs;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.server.ws.WebService;
import org.sonar.api.server.ws.WsTester;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Test for the CnesWs class
 * @author begarco
 */
public class CnesWsTest {

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
        assertThat(controller).isNotNull();
        assertThat(controller.path()).isEqualTo("api/cnes");
        assertThat(controller.description()).isNotEmpty();
        assertThat(controller.actions().size()).isEqualTo(3);
    }

    /**
     * Check analyze web service
     * Assert that the key, name and parameters' number is correct
     */
    @Test
    public void analyzeWebServiceTest() {
        final WebService.Action getTree = controller.action("analyze");
        assertThat(getTree).isNotNull();
        assertThat(getTree.key()).isEqualTo("analyze");
        assertThat(getTree.params().size()).isEqualTo(6);
    }

    /**
     * Check report web service
     * Assert that the key, name and parameters' number is correct
     */
    @Test
    public void reportWebServiceTest() {
        final WebService.Action getTree = controller.action("report");
        assertThat(getTree).isNotNull();
        assertThat(getTree.key()).isEqualTo("report");
        assertThat(getTree.params().size()).isEqualTo(4);
    }

    /**
     * Check create_project web service
     * Assert that the key, name and parameters' number is correct
     */
    @Test
    public void createProjectWebServiceTest() {
        final WebService.Action getTree = controller.action("create_project");
        assertThat(getTree).isNotNull();
        assertThat(getTree.key()).isEqualTo("create_project");
        assertThat(getTree.params().size()).isEqualTo(4);
    }
}
