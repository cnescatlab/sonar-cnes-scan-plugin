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
        NewAction analysis = controller.createAction(string(ANALYZE_KEY));
        //set
        analysis.setDescription(string(ANALYZE_DESC));
        analysis.setSince(string(SONAR_VERSION));
        analysis.setPost(true);
        // new scan task to handle the request and work on the code
        analysis.setHandler(new AnalysisTask());
        // create parameter of the action
        // key parameter
        NewParam newParam = analysis.createParam(string(ANALYZE_KEY_NAME));
        newParam.setDescription(string(ANALYZE_KEY_DESC));
        newParam.setRequired(true);
        // name parameter
        newParam = analysis.createParam(string(ANALYZE_NAME_NAME));
        newParam.setDescription(string(ANALYZE_NAME_DESC));
        newParam.setRequired(true);
        // folder parameter
        newParam = analysis.createParam(string(ANALYZE_FOLDER_NAME));
        newParam.setDescription(string(ANALYZE_FOLDER_DESC));
        newParam.setRequired(true);
        // spp parameter
        newParam = analysis.createParam(string(ANALYZE_SPP_NAME));
        newParam.setDescription(string(ANALYZE_SPP_DESC));
        newParam.setRequired(true);
    }

    /**
     * Add the action corresponding to the report generation
     * @param controller controller to which add the action
     */
    private void reportAction(NewController controller) {
        NewAction report = controller.createAction(string(REPORT_KEY));
        report.setDescription(string(REPORT_DESC));
        report.setSince(string(SONAR_VERSION));
        report.setHandler(new ReportTask());
        // add the parameters of the controller
        // key parameter
        NewParam newParam = report.createParam(string(CNES_ACTION_REPORT_PARAM_KEY_NAME));
        newParam.setDescription(string(REPORT_KEY_DESC));
        newParam.setRequired(true);
        // author's name parameter
        newParam = report.createParam(string(CNES_ACTION_REPORT_PARAM_AUTHOR_NAME));
        newParam.setDescription(string(REPORT_AUTHOR_DESC));
        newParam.setRequired(true);
    }

    /**
     * Add the action corresponding to the project creation
     * @param controller controller to which add the action
     */
    private void projectAction(NewController controller) {
        NewAction project = controller.createAction(string(PROJECT_KEY));
        project.setDescription(string(PROJECT_DESC));
        project.setSince(string(SONAR_VERSION));
        project.setHandler(new ProjectTask());
        // add the parameters of the controller
        // key parameter
        NewParam newParam = project.createParam(string(PROJECT_PARAM_KEY_NAME));
        newParam.setDescription(string(PROJECT_PARAM_KEY_DESC));
        newParam.setRequired(true);
        // name of the project parameter
        newParam = project.createParam(string(PROJECT_PARAM_NAME_NAME));
        newParam.setDescription(string(PROJECT_PARAM_NAME_DESC));
        newParam.setRequired(true);
        // quality profiles parameter
        newParam = project.createParam(string(PROJECT_PARAM_PROFILES_NAME));
        newParam.setDescription(string(PROJECT_PARAM_PROFILES_DESC));
        newParam.setRequired(true);
        // quality gate parameter
        newParam = project.createParam(string(PROJECT_PARAM_GATE_NAME));
        newParam.setDescription(string(PROJECT_PARAM_GATE_DESC));
        newParam.setRequired(true);
    }

}