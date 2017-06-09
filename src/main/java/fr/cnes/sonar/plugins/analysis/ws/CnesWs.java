package fr.cnes.sonar.plugins.analysis.ws;

import fr.cnes.sonar.plugins.analysis.tasks.AnalysisTask;
import fr.cnes.sonar.plugins.analysis.tasks.ReportTask;
import org.sonar.api.server.ws.WebService;

import static fr.cnes.sonar.plugins.analysis.utils.StringManager.*;

/**
 * Expose CNES plugin api
 * @author garconb
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

        // important to apply changes
        controller.done();
    }

    /**
     * Add the action corresponding to the analysis
     * @param controller controller to which add the action
     */
    private void analyzeAction(NewController controller) {
        NewAction analysis = controller.createAction(string(CNES_ACTION_1_KEY))
                //set
        .setDescription(string(CNES_ACTION_1_DESC))
        .setSince(string(SONAR_VERSION))
        .setPost(true)
        .setHandler((request, response) -> {
            // read request parameters and generates response output

            // create the task to analyze the project
            AnalysisTask analysisWorker = new AnalysisTask();

            // concrete analysis
            String result = analysisWorker.analyze(
                    request.mandatoryParam(string(CNES_ACTION_1_PARAM_2_NAME)),
                    request.mandatoryParam(string(CNES_ACTION_1_PARAM_5_NAME)),
                    request.mandatoryParam(string(CNES_ACTION_1_PARAM_6_NAME))
            );

            // write the json response
            response.newJsonWriter()
               .beginObject()
                    // add logs to response
               .prop(string(CNES_ACTION_1_FIELD_1), result)
               .endObject()
               .close();
        });
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
                .setHandler((request, response) -> {
                    // read request parameters and generates response output
                    // create the report
                    ReportTask reportWorker = new ReportTask();
                    String result = reportWorker.report(
                            request.mandatoryParam(string(CNES_ACTION_2_PARAM_1_NAME)),
                            request.mandatoryParam(string(CNES_ACTION_2_PARAM_2_NAME)),
                            request.mandatoryParam(string(CNES_ACTION_2_PARAM_3_NAME)),
                            request.mandatoryParam(string(CNES_ACTION_2_PARAM_4_NAME)),
                            string(CNES_REPORTER_OUTPUT),
                            string(CNES_REPORTER_TEMPLATE)
                            );

                    // set the response
                    response.newJsonWriter()
                            .beginObject()
                            // add logs to response
                            .prop(string(CNES_ACTION_2_FIELD_1), result)
                            .endObject()
                            .close();
                });
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

}