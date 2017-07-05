package fr.cnes.sonar.plugins.scan.tasks;

import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static fr.cnes.sonar.plugins.scan.utils.StringManager.*;

/**
 * Execute the scan of a project
 * @author begarco
 */
public class AnalysisTask extends AbstractTask {

    /**
     * Execute the scan of a project
     * @param projectName name of the project to analyze
     * @param projectFolder url of the folder containing the project to analyze
     * @param sonarProjectProperties the sonar-project.properties as string
     * @return logs
     * @throws IOException when a file writing goes wrong
     * @throws InterruptedException when a command is not finished
     */
    private String analyze(String projectName, String projectFolder, String sonarProjectProperties)
            throws IOException, InterruptedException {

        try {
            // path where spp should be written
            String sppPath = String.format(string(CNES_SPP_PATH),
                    string(CNES_WORKSPACE), projectFolder);
            // write sonar-project.properties in the project folder
            writeTextFile(sppPath, sonarProjectProperties);
            // build the scan command
            String analysisCommand = String.format(string(CNES_COMMAND_SCAN),
                    string(CNES_WORKSPACE), projectFolder, projectFolder);
            // scan execution
            log(executeCommand(analysisCommand));

            // string formatted date as string
            String date = new SimpleDateFormat(string(DATE_PATTERN)).format(new Date());

            // export log file
            String logPath = String.format(string(CNES_LOG_PATH),
                    string(CNES_WORKSPACE), date, projectName);
            writeTextFile(logPath, getLogs());
        } catch (FileNotFoundException e) {
            // the spp file or the log file could not be written
            // so we log the problem and return logs
            log(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }

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

    /**
     * Use the user's request to launch an scan
     * @param request request coming from the user
     * @param response response to send to the user
     * @throws IOException when communicating with the client
     * @throws InterruptedException ...
     */
    @Override
    public void handle(Request request, Response response)
            throws IOException, InterruptedException {
        // reset logs to not stack them
        setLogs("");

        // get project's information from the request's parameters
        final String projectName = request.mandatoryParam(string(CNES_ACTION_1_PARAM_2_NAME));
        final String workspace = request.mandatoryParam(string(CNES_ACTION_1_PARAM_5_NAME));
        final String sonarProjectProperties = request.mandatoryParam(string(CNES_ACTION_1_PARAM_6_NAME));

        // concrete scan
        String result = analyze(projectName, workspace, sonarProjectProperties);

        // write the json response
        response.newJsonWriter()
                .beginObject()
                // add logs to response
                .prop(string(CNES_ACTION_1_FIELD_1), result)
                .endObject()
                .close();
    }

}
