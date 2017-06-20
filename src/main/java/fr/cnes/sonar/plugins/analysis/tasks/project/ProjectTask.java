package fr.cnes.sonar.plugins.analysis.tasks.project;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import fr.cnes.sonar.plugins.analysis.tasks.AbstractTask;
import fr.cnes.sonar.plugins.analysis.utils.Status;
import fr.cnes.sonar.plugins.analysis.utils.StringManager;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.Response;
import org.sonarqube.ws.QualityProfiles.SearchWsResponse.QualityProfile;
import org.sonarqube.ws.WsComponents;
import org.sonarqube.ws.WsComponents.SearchProjectsWsResponse;
import org.sonarqube.ws.WsQualityGates;
import org.sonarqube.ws.client.*;
import org.sonarqube.ws.client.component.SearchProjectsRequest;
import org.sonarqube.ws.client.project.CreateRequest;
import org.sonarqube.ws.client.qualitygate.SelectWsRequest;
import org.sonarqube.ws.client.qualityprofile.AddProjectRequest;

import java.io.IOException;
import java.util.*;

import static fr.cnes.sonar.plugins.analysis.utils.StringManager.*;
import static java.util.Arrays.asList;

/**
 * Execute element to produce the report
 * @author garconb
 */
public class ProjectTask extends AbstractTask {

    /**
     *  Name of the field containing the list of quality gates
     */
    private static final String QUALITYGATES_JSON_FIELD = "qualitygates";
    /**
     *  Error message when a quality gate is unknown
     */
    private static final String QUALITY_GATE_UNKNOWN = "Quality gate %s does not exist.";
    /**
     *  Error message when a project key is known
     */
    private static final String PROJECT_ALREADY_EXISTS = "[WARNING] project %s already exists.";
    /**
     *  Error message when a quality profile is unknown
     */
    private static final String WARNING_QUALITYPROFILE_UNKNOWN = "[ERROR] quality profile %s is unknown.";
    /**
     *  Success message for quality profile linking
     */
    private static final String SUCCESS_QUALITYPROFILE = "[SUCCESS] quality profile %s was linked successfully.";
    /**
     *  Success message for quality gate linking
     */
    private static final String SUCCESS_QUALITYGATE = "[SUCCESS] quality gate %s was linked successfully.";
    /**
     *  Maximum page size for a request to the server
     */
    private static final int PAGE_SIZE = 500;
    /**
     *  Success message for project creation
     */
    private static final String SUCCESS_PROJECT = "[SUCCESS] project %s was created successfully.";

    /**
     * Create a project and set quality gate and profiles
     * If an error occurred nothing is reversed
     * You need permissions to use it.
     * @param request request coming from the user
     * @param response response to send to the user
     * @throws IOException ...
     */
    @Override
    public void handle(Request request, Response response) throws IOException {
        // reset logs to not stack them
        setLogs("");
        // describe how worked the task and is returned
        Status status = new Status();

        // extract parameters
        // key of the project to create
        String key = request.mandatoryParam(string(CNES_ACTION_3_PARAM_1_NAME));
        // name of the project to create
        String name = request.mandatoryParam(string(CNES_ACTION_3_PARAM_2_NAME));
        // quality profiles of the project to create
        String qualityProfiles = request.mandatoryParam(string(CNES_ACTION_3_PARAM_3_NAME));
        // quality gate of the project to create
        String qualityGate = request.mandatoryParam(string(CNES_ACTION_3_PARAM_4_NAME));
        // extract the list of quality profiles
        List<String> qualityProfilesList = parseQualityProfiles(qualityProfiles);


        // create a new client to talk with sonarqube's services
        WsClient wsClient = WsClientFactories.getLocal().newClient(request.localConnector());


        // create the project
        Status creationStatus = createProject(wsClient, key, name);
        // set the quality gate
        Status gateStatus = setQualityGate(wsClient, key, qualityGate);
        // set project's quality profiles
        Status profilesStatus = setQualityProfiles(wsClient, key, qualityProfilesList);


        // check if all steps worked correctly and prepare the response
        if(creationStatus.isSuccess() && gateStatus.isSuccess() && profilesStatus.isSuccess()) {
            status.setSuccess(true);
        }

        // log error concerning project creation
        status.merge(creationStatus);
        // log error concerning gate
        status.merge(gateStatus);
        // log error concerning profiles
        status.merge(profilesStatus);

        // write the json response
        response.newJsonWriter()
                .beginObject()
                // add logs to response
                .prop(string(CNES_ACTION_3_FIELD_1), status.getmMessage())
                // add success status
                .prop(string(CNES_ACTION_3_FIELD_2), status.isSuccess())
                .endObject()
                .close();

    }

