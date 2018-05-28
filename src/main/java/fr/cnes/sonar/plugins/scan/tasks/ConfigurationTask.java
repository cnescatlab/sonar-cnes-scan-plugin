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

import java.io.IOException;

/**
 * Execute the scan of a project
 * @author lequal
 */
public class ConfigurationTask extends AbstractTask {

    private final Configuration config;

    /**
     * @param config The configuration
     */
    public ConfigurationTask(Configuration config){
        this.config = config;
    }

    /**
     * @return pylintrc folder set in preferences
     */
    private String getValue(final String key) {
        return this.config.get(StringManager.string(key)).orElse(StringManager.string(StringManager.DEFAULT_STRING));
    }



    /**
     * Send plugin's properties set for the current instance of sonar
     * @param request request coming from the user
     * @param response response to send to the user
     * @throws IOException when communicating with the client
     * @throws InterruptedException ...
     */
    @Override
    public void handle(final Request request, final Response response)
            throws IOException, InterruptedException {
        // write the json response
        try (JsonWriter jsonWriter = response.newJsonWriter()) {
            jsonWriter.beginObject();
            // add logs to response
            jsonWriter.prop(StringManager.string(StringManager.HOME_PROP_DEF_API_KEY), getValue(StringManager.HOME_PROP_DEF_KEY));
            jsonWriter.prop(StringManager.string(StringManager.REPORT_OUTPUT_PROP_DEF_API_KEY), getValue(StringManager.REPORT_OUTPUT_PROP_DEF_KEY));
            jsonWriter.prop(StringManager.string(StringManager.PYLINTRC_PROP_DEF_API_KEY), getValue(StringManager.PYLINTRC_PROP_DEF_KEY));
            jsonWriter.prop(StringManager.string(StringManager.TIMEOUT_PROP_DEF_API_KEY), getValue(StringManager.TIMEOUT_PROP_DEF_KEY));
            jsonWriter.endObject();
        }
    }



}