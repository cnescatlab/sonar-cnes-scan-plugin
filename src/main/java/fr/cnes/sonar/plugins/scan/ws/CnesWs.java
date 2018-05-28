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
package fr.cnes.sonar.plugins.scan.ws;

import fr.cnes.sonar.plugins.scan.tasks.AbstractTask;
import fr.cnes.sonar.plugins.scan.tasks.AnalysisTask;
import fr.cnes.sonar.plugins.scan.tasks.ConfigurationTask;
import fr.cnes.sonar.plugins.scan.tasks.ReportTask;
import fr.cnes.sonar.plugins.scan.tasks.project.ProjectTask;
import fr.cnes.sonar.plugins.scan.utils.StringManager;
import org.sonar.api.config.Configuration;
import org.sonar.api.server.ws.WebService;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * Expose CNES plugin api
 * @author lequal
 */
public class CnesWs implements WebService {

    protected static final Logger LOGGER = Loggers.get(AbstractTask.class);

    private final Configuration configuration;

    public CnesWs(final Configuration config){
        this.configuration = config;
        LOGGER.debug(config.toString());
    }

    public Configuration getConfiguration(){
        return this.configuration;
    }

    /**
     * Define the new web service
     * Define each controller and action
     * @param context Context of the WebService
     */
    @Override
    public void define(final Context context) {
        // create the new controller for the cnes web service
        final NewController controller = context.createController(
                StringManager.string(StringManager.CNES_CTRL_KEY));
        // set minimal sonarqube version required
        controller.setSince(StringManager.string(StringManager.SONAR_VERSION));
        // set description of the controller
        controller.setDescription(StringManager.string(StringManager.CNES_CTRL_DESCRIPTION));

        // create the action for URL /api/cnes/analyze
        analyzeAction(controller);

        // create the action for URL /api/cnes/report
        reportAction(controller);

        // create the action for URL /api/cnes/create_project
        projectAction(controller);

        // create the action for URL /api/cnes/configuration/pylintrc
        configurationAction(controller);


        // important to apply changes
        controller.done();
    }
    /**
     * Add the action corresponding to the report generation
     * @param controller controller to which add the action
     */
    private void configurationAction(final NewController controller) {
        final NewAction configuratoin = controller.createAction("configuration" );
        configuratoin.setDescription("Return CNES Scanner plugin user's set properties.");
        configuratoin.setSince("6.7.1");
        configuratoin.setHandler(new ConfigurationTask(this.configuration));

    }

    /**
     * Add the action corresponding to the scan
     * @param controller controller to which add the action
     */
    private void analyzeAction(final NewController controller) {
        final NewAction analysis = controller.createAction(
                StringManager.string(StringManager.ANALYZE_KEY));
        //set
        analysis.setDescription(StringManager.string(StringManager.ANALYZE_DESC));
        analysis.setSince(StringManager.string(StringManager.SONAR_VERSION));
        analysis.setPost(true);
        // new scan task to handle the request and work on the code
        analysis.setHandler(new AnalysisTask(this.configuration));
        // create parameter of the action
        // key parameter
        NewParam newParam = analysis.createParam(
                StringManager.string(StringManager.ANALYZE_KEY_NAME));
        newParam.setDescription(StringManager.string(StringManager.ANALYZE_KEY_DESC));
        newParam.setRequired(true);
        // name parameter
        newParam = analysis.createParam(StringManager.string(StringManager.ANALYZE_NAME_NAME));
        newParam.setDescription(StringManager.string(StringManager.ANALYZE_NAME_DESC));
        newParam.setRequired(true);
        // folder parameter
        newParam = analysis.createParam(StringManager.string(StringManager.ANALYZE_FOLDER_NAME));
        newParam.setDescription(StringManager.string(StringManager.ANALYZE_FOLDER_DESC));
        newParam.setRequired(true);
        // spp parameter
        newParam = analysis.createParam(StringManager.string(StringManager.ANALYZE_SPP_NAME));
        newParam.setDescription(StringManager.string(StringManager.ANALYZE_SPP_DESC));
        newParam.setRequired(true);
    }

    /**
     * Add the action corresponding to the report generation
     * @param controller controller to which add the action
     */
    private void reportAction(final NewController controller) {
        final NewAction report = controller.createAction(
                StringManager.string(StringManager.REPORT_KEY));
        report.setDescription(StringManager.string(StringManager.REPORT_DESC));
        report.setSince(StringManager.string(StringManager.SONAR_VERSION));
        report.setHandler(new ReportTask(this.configuration));
        // add the parameters of the controller
        // key parameter
        NewParam newParam = report.createParam(
                StringManager.string(StringManager.CNES_ACTION_REPORT_PARAM_KEY_NAME));
        newParam.setDescription(StringManager.string(StringManager.REPORT_KEY_DESC));
        newParam.setRequired(true);
        // author's name parameter
        newParam = report.createParam(
                StringManager.string(StringManager.CNES_ACTION_REPORT_PARAM_AUTHOR_NAME));
        newParam.setDescription(StringManager.string(StringManager.REPORT_AUTHOR_DESC));
        newParam.setRequired(true);
    }

    /**
     * Add the action corresponding to the project creation
     * @param controller controller to which add the action
     */
    private void projectAction(final NewController controller) {
        final NewAction project = controller.createAction(
                StringManager.string(StringManager.PROJECT_KEY));
        project.setDescription(StringManager.string(StringManager.PROJECT_DESC));
        project.setSince(StringManager.string(StringManager.SONAR_VERSION));
        project.setHandler(new ProjectTask());
        // add the parameters of the controller
        // key parameter
        NewParam newParam = project.createParam(
                StringManager.string(StringManager.PROJECT_PARAM_KEY_NAME));
        newParam.setDescription(StringManager.string(StringManager.PROJECT_PARAM_KEY_DESC));
        newParam.setRequired(true);
        // name of the project parameter
        newParam = project.createParam(StringManager.string(StringManager.PROJECT_PARAM_NAME_NAME));
        newParam.setDescription(StringManager.string(StringManager.PROJECT_PARAM_NAME_DESC));
        newParam.setRequired(true);
        // quality profiles parameter
        newParam = project.createParam(
                StringManager.string(StringManager.PROJECT_PARAM_PROFILES_NAME));
        newParam.setDescription(StringManager.string(StringManager.PROJECT_PARAM_PROFILES_DESC));
        newParam.setRequired(true);
        // quality gate parameter
        newParam = project.createParam(StringManager.string(StringManager.PROJECT_PARAM_GATE_NAME));
        newParam.setDescription(StringManager.string(StringManager.PROJECT_PARAM_GATE_DESC));
        newParam.setRequired(true);
    }

}