    /**
     * Create a project
     * @param wsClient Client to use sonarqube's services
     * @param key Key of the project to create
     * @param name  Name of the project to create
     * @return logs of the task
     */
    private Status createProject(WsClient wsClient, String key, String name) {
        // describe how worked the task and is returned
        Status status = new Status();

        // search for project having the same key
        if(!checkProjectExists(wsClient, key)) {

            // prepare a request to ask for the creation of a project
            CreateRequest projectCreateRequest = CreateRequest.builder().setKey(key).setName(name).build();
            // make the project creation request to the server
            wsClient.projects().create(projectCreateRequest);
            // log success
            log(String.format(SUCCESS_PROJECT, name));
            status.setmMessage(String.format(SUCCESS_PROJECT, name));

        } else {
            // log error
            log(String.format(PROJECT_ALREADY_EXISTS, name));
            status.setmMessage(String.format(PROJECT_ALREADY_EXISTS, name));
        }

        // set the status of the function
        status.setSuccess(true);

        return status;
    }

    /**
     * Verify if a project already exists
     * @param wsClient Client to get project list to check
     * @param projectKey Key of the project to find
     * @return a boolean true if it exists
     */
    private boolean checkProjectExists(WsClient wsClient, String projectKey) {
        // returned value true if a project with the same key as projectKey exists
        boolean exist = false;

        // request to get all projects
        SearchProjectsRequest searchProjectsRequest = SearchProjectsRequest.builder().setPageSize(PAGE_SIZE).build();
        SearchProjectsWsResponse searchProjectsResponse = wsClient.components().searchProjects(searchProjectsRequest);

        // iterate on projects
        Iterator iterator = searchProjectsResponse.getComponentsList().iterator();

        // browse all projects until find the good one
        while(iterator.hasNext() && !exist) {
            WsComponents.Component current = (WsComponents.Component) iterator.next();
            exist = current.getKey().equals(projectKey);
        }

        return exist;
    }

    /**
     * Take the raw string of quality profiles' names
     * represented as profile1;profile2 ; profile3;profile4
     * and provide a List<String>
     * @param qualityProfiles raw string input
     * @return List<String> with th quality profiles' names
     */
    private List<String> parseQualityProfiles(String qualityProfiles) {
        // final result to return, never null
        List<String> list;

        // split with separators
        list = new LinkedList<>(asList(qualityProfiles.split(string(StringManager.CNES_COMMAND_PROJECT_PROFILES_SEPARATOR))));
        // clean useless blanks
        list.replaceAll(String::trim);
        // remove empty strings
        list.removeAll(Arrays.asList("", null));

        return list;
    }

    /**
     * Set quality profiles from a list of quality profiles' names
     * @param wsClient Client to talk to the server
     * @param key Key of the project to set
     * @param qualityProfilesList a list of quality profiles' names
     * @return A Status
     */
    private Status setQualityProfiles(WsClient wsClient, String key, List<String> qualityProfilesList) {
        // result to know if the gate was set
        Status status = new Status();
        status.setSuccess(true);

        // retrieve the list of available quality profiles
        qualityProfilesList.forEach((String profileName) -> {
            // Create a request to find all profile corresponding to a key
            // Execute the request
            // Filter to select only one result, example:
            // if you search key to, you can get [to, toto]
            // so we have to filter the response's list
            org.sonarqube.ws.client.qualityprofile.SearchWsRequest searchWsRequest =
                    new org.sonarqube.ws.client.qualityprofile.SearchWsRequest();
            searchWsRequest.setProfileName(profileName);
            List<QualityProfile> qpList = wsClient.qualityProfiles().search(searchWsRequest).getProfilesList();
            QualityProfile profile = findQPByName(qpList, profileName);

            // if there is at means one result we linked it to the project
            if(profile!=null) {
                // create the link (the request) between the current profile and the project
                AddProjectRequest addProjectRequest = AddProjectRequest.builder()
                        .setProfileKey(profile.getKey()).setProjectKey(key).build();
                // execute the previous request
                wsClient.qualityProfiles().addProject(addProjectRequest);
                // log result
                log(String.format(SUCCESS_QUALITYPROFILE, profileName));
                status.setmMessage(String.format(SUCCESS_QUALITYPROFILE, profileName));
            } else {
                // log warning when a profile could not be linked
                status.setmMessage(String.format(WARNING_QUALITYPROFILE_UNKNOWN, profileName));
                status.setSuccess(false);
            }
        });

        // return the status of the setting
        return status;
    }

