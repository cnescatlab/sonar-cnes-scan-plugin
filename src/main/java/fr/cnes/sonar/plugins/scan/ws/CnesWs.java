package fr.cnes.sonar.plugins.scan.ws;

import fr.cnes.sonar.plugins.scan.tasks.AnalysisTask;
import fr.cnes.sonar.plugins.scan.tasks.ReportTask;
import fr.cnes.sonar.plugins.scan.tasks.project.ProjectTask;
import org.sonar.api.server.ws.WebService;

import static fr.cnes.sonar.plugins.scan.utils.StringManager.*;

/**
 * Expose CNES plugin api
 * @author begarco
 */
public class CnesWs implements WebService {

    /**
     * Define the new web service
     * Define each controller and action
     * @param context Context of the WebService
     */
    @Override
    public void define(Context context) {
        // create the new controller for the cnes web service
        NewController controller = context.createController(string(CNES_CTRL_KEY));
        // set minimal sonarqube version required
        controller.setSince(string(SONAR_VERSION));
        // set description of the controller
        controller.setDescription(string(CNES_CTRL_DESCRIPTION));

        // create the action for URL /api/cnes/analyze
        analyzeAction(controller);

        // create the action for URL /api/cnes/report
        reportAction(controller);

        // create the action for URL /api/cnes/create_project
        projectAction(controller);

        // important to apply changes
        controller.done();
    }

    /**
     * Add the action corresponding to the scan
     * @param controller controller to which add the action
     */
    private void analyzeAction(NewController controller) {
        NewAction analysis = controller.createAction(string(CNES_ACTION_1_KEY))
                //set
                .setDescription(string(CNES_ACTION_1_DESC))
                .setSince(string(SONAR_VERSION))
                .setPost(true)
                // new scan task to handle the request and work on the code
                .setHandler(new AnalysisTask());
        // create parameter of the action
        // key parameter
        analysis.createParam(string(CNES_ACTION_1_PARAM_1_NAME))
                .setDescription(string(CNES_ACTION_1_PARAM_1_DESC)).setRequired(true);
        // name parameter
        analysis.createParam(string(CNES_ACTION_1_PARAM_2_NAME))
                .setDescription(string(CNES_ACTION_1_PARAM_2_DESC)).setRequired(true);
        // quality profile parameter
        analysis.createParam(string(CNES_ACTION_1_PARAM_3_NAME))
                .setDescription(string(CNES_ACTION_1_PARAM_3_DESC)).setRequired(true);
        // quality gate parameter
        analysis.createParam(string(CNES_ACTION_1_PARAM_4_NAME))
                .setDescription(string(CNES_ACTION_1_PARAM_4_DESC)).setRequired(true);
        // folder parameter
        analysis.createParam(string(CNES_ACTION_1_PARAM_5_NAME))
                .setDescription(string(CNES_ACTION_1_PARAM_5_DESC)).setRequired(true);
        // spp parameter
        analysis.createParam(string(CNES_ACTION_1_PARAM_6_NAME))
                .setDescription(string(CNES_ACTION_1_PARAM_6_DESC)).setRequired(true);
    }

    /**
     * Add the action corresponding to the report generation
     * @param controller controller to which add the action
     */
    private void reportAction(NewController controller) {
        NewAction report = controller.createAction(string(CNES_ACTION_2_KEY))
                .setDescription(string(CNES_ACTION_2_DESC))
                .setSince(string(SONAR_VERSION))
                .setHandler(new ReportTask());
        // add the parameters of the controller
        // key parameter
        report.createParam(string(CNES_ACTION_2_PARAM_1_NAME))
                .setDescription(string(CNES_ACTION_2_PARAM_1_DESC)).setRequired(true);
        // quality gate parameter
        report.createParam(string(CNES_ACTION_2_PARAM_2_NAME))
                .setDescription(string(CNES_ACTION_2_PARAM_2_DESC)).setRequired(true);
        // name of the project parameter
        report.createParam(string(CNES_ACTION_2_PARAM_3_NAME))
                .setDescription(string(CNES_ACTION_2_PARAM_3_DESC)).setRequired(true);
        // author's name parameter
        report.createParam(string(CNES_ACTION_2_PARAM_4_NAME))
                .setDescription(string(CNES_ACTION_2_PARAM_4_DESC)).setRequired(true);
    }

    /**
     * Add the action corresponding to the project creation
     * @param controller controller to which add the action
     */
    private void projectAction(NewController controller) {
        NewAction project = controller.createAction(string(CNES_ACTION_3_KEY))
                .setDescription(string(CNES_ACTION_3_DESC))
                .setSince(string(SONAR_VERSION))
                .setHandler(new ProjectTask());
        // add the parameters of the controller
        // key parameter
        project.createParam(string(CNES_ACTION_3_PARAM_1_NAME))
                .setDescription(string(CNES_ACTION_3_PARAM_1_DESC)).setRequired(true);
        // name of the project parameter
        project.createParam(string(CNES_ACTION_3_PARAM_2_NAME))
                .setDescription(string(CNES_ACTION_3_PARAM_2_DESC)).setRequired(true);
        // quality profiles parameter
        project.createParam(string(CNES_ACTION_3_PARAM_3_NAME))
                .setDescription(string(CNES_ACTION_3_PARAM_3_DESC)).setRequired(true);
        // quality gate parameter
        project.createParam(string(CNES_ACTION_3_PARAM_4_NAME))
                .setDescription(string(CNES_ACTION_3_PARAM_4_DESC)).setRequired(true);
    }

}