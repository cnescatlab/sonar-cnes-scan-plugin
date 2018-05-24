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
import org.sonar.api.config.Configuration;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.Response;
import org.sonar.api.utils.text.JsonWriter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Execute element to produce the report
 * @author lequal
 */
public class ReportTask extends AbstractTask {


    private final Configuration config;

    public ReportTask(Configuration config){
        this.config = config;
    }
    /**
     * Replacement character for not supported chars
     */
    private static final String HASHTAG = "#";
    /**
     * Not supported characters' regex
     */
    private static final String NOT_SUPPORTED_CHARS = ":";

    /**
     * Product the report
     * @param projectId Key of the project to report
     * @param reportAuthor Author of the report
     * @param reportPath Output folder
     * @param reportTemplate template to use for the processing
     * @param issuesTemplate template for the xlsx file
     * @return logs of the task
     * @throws IOException when a file writing goes wrong
     * @throws InterruptedException when a command is not finished
     */
    public String report(final String projectId, final String reportAuthor, final String reportPath,
                         final String reportTemplate, final String issuesTemplate)
            throws IOException, InterruptedException {

        // creation of the output directory
        final boolean success = (new File(reportPath)).mkdirs();
        if (!success) {
            // Directory creation failed
            log(String.format(StringManager.string(StringManager.CNES_MKDIR_ERROR), reportPath));
        }
        // formatted date
        final String date = new SimpleDateFormat(StringManager.string(StringManager.DATE_PATTERN))
                .format(new Date());
        // construct the command string to run scan
        final String command = String.format(
                StringManager.string(StringManager.CNES_COMMAND_REPORT),
                config.get(StringManager.string(StringManager.REPORT_PATH_PROP_DEF_KEY)).orElse(StringManager.string(StringManager.DEFAULT_STRING)),
                StringManager.string(StringManager.SONAR_URL),
                projectId, reportAuthor, date, reportPath, reportTemplate, issuesTemplate);
        // log the command used
        log(command);
        // log the execution result
        log(executeCommand(command));

        // return the log
        return getLogs();
    }

    /**
     * Use the user's request to start the report generation
     * @param request request coming from the user
     * @param response response to send to the user
     * @throws IOException when an error occurred on file writing
     * @throws InterruptedException ...
     */
    @Override
    public void handle(final Request request, final Response response)
            throws IOException, InterruptedException {
        // reset logs to not stack them
        setLogs("");

        // Key of the project provided by the user through parameters
        final String projectKey = request.mandatoryParam(
                StringManager.string(StringManager.CNES_ACTION_REPORT_PARAM_KEY_NAME));
        // Code to be used in the created files. The only character that is not supported
        // in filesystems but which is in project key is ":", so we replace all occurences by a "#".
        final String projectCode = projectKey.replaceAll(NOT_SUPPORTED_CHARS, HASHTAG);
        // Report's author
        final String author = request.mandatoryParam(
                StringManager.string(StringManager.CNES_ACTION_REPORT_PARAM_AUTHOR_NAME));
        // Date of today
        final String today = new SimpleDateFormat(
                StringManager.string(StringManager.DATE_PATTERN)).format(new Date());
        // Construct the name of the output folder like that: sharedFolder/project-date-results
        final String output = String.format(StringManager.string(StringManager.CNES_REPORTS_FOLDER),
                config.get(StringManager.string(StringManager.REPORT_OUTPUT_PROP_DEF_KEY)).orElse(StringManager.string(StringManager.DEFAULT_STRING)), today, projectCode);

        // read request parameters and generates response output
        // generate the reports and save output
        final String result = report(
                projectKey,
                author,
                output,
                StringManager.string(config.get(StringManager.string(StringManager.REPORT_TEMPLATE_PROP_DEF_KEY)).orElse(StringManager.DEFAULT_STRING)),
                StringManager.string(config.get(StringManager.string(StringManager.ISSUES_TEMPLATE_PROP_DEF_KEY)).orElse(StringManager.DEFAULT_STRING))
        );

        // set the response
        try (JsonWriter jsonWriter = response.newJsonWriter()) {
            jsonWriter.beginObject();
            // add logs to response
            jsonWriter.prop(StringManager.string(StringManager.REPORT_RESPONSE_LOG), result);
            jsonWriter.endObject();
        }
    }
}
