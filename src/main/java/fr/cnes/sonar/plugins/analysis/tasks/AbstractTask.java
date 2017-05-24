package fr.cnes.sonar.plugins.analysis.tasks;


import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Execute the analysis of a project
 * @author garconb
 */
public class AbstractTask {

    // logger for all tasks
    private static final Logger LOGGER = Loggers.get(AbstractTask.class);

    // contain all the task's logs
    private StringBuilder logs = new StringBuilder();

    /**
     * Execute an environment command
     * @param command command to execute on the system
     * @return logs
     * @throws IOException when a stream use goes wrong
     * @throws InterruptedException when a command is not finished
     */
    String executeCommand(String command) throws IOException, InterruptedException {

        LOGGER.info(command);

        StringBuilder output = new StringBuilder();

        Process p;
        p = Runtime.getRuntime().exec(command);
        p.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(p.getErrorStream()));

        String line;
        while ((line = reader.readLine())!= null) {
            output.append(line).append("\n");
        }
        while ((line = reader2.readLine())!= null) {
            output.append(line).append("\n");
        }

        LOGGER.info(output.toString());

        return output.toString();
    }

    String getLogs() {
        return logs.toString();
    }

    public void setLogs(String logs) {
        this.logs = new StringBuilder().append(logs);
    }

    /**
     * Add logs
     * @param logs string to add
     */
    void log(String logs) {
        this.logs.append(logs);
    }
}
