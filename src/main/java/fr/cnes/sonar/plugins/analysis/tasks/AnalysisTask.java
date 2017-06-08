package fr.cnes.sonar.plugins.analysis.tasks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Execute the analysis of a project
 * @author garconb
 */
public class AnalysisTask extends AbstractTask {

    /**
     * Path to the shared folder between host and guest
     */
    private static final String SHARED_FOLDER = "/media/sf_Shared";
    /**
     * Path pattern for the sonar-project.properties file
     * arg1: shared folder
     * arg2: project folder
     */
    private static final String SPP_PATH_PATTERN = "%s/%s/sonar-project.properties";
    /**
     * Path pattern for the log file
     * arg1: shared folder
     * arg2: date
     * arg3: project name
     */
    private static final String LOG_PATH_PATTERN = "%s/%s-%s.log";
    /**
     * Command pattern to run an analysis
     */
    private static final String SCAN_COMMAND_PATTERN = "/opt/sonar-scanner/bin/sonar-scanner -D project.settings=" + SHARED_FOLDER + "/%s/sonar-project.properties -D sonar.projectBaseDir=/media/sf_Shared/%s";

    /**
     * Execute the analysis of a project
     * @param projectName name of the project to analyze
     * @param projectFolder url of the folder containing the project to analyze
     * @param sonarProjectProperties the sonar-project.properties as string
     * @return logs
     * @throws IOException when a file writing goes wrong
     * @throws InterruptedException when a command is not finished
     */
    public String analyze(String projectName, String projectFolder, String sonarProjectProperties)
            throws IOException, InterruptedException {

        // write sonar-project.properties in the project folder
        writeTextFile(String.format(SPP_PATH_PATTERN, SHARED_FOLDER, projectFolder), sonarProjectProperties);

        // analysis execution
        log(executeCommand(String.format(SCAN_COMMAND_PATTERN, projectFolder, projectFolder)));

        // export log file
        String logPath = String.format(LOG_PATH_PATTERN, SHARED_FOLDER,
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()), projectName);
        writeTextFile(logPath, getLogs());

        // return the complete logs
        return getLogs();
    }

    /**
     * Export the sonar-project.properties in the corresponding folder
     * @param path Output folder
     * @param data Data to write
     * @throws IOException when a file writing goes wrong
     */
    private void writeTextFile(String path, String data) throws IOException {
        // create a new file
        File spp = new File(path);

        // create the writer
        // true to append; false to overwrite.
        try(FileWriter fileWriter = new FileWriter(spp, false)) {
            // write the data
            fileWriter.write(data);
        }
    }

}
