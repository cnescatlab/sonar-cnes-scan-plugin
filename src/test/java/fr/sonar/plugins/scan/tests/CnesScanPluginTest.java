package fr.sonar.plugins.scan.tests;

import fr.cnes.sonar.plugins.scan.CnesScanPlugin;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.Plugin;
import org.sonar.api.SonarEdition;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.SonarRuntime;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.utils.Version;

import static org.junit.Assert.assertEquals;

/**
 * Test for the CnesScanPlugin class
 * @author lequal
 */
public class CnesScanPluginTest {

    /**
     * Instance of the plugin to test
     */
    private CnesScanPlugin cnesScanPlugin;

    /**
     * Prepare each test by creating a new CnesScanPlugin
     */
    @Before
    public void prepare() {
        cnesScanPlugin = new CnesScanPlugin();
    }

    /**
     * Assert that the plugin subscribe correctly to SonarQube
     * by checking the good number of extensions.
     */
    @Test
    public void sonarqubePluginDefinitionTest() {
        final SonarRuntime runtime = SonarRuntimeImpl.forSonarQube(Version.create(7,9), SonarQubeSide.SERVER, SonarEdition.COMMUNITY);
        final Plugin.Context context = new Plugin.Context(runtime);
        cnesScanPlugin.define(context);
        assertEquals(8, context.getExtensions().size());
    }


}
