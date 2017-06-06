window.registerExtension('cnes/analysis', function (options) {
    // let's create a flag telling if the page is still displayed
    var isDisplayedAnalysis = true;

    // contain the template of the page
    var htmlTemplate = '\
    <div id="bd" class="page-wrapper-simple">\
        <div id="nonav" class="page-simple" style="width:100%; margin-left: 10%; margin-right: 10%;">\
            <div>\
                <h1 class="maintenance-title text-center">Analyze a project</h1>\
                <form id="analyze-form"><!-- react-empty: 33 -->\
                    <div class="big-spacer-bottom">\
                        <label for="key" class="login-label">Project key<em style="color:red;">*</em></label><input required type="text"\
                            id="key"\
                            name="key"\
                            class="login-input"\
                            maxlength="255"\
                            required="true"\
                            placeholder="Project key"><em style="color:red;">* This field is mandatory.</em>\
                    </div>\
                    <div class="big-spacer-bottom">\
                        <label for="name" class="login-label" >Project name<em style="color:red;">*</em></label><input required type="text"\
                            id="name"\
                            name="name"\
                            class="login-input"\
                            maxlength="255"\
                            required="true"\
                            placeholder="Project name"><em style="color:red;">* This field is mandatory.</em>\
                    </div>\
                    <div class="big-spacer-bottom">\
                        <label for="folder" class="login-label">Project folder<em style="color:red;">*</em></label><input required type="text"\
                            id="folder"\
                            name="folder"\
                            class="login-input"\
                            maxlength="255"\
                            required="true"\
                            placeholder="Project folder"><em style="color:red;">* This field is mandatory.</em>\
                    </div>\
                    <div class="big-spacer-bottom">\
                        <label for="author" class="login-label">Author</label><input type="text"\
                            id="author"\
                            name="author"\
                            class="login-input"\
                            maxlength="255"\
                            required="true"\
                            placeholder="Report author">\
                            <em style="color:grey;">Default value: default</em>\
                    </div>\
                    <div class="big-spacer-bottom">\
                        <label for="quality-gate" class="login-label">Project quality gate</label><input type="text"\
                            id="quality-gate"\
                            name="quality-gate"\
                            class="login-input"\
                            maxlength="255"\
                            required="true"\
                            placeholder="Project quality gate">\
                            <em style="color:grey;">Default value: CNES</em>\
                    </div>\
                    <div class="big-spacer-bottom">\
                        <label for="quality-profile" class="login-label">Project quality profile</label><input type="text"\
                            id="quality-profile"\
                            name="quality-profile"\
                            class="login-input"\
                            maxlength="255"\
                            required="true"\
                            placeholder="Project quality profile">\
                            <em style="color:grey;">Default value: default quality profiles</em>\
                    </div>\
                    <div class="big-spacer-bottom">\
                        <label for="spp" class="login-label">sonar-project.properties<em style="color:red;">*</em></label><textarea required type="text"\
                            id="spp"\
                            name="spp"\
                            class="login-input"\
                            rows="25"\
                            required="true"\
                            ># Required metadata\nsonar.projectKey=<<TO REPLACE>>\nsonar.projectName=<<TO REPLACE>>\nsonar.projectDescription=<<TO REPLACE>>\nsonar.projectVersion=<<TO REPLACE>>\nsonar.language=<<TO REPLACE>>\n\n# Path to files\nsonar.sources=<<TO REPLACE>>\nsonar.tests=<<TO REPLACE>>\nsonar.java.binaries=<<TO REPLACE>>\n\n# Encoding of the source files\nsonar.sourceEncoding=UTF-8\n</textarea><em style="color:red;">* This field is mandatory.</em>\
                    </div>\
                    <div class="big-spacer-bottom">\
                        <div class="text-center overflow-hidden">\
                            <input id="analyze" name="analyze" type="button" value="Analyze">\
                            <input id="clear" class="button button-red spacer-left" type="reset" value="Reset">\
                        </div>\
                    </div>\
                    <textarea id="logging" name="logging" class="login-input" rows="5" required="false" style="background: black; color:white;" readonly="">## Logging console ##</textarea>\
                </form>\
            </div>\
        </div>\
    </div>';

    /**
     * Verify that the fields are correct.
     * @returns {boolean} true if all is good
     */
    var checkForm = function () {
        // check the field key
        // get it
        var key = document.forms["analyze-form"]["key"].value;
        // check if void
        if (key === "") {
            // log error
            log("Key must be filled out.");
            // abort the process
            return false;
        }
        // check the field name (project)
        // get it
        var name = document.forms["analyze-form"]["name"].value;
        // check if void
        if (name === "") {
            log("Name must be filled out.");
            // abort the process
            return false;
        }
        // check the field folder (project)
        // get it
        var folder = document.forms["analyze-form"]["folder"].value;
        // check if void
        if (folder === "") {
            log("Folder must be filled out.");
            // abort the process
            return false;
        }
        // check the field spp (sonar-project.properties)
        // get it
        var spp = document.forms["analyze-form"]["spp"].value;
        // check if void
        if (spp === "") {
            log("sonar-project.properties must be filled out.");
            // abort the process
            return false;
        // check if it contains <<TO REPLACE>>
        } else if(spp.indexOf("<<TO REPLACE>>")!==-1) {
            log("sonar-project.properties was not correctly filled out. Replace all '<<TO REPLACE>>' fields.");
            // abort the process
            return false;
        }
        return true;
    };

    /**
     * Log information in the bottom text area
     * @param string Text to log
     */
    var log = function (string) {
        // get the logging element
        var logging = document.querySelector('#logging');
        // append text to log
        logging.innerHTML = logging.innerHTML + "\n" + string;
    };

    /**
     * Set the quality gate and profile of the project
     * @param projectKey Key of the project
     * @param qualityGate value for the quality gate
     * @param qualityProfile value for the quality profile
     */
    var setQualityParams = function (projectKey, name, folder, qualityGate, qualityProfile, spp, author) {

        // check if the quality gate field is filled out
        qualityGate = qualityGate === "" ? "CNES" : qualityGate;

        // Quality gate setting Basic YWRtaW46YWRtaW4=
        window.SonarRequest.getJSON(    // get the id from the name
            '/api/qualitygates/show?name='+qualityGate
        ).then(function (response) {
        // it exists
        // request the setting of the quality gate
            window.SonarRequest.request(
                '/api/qualitygates/select'
            ).setHeader(
            // need admin permissions
                "Authorization", "Basic YWRtaW46YWRtaW4="
            ).setMethod(
            // use post method
                "POST"
            ).setData(
                { projectKey: projectKey, gateId: response.id }
            ).submit().then(function (response) {
                log("[INFO] Quality gate selection response: " + response.status);
            }).catch(function (error) {
                log("[WARNING] There were a problem during quality gate's setting.");
            });
        }).catch(function (error) {
        // on error log it
            log("[WARNING] The quality gate does not exist.");
        }).then(function () {
            // set the quality profile if the field is not empty
            if(qualityProfile!=="") {
                // Quality profile setting Basic YWRtaW46YWRtaW4=
                window.SonarRequest.getJSON(
                // get the id from the name
                    '/api/qualityprofiles/search?profileName='+qualityProfile
                ).then(function (response) {
                // if it exists
                    if(response.profiles !== undefined && response.profiles.length >= 1) {
                        // filter profile with the good name
                        var filteredProfiles = response.profiles.filter(function (profile) {
                            return profile.name === qualityProfile;
                        });
                        // if there is at means one profile
                        if(filteredProfiles.length >= 1) {
                            // request to set the profile
                            window.SonarRequest.request(
                                '/api/qualityprofiles/add_project'
                            ).setHeader(
                                "Authorization", "Basic YWRtaW46YWRtaW4="
                            ).setMethod(
                                "POST"
                            ).setData(
                                { projectKey: projectKey, profileKey: filteredProfiles[0].key }
                            ).submit().then(function (response) {
                                log("[INFO] Quality profile selection response: " + response.status);
                                runAnalysis(projectKey, name, folder, qualityGate, qualityProfile, spp, author);
                            }).catch(function (error) {
                                log("[WARNING] There were a problem during quality profile's setting.");
                            });
                        } else {
                            // log error
                            log("[WARNING] The quality profile does not exists.");
                        }

                    } else {
                        // log error
                        log("[WARNING] The quality profile does not exists.");
                    }
                }).catch(function (error) {
                    // log error
                    log("[WARNING] The quality profile does not exist.");
                });
            } else {
                // if there is no need to set a profile we just launch the analysis
                runAnalysis(projectKey, name, folder, qualityGate, qualityProfile, spp, author);
            }

        });
    };

    /**
     * Run the analysis
     * @param key
     * @param name
     * @param folder
     * @param qualitygate
     * @param qualityprofile
     * @param spp
     */
    var runAnalysis = function (key, name, folder, qualitygate, qualityprofile, spp, author) {
        // send post request to the cnes web service
        window.SonarRequest.postJSON(
            '/api/cnes/analyze',
            { key: key, name: name, folder: folder, qualitygate: qualitygate, qualityprofile: qualityprofile, sonarProjectProperties: spp }
        ).then(function (response) {
            // on success
            // log output
            log("[INFO] Project analysis response: \n" + response.logs);
            // produce the report
            produceReport(key, name, qualitygate, qualityprofile, author);
        }).catch(function (error) {
            // log error
            log("[ERROR] Project analysis failed.");
        });
    };

    /**
     * Generate the report
     * @param key
     * @param name
     * @param qualitygate
     * @param qualityprofile
     * @param author
     */
    var produceReport = function (key, name, qualitygate, qualityprofile, author) {
        // http GET request to the cnes web service
        window.SonarRequest.getJSON(
            '/api/cnes/report',
            { key: key, name: name, qualitygate: qualitygate, qualityprofile: qualityprofile, author: author }
        ).then(function (response) {
            // on success log generation
            log("[INFO] Project report generation response: \n" + response.logs);
            log("############################################################\n\tAnalysis finished with success!\n############################################################\n")
        }).catch(function (error) {
            // log error
            log("[ERROR] Project report generation failed.");
        });
    };

    // once the request is done, and the page is still displayed (not closed already)
    if (isDisplayedAnalysis) {

        // Add html template
        var template = document.createElement("template");
        template.innerHTML = htmlTemplate;
        options.el.appendChild(template);
        options.el.appendChild(document.importNode(template.content, true));

        // set analyze button action
        var analyzeButton = document.querySelector('#analyze');
        // set its action on click
        analyzeButton.onclick = function () {

            // validation of the form
            if(checkForm()) {

                // Get form values
                var key = document.forms["analyze-form"]["key"].value;
                var name = document.forms["analyze-form"]["name"].value;
                var folder = document.forms["analyze-form"]["folder"].value;
                var qgate = document.forms["analyze-form"]["quality-gate"].value;
                var qprofile = document.forms["analyze-form"]["quality-profile"].value;
                var author = document.forms["analyze-form"]["author"].value;
                var spp = document.forms["analyze-form"]["spp"].value;

                // request the creation of the project
                window.SonarRequest.post(
                    '/api/projects/create',
                    { project: key, name: name }
                ).then(function (response) {
                    // on success log and set quality params
                    log("[INFO] Project creation response: " + response.status);
                    setQualityParams(key, name, folder, qgate, qprofile, spp, author);
                }).catch(function (error) {
                    // on failure log and set quality params too
                    log("[WARNING] This project was not created. Maybe, it was already created.");
                    setQualityParams(key, name, folder, qgate, qprofile, spp, author);
                });
            }
        }

    }

    // return a function, which is called when the page is being closed
    return function () {
        // we unset the `isDisplayedAnalysis` flag to ignore to Web API calls finished after the page is closed
        isDisplayedAnalysis = false;
        options.el.textContent = '';
    };
});