    /**
     * Set the quality gate (found by name) of a given project (key)
     * @param wsClient Client to talk to the server
     * @param key Key of the project to set
     * @param qualityGateName Name of the quality gate to link
     * @return A Status
     */
    private Status setQualityGate(WsClient wsClient, String key, String qualityGateName) {
        // result to know if the gate was set
        Status status = new Status();
        // Retrieve all quality gates from server
        WsRequest listWsRequest = new GetRequest(string(CNES_REQUESTS_QUALITYGATES_LIST));
        WsResponse listWsResponse = wsClient.wsConnector().call(listWsRequest);

        // on previous success
        if(listWsResponse.isSuccessful()) {
            // tools to parse json
            Gson gson = new Gson();
            JsonParser jsonParser = new JsonParser();

            // get the quality gates list from the json and as json
            JsonElement json = jsonParser.parse(listWsResponse.content())
                    .getAsJsonObject().get(QUALITYGATES_JSON_FIELD);

            // convert the list to QualityGate entities list
            List<QualityGate> modelQGList = asList(gson.fromJson(json , QualityGate[].class));

            // fill out Sonar Quality Gates
            List<WsQualityGates.QualityGate> qualityGates = new ArrayList<>();
            for (QualityGate q : modelQGList) {
                // build and add a new quality gate from model
                qualityGates.add(WsQualityGates.QualityGate.newBuilder().setId(q.getId()).setName(q.getName()).build());
            }


            // look for the wanted quality gate
            WsQualityGates.QualityGate qg = findQGByName(qualityGates, qualityGateName);

            if(qg != null) {
                // if we found the quality gate we link it to the project
                SelectWsRequest selectWsRequest = new SelectWsRequest().setGateId(Long.parseLong(qg.getId())).setProjectKey(key);
                wsClient.qualityGates().associateProject(selectWsRequest);
                // setting is a success
                status.setSuccess(true);
                log(String.format(SUCCESS_QUALITYGATE, qualityGateName));
                status.setmMessage(String.format(SUCCESS_QUALITYGATE, qualityGateName));
            } else {
                // setting is not a success so we register the error message
                status.setmMessage(String.format(QUALITY_GATE_UNKNOWN, qualityGateName));
            }
        } else {
            // setting is not a success so we register the error message
            status.setmMessage(String.format(QUALITY_GATE_UNKNOWN, qualityGateName));
        }

        // return the status of the setting
        return status;
    }

    /**
     * Find a quality gate with its name
     * @param qualityGates quality gate list to browse
     * @param qualityGateName name of the quality gate to find
     * @return the corresponding quality gate or null
     */
    private WsQualityGates.QualityGate findQGByName(List<WsQualityGates.QualityGate> qualityGates, String qualityGateName) {
        // result that will be returned
        WsQualityGates.QualityGate qualityGate = null;

        // iterator on the quality gate list
        Iterator iterator = qualityGates.iterator();

        while(iterator.hasNext() && qualityGate==null) {
            WsQualityGates.QualityGate current = (WsQualityGates.QualityGate) iterator.next();
            // we check if the current quality gate is the good one (good name)
            qualityGate = current.getName().equals(qualityGateName) ? current : null;
        }

        return qualityGate;
    }

    /**
     * Find a quality profile with its name
     * @param qualityProfiles quality profile list to browse
     * @param qualityProfileName name of the quality profile to find
     * @return the corresponding quality profile or null
     */
    private QualityProfile findQPByName(List<QualityProfile> qualityProfiles, String qualityProfileName) {
        // result that will be returned
        QualityProfile qualityProfile = null;

        // iterator on the quality gate list
        Iterator iterator = qualityProfiles.iterator();

        while(iterator.hasNext() && qualityProfile==null) {
            QualityProfile current = (QualityProfile) iterator.next();
            // we check if the current quality gate is the good one (good name)
            qualityProfile = current.getName().equals(qualityProfileName) ? current : null;
        }

        return qualityProfile;
    }
}
