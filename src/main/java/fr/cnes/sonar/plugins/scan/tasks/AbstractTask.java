package fr.cnes.sonar.plugins.scan.tasks;


import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Execute the scan of a project
 * @author lequal
 */
public abstract class AbstractTask implements RequestHandler {

    /**
     * logger for all tasks
     */
    protected static final Logger LOGGER = Loggers.get(AbstractTask.class);

    /**
     * contain all the task's logs
     */
    private StringBuilder logs = new StringBuilder();

    /**
     * Execute an environment command
     * @param command command to execute on the system
     * @return logs
     * @throws IOException when a stream use goes wrong
     * @throws InterruptedException when a command is not finished
     */
    protected String executeCommand(String command) throws IOException, InterruptedException {
        // log the command to execute
        LOGGER.info(command);

        // prepare a string builder for the output gathering
        StringBuilder output = new StringBuilder();

        // create a new process
        Process p;
        // execute the process on the runtime
        p = Runtime.getRuntime().exec(command);
        // wait for the end of the process
        p.waitFor();


        try (
                // collect input
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                // collect errors
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {

            // append input stream to output
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            // append error stream to output
            while ((line = reader2.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        // log output
        String result = output.toString();
        LOGGER.info(result);

        // return the output logs
        return result;
    }

    /**
     * Getter of logs
     * @return the string contained in the StringBuilder
     */
    protected String getLogs() {
        return logs.toString();
    }

    /**
     * Setter of logs
     * @param logs string to put in a new StringBuilder
     */
    protected void setLogs(String logs) {
        this.logs = new StringBuilder().append(logs);
    }

    /**
     * Add logs
     * @param logs string to add
     */
    protected void log(String logs) {
        this.logs.append(logs);
    }
}
