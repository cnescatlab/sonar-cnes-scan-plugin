package fr.cnes.sonar.plugins.analysis.ws;

import fr.cnes.sonar.plugins.analysis.tasks.AnalysisTask;
import fr.cnes.sonar.plugins.analysis.tasks.ReportTask;
import org.sonar.api.server.ws.WebService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Expose CNES plugin api
 * @author garconb
 */
public class CnesWs implements WebService {

    @Override
    public void define(Context context) {
        NewController controller = context.createController("api/cnes");
        controller.setSince("6.3.1");
        controller.setDescription("Allow to analyze and export code analysis' reports with the CNES template.");

        // create the URL /api/cnes/analyze
        NewAction analysis = controller.createAction("analyze")
        .setDescription("Analyze a project.")
        .setSince("6.3.1")
        .setPost(true)
        .setHandler((request, response) -> {
            // read request parameters and generates response output

            AnalysisTask analysisWorker = new AnalysisTask();
            String result = analysisWorker.analyze(
                    request.mandatoryParam("key"),
                    request.mandatoryParam("name"),
                    request.mandatoryParam("qualityprofile"),
                    request.mandatoryParam("qualitygate"),
                    request.mandatoryParam("folder"),
                    request.mandatoryParam("sonarProjectProperties")
            );

            response.newJsonWriter()
               .beginObject()
               .prop("logs", result)
               .endObject()
               .close();
        });
        analysis.createParam("key").setDescription("Key of the project to analyze.").setRequired(true);
        analysis.createParam("name").setDescription("Name of the project to analyze.").setRequired(true);
        analysis.createParam("qualityprofile").setDescription("Name of the quality profile to use to analyze the project.").setRequired(true);
        analysis.createParam("qualitygate").setDescription("Name of the quality gate to use to analyze the project.").setRequired(true);
        analysis.createParam("folder").setDescription("Name of the project folder.").setRequired(true);
        analysis.createParam("sonarProjectProperties").setDescription("The classical sonar-project.properties content.").setRequired(true);

        // create the URL /api/cnes/analyze
        NewAction report = controller.createAction("report")
                .setDescription("Generate the report of an analysis.")
                .setSince("6.3.1")
                .setHandler((request, response) -> {
                    // read request parameters and generates response output

                    ReportTask reportWorker = new ReportTask();
                    String result = reportWorker.report(
                            request.mandatoryParam("key"),
                            request.mandatoryParam("qualityprofile"),
                            request.mandatoryParam("qualitygate"),
                            request.mandatoryParam("name"),
                            request.mandatoryParam("author"),
                            new SimpleDateFormat("dd-MM-yyyy").format(new Date()),
                            "/media/sf_Shared",
                            "extensions/cnes/code-analysis-template.docx"
                            );

                    response.newJsonWriter()
                            .beginObject()
                            .prop("logs", result)
                            .endObject()
                            .close();
                });
        report.createParam("key").setDescription("Key of the project to report.").setRequired(true);
        report.createParam("qualityprofile").setDescription("Name of the quality profile used to analyze the project.").setRequired(true);
        report.createParam("qualitygate").setDescription("Name of the quality gate used to analyze the project.").setRequired(true);
        report.createParam("name").setDescription("Name of the report.").setRequired(true);
        report.createParam("author").setDescription("Author of the report.").setRequired(true);

        // important to apply changes
        controller.done();
    }

}