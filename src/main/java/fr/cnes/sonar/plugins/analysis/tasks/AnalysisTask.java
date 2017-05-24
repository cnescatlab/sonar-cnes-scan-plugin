package fr.cnes.sonar.plugins.analysis.tasks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Execute the analysis of a project
 * @author garconb
 */
public class AnalysisTask extends AbstractTask {

    /**
     * Execute the analysis of a projec
     * @return logs
     * @throws IOException when a file writing goes wrong
     * @throws InterruptedException when a command is not finished
     */
    public String analyze(String projectKey, String projectName,
                          String qualityProfile, String qualityGate,
                          String projectFolder, String sonarProjectProperties) throws IOException, InterruptedException {

        // write of sonar-project.properties
        writeSonarProjectProperties(projectFolder, sonarProjectProperties);

        // analysis execution
        log(executeCommand("/opt/sonar-scanner/bin/sonar-scanner -D project.settings=/media/sf_Shared/" + projectFolder
                + "/sonar-project.properties -D sonar.projectBaseDir=/media/sf_Shared/" + projectFolder));

        return getLogs();
    }

    /**
     * Export the sonar-project.properties in the corresponding folder
     * @param folder Output folder
     * @param data Data to write
     * @throws IOException when a file writing goes wrong
     */
    private void writeSonarProjectProperties(String folder, String data) throws IOException {
        String filePath = "/media/sf_Shared" + "/" + folder + "/" + "sonar-project.properties";
        File spp = new File(filePath);
        FileWriter fileWriter = new FileWriter(spp, false); // true to append; false to overwrite.
        fileWriter.write(data);
        fileWriter.close();
    }

}
