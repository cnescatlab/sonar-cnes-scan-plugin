package fr.cnes.sonar.plugins.analysis.ws;

import fr.cnes.sonar.plugins.analysis.tasks.AnalysisTask;
import fr.cnes.sonar.plugins.analysis.tasks.ReportTask;
import org.sonar.api.server.ws.WebService;

/**
 * Expose CNES plugin api
 * @author garconb
 */
public class CnesWs implements WebService {

    /**
     * Define the minimal version of sonarqube
     */
    private static final String MIN_VERSION = "6.3.1";
    /**
     * Define the name of the quality profile parameter
     */
    private static final String QUALITY_PROFILE = "qualityprofile";
    /**
     * Define the name of the quality gate parameter
     */
    private static final String QUALITY_GATE = "qualitygate";
    /**
     * Define the name of the author parameter
     */
    private static final String AUTHOR = "author";
    /**
     * Define the name of the project's name parameter
     */
    private static final String NAME = "name";
    /**
     * Define the name of the projects's key parameter
     */
    private static final String KEY = "key";
    /**
     * Define the name of the returned log filed
     */
    private static final String LOGS = "logs";
    /**
     * Define the name of the projects's folder's name parameter
     */
    private static final String FOLDER = "folder";
    /**
     * Define the name of the projects's sonar-project.properties parameter
     */
    private static final String SONAR_PROJECT_PROPERTIES = "sonarProjectProperties";
    /**
     * Path where the report must be exported
     */
    private static final String REPORT_PATH = "/media/sf_Shared";
    /**
     * Template to use
     */
    private static final String TEMPLATE_PATH = "extensions/cnes/code-analysis-template.docx";

    /**
     * Define the new web service
     * Define each controller and action
     * @param context Context of the WebService
     */
    @Override
    public void define(Context context) {
        // create the new controller for the cnes web service
        NewController controller = context.createController("api/cnes");
        // set minimal sonarqube version required
        controller.setSince(MIN_VERSION);
        // set description of the controller
        controller.setDescription("Allow to analyze and export code analysis' reports with the CNES template.");

        // create the URL /api/cnes/analyze
        NewAction analysis = controller.createAction("analyze")
                //set
        .setDescription("Analyze a project.")
        .setSince(MIN_VERSION)
        .setPost(true)
        .setHandler((request, response) -> {
            // read request parameters and generates response output

            // create the task to analyze the project
            AnalysisTask analysisWorker = new AnalysisTask();

            // concrete analysis
            String result = analysisWorker.analyze(
                    request.mandatoryParam(FOLDER),
                    request.mandatoryParam(SONAR_PROJECT_PROPERTIES)
            );

            // write the json response
            response.newJsonWriter()
               .beginObject()
                    // add logs to response
               .prop(LOGS, result)
               .endObject()
               .close();
        });
        // create parameter of the action
        // key parameter
        analysis.createParam(KEY)
                .setDescription("Key of the project to analyze.").setRequired(true);
        // name parameter
        analysis.createParam(NAME)
                .setDescription("Name of the project to analyze.").setRequired(true);
        // quality profile parameter
        analysis.createParam(QUALITY_PROFILE)
                .setDescription("Name of the quality profile to use to analyze the project.").setRequired(true);
        // quality gate parameter
        analysis.createParam(QUALITY_GATE)
                .setDescription("Name of the quality gate to use to analyze the project.").setRequired(true);
        // folder parameter
        analysis.createParam(FOLDER)
                .setDescription("Name of the project folder.").setRequired(true);
        // spp parameter
        analysis.createParam(SONAR_PROJECT_PROPERTIES)
                .setDescription("The classical sonar-project.properties content.").setRequired(true);

        // create the URL /api/cnes/analyze
        NewAction report = controller.createAction("report")
                .setDescription("Generate the report of an analysis.")
                .setSince(MIN_VERSION)
                .setHandler((request, response) -> {
                    // read request parameters and generates response output
                    // create the report
                    ReportTask reportWorker = new ReportTask();
                    String result = reportWorker.report(
                            request.mandatoryParam(KEY),
                            request.mandatoryParam(QUALITY_PROFILE),
                            request.mandatoryParam(QUALITY_GATE),
                            request.mandatoryParam(NAME),
                            request.mandatoryParam(AUTHOR),
                            REPORT_PATH,
                            TEMPLATE_PATH
                            );

                    // set the response
                    response.newJsonWriter()
                            .beginObject()
                            // add logs to response
                            .prop(LOGS, result)
                            .endObject()
                            .close();
                });
        // add the parameters of the controller
        // key parameter
        report.createParam(KEY)
                .setDescription("Key of the project to report.").setRequired(true);
        // quality profile parameter
        report.createParam(QUALITY_PROFILE)
                .setDescription("Name of the quality profile used to analyze the project.").setRequired(true);
        // quality gate parameter
        report.createParam(QUALITY_GATE)
                .setDescription("Name of the quality gate used to analyze the project.").setRequired(true);
        // name of the project parameter
        report.createParam(NAME)
                .setDescription("Name of the report.").setRequired(true);
        // author's name parameter
        report.createParam(AUTHOR)
                .setDescription("Author of the report.").setRequired(true);

        // important to apply changes
        controller.done();
    }

}