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
package fr.cnes.sonar.plugins.scan.tasks.project;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import fr.cnes.sonar.plugins.scan.tasks.AbstractTask;
import fr.cnes.sonar.plugins.scan.utils.Status;
import fr.cnes.sonar.plugins.scan.utils.StringManager;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.Response;
import org.sonar.api.utils.text.JsonWriter;
import org.sonarqube.ws.Components;
import org.sonarqube.ws.Qualitygates;
import org.sonarqube.ws.Qualityprofiles;
import org.sonarqube.ws.client.*;
import org.sonarqube.ws.client.components.SearchProjectsRequest;
import org.sonarqube.ws.client.projects.CreateRequest;
import org.sonarqube.ws.client.qualitygates.SelectRequest;
import org.sonarqube.ws.client.qualityprofiles.AddProjectRequest;
import org.sonarqube.ws.client.qualityprofiles.SearchRequest;

import java.io.IOException;
import java.util.*;

/**
 * Execute element to produce the report
 * @author lequal
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
    private static final String PROJECT_ALREADY_EXISTS = "[WARN] Project %s already exists.";
    /**
     * Define the regex to match with for a project key
     */
    private static final String KEY_REGEX = "[a-zA-Z0-9_:.\\-]+";
    /**
     *  Error message when a project key is not matching the sonarqube regex
     */
    private static final String BAD_PROJECT_KEY =
            "[ERROR] Key %s does not match the following regular expression: " + KEY_REGEX;
    /**
     *  Error message when a quality profile is unknown
     */
    private static final String WARNING_QUALITYPROFILE_UNKNOWN =
            "[ERROR] Quality profile %s is unknown.";
    /**
     *  Success message for quality profile linking
     */
    private static final String SUCCESS_QUALITYPROFILE =
            "[INFO] Quality profile %s was linked successfully.";
    /**
     *  Success message for quality gate linking
     */
    private static final String SUCCESS_QUALITYGATE =
            "[INFO] Quality gate %s was linked successfully.";
    /**
     *  Maximum page size for a request to the server
     */
    private static final int PAGE_SIZE = 500;
    /**
     *  Success message for project creation
     */
    private static final String SUCCESS_PROJECT = "[INFO] Project %s was created successfully.";

    /**
     * Create a project and set quality gate and profiles
     * If an error occurred nothing is reversed
     * You need permissions to use it.
     * @param request request coming from the user
     * @param response response to send to the user
     * @throws IOException ...
     */
    @Override
    public void handle(final Request request, final Response response) throws IOException {
        // reset logs to not stack them
        setLogs("");
        // describe how worked the task and is returned
        final Status status = new Status();
        // extract parameters
        // key of the project to create
        final String key = request.mandatoryParam(
                StringManager.string(StringManager.PROJECT_PARAM_KEY_NAME));
        // name of the project to create
        final String name = request.mandatoryParam(
                StringManager.string(StringManager.PROJECT_PARAM_NAME_NAME));
        // quality profiles of the project to create
        String qualityProfiles = request.getParam(
                StringManager.string(StringManager.PROJECT_PARAM_PROFILES_NAME)).getValue();
        // Set default value for quality profiles
        if (qualityProfiles == null){
            qualityProfiles = "";
        }
        // quality gate of the project to create
        final String qualityGate = request.mandatoryParam(
                StringManager.string(StringManager.PROJECT_PARAM_GATE_NAME));
        // extract the list of quality profiles
        final List<String> qualityProfilesList = parseQualityProfiles(qualityProfiles);


        // create a new client to talk with sonarqube's services
        final WsClient wsClient = WsClientFactories.getLocal().newClient(request.localConnector());


        // create the project
        final Status creationStatus = createProject(wsClient, key, name);
        // log error concerning project creation
        status.merge(creationStatus);
        // check if all steps worked correctly and prepare the response
        if(creationStatus.isSuccess()) {
            // set the quality gate
            final Status gateStatus = setQualityGate(wsClient, key, qualityGate);
            // log error concerning gate
            status.merge(gateStatus);
            if(gateStatus.isSuccess()) {
                // set project's quality profiles
                final Status profilesStatus = setQualityProfiles(
                        wsClient, key, qualityProfilesList);
                // log error concerning profiles
                status.merge(profilesStatus);
                if(profilesStatus.isSuccess()) {
                    status.setSuccess(true);
                }
            }
        }

        // write the json response
        try (JsonWriter jsonWriter = response.newJsonWriter()) {
            jsonWriter.beginObject();
            // add logs to response
            final String logsField = StringManager.string(StringManager.PROJECT_REPONSE_LOG);
            jsonWriter.prop(logsField, status.getMessage());
            // add success status
            final String statusField = StringManager.string(StringManager.PROJECT_REPONSE_STATUS);
            jsonWriter.prop(statusField, status.isSuccess());
            jsonWriter.endObject();
        }
    }

    /**
     * Create a project
     * @param wsClient Client to use sonarqube's services
     * @param key Key of the project to create
     * @param name  Name of the project to create
     * @return logs of the task
     */
    private Status createProject(final WsClient wsClient, final String key, final String name) {
        // describe how worked the task and is returned
        final Status status = new Status();

        // we check if the key is correct
        if(key.matches(KEY_REGEX)) {
            // search for project having the same key
            if (!checkProjectExists(wsClient, key)) {

                // prepare a request to ask for the creation of a project
                final CreateRequest requestBuilder = new CreateRequest();
                requestBuilder.setProject(key);
                requestBuilder.setName(name);
                // make the project creation request to the server
                wsClient.projects().create(requestBuilder);
                // log success
                log(String.format(SUCCESS_PROJECT, name));
                status.setMessage(String.format(SUCCESS_PROJECT, name));

            } else {
                // log error
                log(String.format(PROJECT_ALREADY_EXISTS, name));
                status.setMessage(String.format(PROJECT_ALREADY_EXISTS, name));
            }

            // set the status of the function
            status.setSuccess(true);
        } else {
            // log error
            log(String.format(BAD_PROJECT_KEY, key));
            status.setMessage(String.format(BAD_PROJECT_KEY, key));
        }

        return status;
    }

    /**
     * Verify if a project already exists
     * @param wsClient Client to get project list to check
     * @param projectKey Key of the project to find
     * @return a boolean true if it exists
     */
    private boolean checkProjectExists(final WsClient wsClient, final String projectKey) {
        // returned value true if a project with the same key as projectKey exists
        boolean exist = false;

        // request to get all projects
        final SearchProjectsRequest searchProjectsRequest =
                new SearchProjectsRequest().setPs(String.valueOf(PAGE_SIZE));
        final Components.SearchProjectsWsResponse searchProjectsResponse =
                wsClient.components().searchProjects(searchProjectsRequest);

        // iterate on projects
        final Iterator<Components.Component> iterator =
                searchProjectsResponse.getComponentsList().iterator();
        Components.Component current;

        // browse all projects until find the good one
        while(iterator.hasNext() && !exist) {
            current = iterator.next();
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
    private List<String> parseQualityProfiles(final String qualityProfiles) {
        // final result to return, never null
        final List<String> list;

        // split with separators
        list = new LinkedList<>(Arrays.asList(qualityProfiles.split(
                StringManager.string(StringManager.CNES_COMMAND_PROJECT_PROFILES_SEPARATOR))));
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
     * @param qualityProfilesList a list of quality profiles' keys
     * @return A Status
     */
    private Status setQualityProfiles(final WsClient wsClient, final String key,
                                      final List<String> qualityProfilesList) {
        // result to know if the gate was set
        final Status status = new Status();
        status.setSuccess(true);
        final Status tmpStatus = new Status();

        // retrieve the list of available quality profiles
        qualityProfilesList.forEach((String profileKey) -> {
            // Create a request to find all profile corresponding to a key
            // Execute the request
            // Filter to select only one result, example:
            // if you search key to, you can get [to, toto]
            // so we have to filter the response's list

        	final SearchRequest searchWsRequest =
                new SearchRequest();
            final List<Qualityprofiles.SearchWsResponse.QualityProfile> qpList = wsClient.qualityprofiles()
                    .search(searchWsRequest).getProfilesList();
            final Qualityprofiles.SearchWsResponse.QualityProfile profile = findQPByKey(qpList, profileKey);

            // if there is at means one result we linked it to the project
            if(profile!=null) {
                // create the link (the request) between the current profile and the project
            	final AddProjectRequest addProjectRequest = new AddProjectRequest()
                        .setLanguage(profile.getLanguage())
                        .setProject(key)
                        .setQualityProfile(profile.getName());
                // execute the previous request
                wsClient.qualityprofiles().addProject(addProjectRequest);
                // log result
                log(String.format(SUCCESS_QUALITYPROFILE, profileKey));
                tmpStatus.setMessage(String.format(SUCCESS_QUALITYPROFILE, profileKey));
                tmpStatus.setSuccess(true);
            } else {
                // log warning when a profile could not be linked
                tmpStatus.setMessage(String.format(WARNING_QUALITYPROFILE_UNKNOWN, profileKey));
                tmpStatus.setSuccess(false);
            }

            status.merge(tmpStatus);
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
    private Status setQualityGate(final WsClient wsClient, final String key,
                                  final String qualityGateName) {
        // result to know if the gate was set
        final Status status = new Status();
        // Retrieve all quality gates from server
        final WsRequest listWsRequest = new GetRequest(
                StringManager.string(StringManager.CNES_REQUESTS_QUALITYGATES_LIST));
        final WsResponse listWsResponse = wsClient.wsConnector().call(listWsRequest);

        // on previous success
        if(listWsResponse.isSuccessful()) {
            // tools to parse json
            final Gson gson = new Gson();
            final JsonParser jsonParser = new JsonParser();

            // get the quality gates list from the json and as json
            final JsonElement json = jsonParser.parse(listWsResponse.content())
                    .getAsJsonObject().get(QUALITYGATES_JSON_FIELD);

            // convert the list to QualityGate entities list
            final List<QualityGate> modelQGList = Arrays.asList(
                    gson.fromJson(json , QualityGate[].class));

            // fill out Sonar Quality Gates
            final List<Qualitygates.QualityGate> qualityGates = new ArrayList<>();
            Qualitygates.QualityGate.Builder qualityGateBuilder;
            for (final QualityGate q : modelQGList) {
                // build and add a new quality gate from model
                qualityGateBuilder = Qualitygates.QualityGate.newBuilder();
                qualityGateBuilder.setId(Long.parseLong(q.getId()));
                qualityGateBuilder.setName(q.getName());
                qualityGates.add(qualityGateBuilder.build());
            }


            // look for the wanted quality gate
            final Qualitygates.QualityGate qg = findQGByName(qualityGates, qualityGateName);

            if(qg != null) {
                // if we found the quality gate we link it to the project
                final SelectRequest selectWsRequest = new SelectRequest()
                        .setGateId(String.valueOf(qg.getId())).setProjectKey(key);
                wsClient.qualitygates().select(selectWsRequest);
                // setting is a success
                status.setSuccess(true);
                log(String.format(SUCCESS_QUALITYGATE, qualityGateName));
                status.setMessage(String.format(SUCCESS_QUALITYGATE, qualityGateName));
            } else {
                // setting is not a success so we register the error message
                status.setMessage(String.format(QUALITY_GATE_UNKNOWN, qualityGateName));
            }
        } else {
            // setting is not a success so we register the error message
            status.setMessage(String.format(QUALITY_GATE_UNKNOWN, qualityGateName));
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
    private Qualitygates.QualityGate findQGByName(final List<Qualitygates.QualityGate> qualityGates,
                                                    final String qualityGateName) {
        // result that will be returned
        Qualitygates.QualityGate qualityGate = null;

        // iterator on the quality gate list
        final Iterator<Qualitygates.QualityGate> iterator = qualityGates.iterator();
        Qualitygates.QualityGate current;

        while(iterator.hasNext() && qualityGate==null) {
            current = iterator.next();
            // we check if the current quality gate is the good one (good name)
            if(current.getName().equals(qualityGateName)) {
                qualityGate = current;
            }
        }

        return qualityGate;
    }

    /**
     * Find a quality profile with its key
     * @param qualityProfiles quality profile list to browse
     * @param qualityProfileKey key of the quality profile to find
     * @return the corresponding quality profile or null
     */
    private Qualityprofiles.SearchWsResponse.QualityProfile findQPByKey(final List<Qualityprofiles.SearchWsResponse.QualityProfile> qualityProfiles,
                                                                        final String qualityProfileKey) {
        // result that will be returned
        Qualityprofiles.SearchWsResponse.QualityProfile qualityProfile = null;

        // iterator on the quality gate list
        final Iterator<Qualityprofiles.SearchWsResponse.QualityProfile> iterator = qualityProfiles.iterator();
        Qualityprofiles.SearchWsResponse.QualityProfile current;

        while(iterator.hasNext() && qualityProfile==null) {
            current = iterator.next();
            // we check if the current quality profile is the good one (good key)
            if(current.getKey().equals(qualityProfileKey)) {
                qualityProfile = current;
            }
        }

        return qualityProfile;
    }
}
