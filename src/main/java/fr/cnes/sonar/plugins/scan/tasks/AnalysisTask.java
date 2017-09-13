/*
 * This file is part of cnesscan.
 *
 * cnesscan is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * cnesscan is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with cnesscan.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.cnes.sonar.plugins.scan.tasks;

import fr.cnes.sonar.plugins.scan.utils.StringManager;
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

/**
 * Execute the scan of a project
 * @author lequal
 */
public class AnalysisTask extends AbstractTask {

    /**
     * Logged message when a file can not be deleted
     */
    private static final String FILE_DELETION_ERROR =
            "The following file could not be deleted: %s.";
    /**
     * Logged message when a file can not be set as executable
     */
    private static final String FILE_PERMISSIONS_ERROR =
            "Permissions of the following file could not be changed: %s.";
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
    private String analyze(final String projectName, final String projectFolder, final String sonarProjectProperties)
            throws IOException, InterruptedException {

        // path where spp should be written
        final String sppPath = String.format(StringManager.string(StringManager.CNES_SPP_PATH),
                StringManager.string(StringManager.CNES_WORKSPACE), projectFolder);
        // write sonar-project.properties in the project folder
        writeTextFile(sppPath, sonarProjectProperties);
        // build the scan command
        final String analysisCommand = String.format(
                StringManager.string(StringManager.CNES_COMMAND_SCAN),
                StringManager.string(StringManager.CNES_WORKSPACE),
                projectFolder,
                projectFolder);
        // create the temporary script to run cxx tools
        final File script = createScript(projectFolder, analysisCommand);

        // string formatted date as string
        final String date = new SimpleDateFormat(
                StringManager.string(StringManager.DATE_PATTERN)).format(new Date());

        // export log file
        final String logPath = String.format(StringManager.string(StringManager.CNES_LOG_PATH),
                StringManager.string(StringManager.CNES_WORKSPACE), date, projectName);

        try {
            // scan execution
            final String scriptCommand = StringManager.string(StringManager.CNES_WORKSPACE)+
                    SLASH+projectFolder+SLASH+CAT_SCAN_SCRIPT;
            log(executeCommand(scriptCommand));
            // log output file
            final String path = StringManager.string(StringManager.CNES_WORKSPACE)+
                    SLASH + projectFolder + SLASH + CAT_LOG_FILE;
            for(final String line : Files.readAllLines(Paths.get(path))) {
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
    private void writeTextFile(final String path, final String data) throws IOException {
        // create a new file
        final File spp = new File(path);

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
    public void handle(final Request request, final Response response)
            throws IOException, InterruptedException {
        // reset logs to not stack them
        setLogs("");

        // get project's information from the request's parameters
        final String projectName = request.mandatoryParam(
                StringManager.string(StringManager.ANALYZE_NAME_NAME));
        final String workspace = request.mandatoryParam(
                StringManager.string(StringManager.ANALYZE_FOLDER_NAME));
        final String sonarProjectProperties = request.mandatoryParam(
                StringManager.string(StringManager.ANALYZE_SPP_NAME));

        // concrete scan
        final String result = analyze(projectName, workspace, sonarProjectProperties);

        // write the json response
        final JsonWriter jsonWriter = response.newJsonWriter();
        jsonWriter.beginObject();
        // add logs to response
        jsonWriter.prop(StringManager.string(StringManager.ANALYZE_RESPONSE_LOG), result);
        jsonWriter.endObject();
        jsonWriter.close();
    }

    /**
     * Create a temporary script containing dedicated command executing sonar-scanner
     * @param project repository containing the source code
     * @param commandLine command line to execute
     * @return The created file
     */
    private File createScript(final String project, final String commandLine) {
        // path to the workspace
        final String workspace = StringManager.string(StringManager.CNES_WORKSPACE)+
                SLASH +project+ SLASH;
        // create script in a file located in the project's repository
        final File scriptOutput = new File(workspace + CAT_SCAN_SCRIPT);

        // Write all command lines in a single temporary script
        try (
                FileWriter script = new FileWriter(scriptOutput)
        ){
            script.write("#!/bin/bash -e");
            script.write("\ncd "+workspace);
            script.write(StringManager.string(StringManager.CNES_LOG_SEPARATOR)+commandLine);
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