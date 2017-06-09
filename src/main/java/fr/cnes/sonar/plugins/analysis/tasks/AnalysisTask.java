package fr.cnes.sonar.plugins.analysis.tasks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static fr.cnes.sonar.plugins.analysis.utils.StringManager.*;

/**
 * Execute the analysis of a project
 * @author garconb
 */
public class AnalysisTask extends AbstractTask {

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

        // path where spp should be written
        String sppPath = String.format(string(CNES_SPP_PATH),
                string(CNES_WORKSPACE), projectFolder);
        // write sonar-project.properties in the project folder
        writeTextFile(sppPath, sonarProjectProperties);

        // build the analysis command
        String analysisCommand = String.format(string(CNES_COMMAND_SCAN),
                string(CNES_WORKSPACE), projectFolder, projectFolder);
        // analysis execution
        log(executeCommand(analysisCommand));

        // string formatted date as string
        String date = new SimpleDateFormat(string(DATE_PATTERN)).format(new Date());

        // export log file
        String logPath = String.format(string(CNES_LOG_PATH),
                string(CNES_WORKSPACE), date, projectName);
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
