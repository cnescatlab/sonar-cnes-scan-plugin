package fr.cnes.sonar.plugins.analysis.tasks;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static fr.cnes.sonar.plugins.analysis.utils.StringManager.*;

/**
 * Execute element to produce the report
 * @author garconb
 */
public class ReportTask extends AbstractTask {

    /**
     * Product the report
     * @param projectId Key of the project to report
     * @param projectQualityGate Quality Gate of the project to report
     * @param projectName  Name of the project to report
     * @param reportAuthor Author of the report
     * @param reportPath Output folder
     * @param reportTemplate template to use for the processing
     * @return logs of the task
     * @throws IOException when a file writing goes wrong
     * @throws InterruptedException when a command is not finished
     */
    public String report(String projectId, String projectQualityGate,
                         String projectName, String reportAuthor, String reportPath,
                         String reportTemplate) throws IOException, InterruptedException {
        // construct the command string to run analysis
        String command = String.format(string(CNES_COMMAND_REPORT),
                string(CNES_REPORT_PATH), string(SONAR_URL));
        //
        command = String.format(command,
                projectId, projectQualityGate, projectName, reportAuthor);
        // formatted date
        String date = new SimpleDateFormat(string(DATE_PATTERN)).format(new Date());
        // format command to contain date report path and template
        command = String.format(command, date, reportPath, reportTemplate);
        // log the command used
        log(command);
        // log the execution result
        log(executeCommand(command));

        // return the log
        return getLogs();
    }

}
