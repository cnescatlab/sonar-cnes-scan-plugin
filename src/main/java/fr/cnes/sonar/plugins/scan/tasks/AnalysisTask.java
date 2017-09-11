package fr.cnes.sonar.plugins.scan.tasks;

import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.Response;
import org.sonar.api.utils.text.JsonWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import static fr.cnes.sonar.plugins.scan.utils.StringManager.*;

/**
 * Execute the scan of a project
 * @author lequal
 */
public class AnalysisTask extends AbstractTask {

    /**
     * Logged message when a file can not be deleted
     */
    private static final String FILE_DELETION_ERROR = "The following file could not be deleted: %s.";
    /**
     * Logged message when a file can not be set as executable
     */
    private static final String FILE_PERMISSIONS_ERROR = "Permissions of the following file could not be changed: %s.";
    /**
     * Just a slash
     */
    private static final String SLASH = "/";
    /**
     * Name of the log file
     */
    private static final String CAT_LOG_FILE = ".cat-sonar-scanner.log";
    /**
     * Script containing command to launch analysis
     */
    private static final String CAT_SCAN_SCRIPT = "cat-scan-script.sh";
    /**
     * Just a new line character
     */
    private static final String NEW_LINE = "\n";

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

        // path where spp should be written
        String sppPath = String.format(string(CNES_SPP_PATH),
                string(CNES_WORKSPACE), projectFolder);
        // write sonar-project.properties in the project folder
        writeTextFile(sppPath, sonarProjectProperties);
        // build the scan command
        String analysisCommand = String.format(string(CNES_COMMAND_SCAN),
                string(CNES_WORKSPACE), projectFolder, projectFolder);
        // create the temporary script to run cxx tools
        File script = createScript(projectFolder, analysisCommand);

        // string formatted date as string
        String date = new SimpleDateFormat(string(DATE_PATTERN)).format(new Date());

        // export log file
        String logPath = String.format(string(CNES_LOG_PATH),
                string(CNES_WORKSPACE), date, projectName);

        try {
            // scan execution
            log(executeCommand(string(CNES_WORKSPACE)+SLASH+projectFolder+SLASH+CAT_SCAN_SCRIPT));
            // log output file
            String path = string(CNES_WORKSPACE)+ SLASH + projectFolder + SLASH + CAT_LOG_FILE;
            for(String line : Files.readAllLines(Paths.get(path))) {
                log(line+ NEW_LINE);
            }
            // export logs for user
            writeTextFile(logPath, sonarProjectProperties+NEW_LINE+getLogs());
        } catch (IOException | InterruptedException e) {
            // the spp file or the log file could not be written
            // so we log the problem and return logs
            log(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }

        // delete temporary script
        if(!script.delete()) {
            LOGGER.error(String.format(FILE_DELETION_ERROR, script.getName()));
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
        final String projectName = request.mandatoryParam(string(ANALYZE_NAME_NAME));
        final String workspace = request.mandatoryParam(string(ANALYZE_FOLDER_NAME));
        final String sonarProjectProperties = request.mandatoryParam(string(ANALYZE_SPP_NAME));

        // concrete scan
        String result = analyze(projectName, workspace, sonarProjectProperties);

        // write the json response
        JsonWriter jsonWriter = response.newJsonWriter();
        jsonWriter.beginObject();
        // add logs to response
        jsonWriter.prop(string(ANALYZE_RESPONSE_LOG), result);
        jsonWriter.endObject();
        jsonWriter.close();
    }

    /**
     * Create a temporary script containing dedicated command executing sonar-scanner
     * @param project repository containing the source code
     * @param commandLine command line to execute
     * @return The created file
     */
    private File createScript(String project, String commandLine) {

        // create script in a file located in the project's repository
        final File scriptOutput = new File(string(CNES_WORKSPACE)+ SLASH +project+ SLASH + CAT_SCAN_SCRIPT);

        // Write all command lines in a single temporary script
        try (
                FileWriter script = new FileWriter(scriptOutput)
        ){
            script.write("#!/bin/bash -e");
            script.write("\ncd "+string(CNES_WORKSPACE)+SLASH+project);
            script.write(string(CNES_LOG_SEPARATOR)+commandLine);
            LOGGER.info(commandLine);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        // give execution rights on the script
        if(!scriptOutput.setExecutable(true)) {
            LOGGER.error(String.format(FILE_PERMISSIONS_ERROR, scriptOutput.getName()));
        }

        return scriptOutput;
    }

}