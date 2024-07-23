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
import org.sonar.api.server.ws.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * Execute the scan of a project
 * @author lequal
 */
public abstract class AbstractTask implements RequestHandler {

    /**
     * logger for all tasks
     */
    protected static final Logger LOGGER = Logger.getLogger(AbstractTask.class.getName());

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
    protected String executeCommand(final String command) throws IOException, InterruptedException {

        // prepare a string builder for the output gathering
        final StringBuilder output = new StringBuilder();

        // create a new process
        final Process p;
        // execute the process on the runtime
        p = Runtime.getRuntime().exec(command);
        // wait for the end of the process
        p.waitFor();


        try (
                // collect input
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(p.getInputStream()));
                // collect errors
                BufferedReader reader2 = new BufferedReader(
                        new InputStreamReader(p.getErrorStream()))) {

            // append input stream to output
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append(StringManager.NEW_LINE);
            }
            // append error stream to output
            while ((line = reader2.readLine()) != null) {
                output.append(line).append(StringManager.NEW_LINE);
            }
        }

        // log output
        final String result = output.toString();
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
     * @param pLogs string to put in a new StringBuilder
     */
    protected void setLogs(final String pLogs) {
        this.logs = new StringBuilder().append(pLogs);
    }

    /**
     * Add logs
     * @param pLogs string to add
     */
    protected void log(final String pLogs) {
        this.logs.append(pLogs);
    }
}
