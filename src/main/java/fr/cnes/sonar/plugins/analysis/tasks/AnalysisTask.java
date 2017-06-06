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
     * Path's pattern for the sonar-project.properties file
     */
    private static final String SPP_PATH_PATTERN = "/media/sf_Shared/%s/sonar-project.properties";
    /**
     * Command pattern to run an analysis
     */
    private static final String SCAN_COMMAND_PATTERN = "/opt/sonar-scanner/bin/sonar-scanner -D project.settings=/media/sf_Shared/%s/sonar-project.properties -D sonar.projectBaseDir=/media/sf_Shared/%s";

    /**
     * Execute the analysis of a project
     * @param projectFolder url of the folder containing the project to analyze
     * @param sonarProjectProperties the sonar-project.properties as string
     * @return logs
     * @throws IOException when a file writing goes wrong
     * @throws InterruptedException when a command is not finished
     */
    public String analyze(String projectFolder, String sonarProjectProperties)
            throws IOException, InterruptedException {

        // write of sonar-project.properties
        writeSonarProjectProperties(projectFolder, sonarProjectProperties);

        // analysis execution
        log(executeCommand(String.format(SCAN_COMMAND_PATTERN, projectFolder, projectFolder)));

        // return the complete logs
        return getLogs();
    }

    /**
     * Export the sonar-project.properties in the corresponding folder
     * @param folder Output folder
     * @param data Data to write
     * @throws IOException when a file writing goes wrong
     */
    private void writeSonarProjectProperties(String folder, String data) throws IOException {
        // construct the path of the spp
        String filePath = String.format(SPP_PATH_PATTERN, folder);
        // create a new file
        File spp = new File(filePath);

        // create the writer
        // true to append; false to overwrite.
        try(FileWriter fileWriter = new FileWriter(spp, false)) {
            // write the data
            fileWriter.write(data);
        }
    }

}
