package fr.cnes.sonar.plugins.scan.tasks;

import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.Response;
import org.sonar.api.utils.text.JsonWriter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static fr.cnes.sonar.plugins.scan.utils.StringManager.*;

/**
 * Execute element to produce the report
 * @author lequal
 */
public class ReportTask extends AbstractTask {

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
    public String report(String projectId, String reportAuthor, String reportPath,
                         String reportTemplate, String issuesTemplate)
            throws IOException, InterruptedException {

        // creation of the output directory
        boolean success = (new File(reportPath)).mkdirs();
        if (!success) {
            // Directory creation failed
            log(String.format(string(CNES_MKDIR_ERROR), reportPath));
        }
        // formatted date
        String date = new SimpleDateFormat(string(DATE_PATTERN)).format(new Date());
        // construct the command string to run scan
        String command = String.format(string(CNES_COMMAND_REPORT),
                string(CNES_REPORT_PATH), string(SONAR_URL), projectId,
                reportAuthor, date, reportPath, reportTemplate, issuesTemplate);
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
    public void handle(Request request, Response response) throws IOException, InterruptedException {
        // reset logs to not stack them
        setLogs("");

        // Key of the project provided by the user through parameters
        final String projectKey = request.mandatoryParam(string(CNES_ACTION_REPORT_PARAM_KEY_NAME));
        // Code to be used in the created files. The only character that is not supported
        // in filesystems but which is in project key is ":", so we replace all occurences by a "#".
        final String projectCode = projectKey.replaceAll(NOT_SUPPORTED_CHARS, HASHTAG);
        // Report's author
        final String author = request.mandatoryParam(string(CNES_ACTION_REPORT_PARAM_AUTHOR_NAME));
        // Date of today
        final String today = new SimpleDateFormat(string(DATE_PATTERN)).format(new Date());
        // Construct the name of the output folder like that: sharedFolder/project-date-results
        final String output = String.format(string(CNES_REPORTS_FOLDER),string(CNES_REPORTER_OUTPUT), today, projectCode);

        // read request parameters and generates response output
        // generate the reports and save output
        String result = report(
                projectKey,
                author,
                output,
                string(CNES_REPORTER_TEMPLATE),
                string(CNES_ISSUES_TEMPLATE)
        );

        // set the response
        JsonWriter jsonWriter = response.newJsonWriter();
        jsonWriter.beginObject();
        // add logs to response
        jsonWriter.prop(string(REPORT_RESPONSE_LOG), result);
        jsonWriter.endObject();
        jsonWriter.close();
    }
}
