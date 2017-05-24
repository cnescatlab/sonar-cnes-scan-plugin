window.registerExtension('cnes/analysis', function (options) {
    // let's create a flag telling if the page is still displayed
    var isDisplayed = true;
    var qualityprofiles = [];
    var qualitygates = [];
    var htmlTemplate = '\
    <div id="bd" class="page-wrapper-simple">\
        <div id="nonav" class="page-simple" style="width:100%; margin-left: 10%; margin-right: 10%;">\
            <div>\
                <h1 class="maintenance-title text-center">Analyze a project</h1>\
                <form id="analyze-form"><!-- react-empty: 33 -->\
                    <div class="big-spacer-bottom">\
                        <label for="key" class="login-label">Project key</label><input type="text"\
                            id="key"\
                            name="key"\
                            class="login-input"\
                            maxlength="255"\
                            required="true"\
                            placeholder="Project key">\
                    </div>\
                    <div class="big-spacer-bottom">\
                        <label for="name" class="login-label">Project name</label><input type="text"\
                            id="name"\
                            name="name"\
                            class="login-input"\
                            maxlength="255"\
                            required="true"\
                            placeholder="Project name">\
                    </div>\
                    <div class="big-spacer-bottom">\
                        <label for="folder" class="login-label">Project folder</label><input type="text"\
                            id="folder"\
                            name="folder"\
                            class="login-input"\
                            maxlength="255"\
                            required="true"\
                            placeholder="Project folder">\
                    </div>\
                    <div class="big-spacer-bottom">\
                        <label for="author" class="login-label">Author</label><input type="text"\
                            id="author"\
                            name="author"\
                            class="login-input"\
                            maxlength="255"\
                            required="true"\
                            placeholder="Report author">\
                    </div>\
                    <div class="big-spacer-bottom">\
                        <label for="quality-gate" class="login-label">Project quality gate</label><input type="text"\
                            id="quality-gate"\
                            name="quality-gate"\
                            class="login-input"\
                            maxlength="255"\
                            required="true"\
                            placeholder="Project quality gate">\
                    </div>\
                    <div class="big-spacer-bottom">\
                        <label for="quality-profile" class="login-label">Project quality profile</label><input type="text"\
                            id="quality-profile"\
                            name="quality-profile"\
                            class="login-input"\
                            maxlength="255"\
                            required="true"\
                            placeholder="Project quality profile">\
                    </div>\
                    <div class="big-spacer-bottom">\
                        <label for="spp" class="login-label">sonar-project.properties</label><textarea type="text"\
                            id="spp"\
                            name="spp"\
                            class="login-input"\
                            rows="25"\
                            required="true"\
                            ># Required metadata\nsonar.projectKey=genius-test\nsonar.projectName=GENIUS\nsonar.projectDescription=This an example based on GENIUS project.\nsonar.projectVersion=1.2\nsonar.language=java\n\n# Path to files\nsonar.sources=src/main/java\nsonar.tests=src/test/java\nsonar.java.binaries=target/classes\n\n# Encoding of the source files\nsonar.sourceEncoding=UTF-8\n\n# Coverage\n#sonar.genericcoverage.reportPaths=report/coverage.xml\n#sonar.genericcoverage.itReportPaths=report/itcoverage.xml\n#sonar.genericcoverage.unitTestReportPaths=report/unittest.xml\n</textarea>\
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
        var key = document.forms["analyze-form"]["key"].value;
        if (key == "") {
            alert("Key must be filled out.");
            return false;
        }
        var name = document.forms["analyze-form"]["name"].value;
        if (name == "") {
            alert("Name must be filled out.");
            return false;
        }
        var author = document.forms["analyze-form"]["author"].value;
        if (author == "") {
            alert("Author must be filled out.");
            return false;
        }
        var folder = document.forms["analyze-form"]["folder"].value;
        if (folder == "") {
            alert("Folder must be filled out.");
            return false;
        }
        var qgate = document.forms["analyze-form"]["quality-gate"].value;
        if (qgate == "") {
            alert("Quality gate must be filled out.");
            return false;
        }
        var qprofile = document.forms["analyze-form"]["quality-profile"].value;
        if (qprofile == "") {
            alert("Quality profile must be filled out.");
            return false;
        }
        return true;
    };

    /**
     * Log information in the bottom textarea
     * @param string Text to log
     */
    var log = function (string) {
        var logging = document.querySelector('#logging');
        logging.innerHTML = logging.innerHTML + "\n" + string;
    };

    /**
     * Set the quality gate and profile of the project
     * @param projectKey Key of the project
     * @param qualityGate value for the quality gate
     * @param qualityProfile value for the quality profile
     */
    var setQualityParams = function (projectKey, name, folder, qualityGate, qualityProfile, spp, author) {

            // Quality gate setting Basic YWRtaW46YWRtaW4=
            window.SonarRequest.getJSON(    // get the id from the name
                '/api/qualitygates/show?name='+qualityGate
            ).then(function (response) { // it exists
                window.SonarRequest.request(    // request the setting with the id
                    '/api/qualitygates/select'
                ).setHeader(
                    "Authorization", "Basic YWRtaW46YWRtaW4="
                ).setMethod(
                    "POST"
                ).setData(
                    { projectKey: projectKey, gateId: response.id }
                ).submit().then(function (response) {
                    log("[INFO] Quality gate selection response: " + response.status);
                }).catch(function (error) {
                    log("[WARNING] There were a problem during quality gate's setting.");
                });
            }).catch(function (error) {
                log("[WARNING] The quality gate does not exist.");
            }).then(function () {


                // Quality profile setting Basic YWRtaW46YWRtaW4=
                window.SonarRequest.getJSON(    // get the id from the name
                    '/api/qualityprofiles/search?profileName='+qualityProfile
                ).then(function (response) { // it exists
                    if(response.profiles != undefined && response.profiles.length >= 1) {
                        var filteredProfiles = response.profiles.filter(function (profile) {
                            return profile.name == qualityProfile;
                        });
                        if(filteredProfiles.length >= 1) {
                            window.SonarRequest.request(    // request the setting with the id
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
                            log("[WARNING] The quality profile does not exists.");
                        }

                    } else {
                        log("[WARNING] The quality profile does not exists.");
                    }
                }).catch(function (error) {
                    log("[WARNING] The quality profile does not exist.");
                });

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
        window.SonarRequest.postJSON(
            '/api/cnes/analyze',
            { key: key, name: name, folder: folder, qualitygate: qualitygate, qualityprofile: qualityprofile, sonarProjectProperties: spp }
        ).then(function (response) {
            log("[INFO] Project analysis response: " + response.logs);
            produceReport(key, name, qualitygate, qualityprofile, author);
        }).catch(function (error) {
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
        window.SonarRequest.getJSON(
            '/api/cnes/report',
            { key: key, name: name, qualitygate: qualitygate, qualityprofile: qualityprofile, author: author }
        ).then(function (response) {
            log("[INFO] Project report generation response: " + response.logs);
            log("############################################################\n\tAnalysis finished with success!\n############################################################\n")
        }).catch(function (error) {
            log("[ERROR] Project report generation failed.");
        });
    };

    // once the request is done, and the page is still displayed (not closed already)
    if (isDisplayed) {

        // Add html template
        var template = document.createElement("template");
        template.innerHTML = htmlTemplate;
        options.el.appendChild(template);
        options.el.appendChild(document.importNode(template.content, true));

        // set analyze button action
        var analyzeButton = document.querySelector('#analyze');
        analyzeButton.onclick = function () {

            if(checkForm()) {   // validation of the form

                // Get form values
                var key = document.forms["analyze-form"]["key"].value;
                var name = document.forms["analyze-form"]["name"].value;
                var folder = document.forms["analyze-form"]["folder"].value;
                var qgate = document.forms["analyze-form"]["quality-gate"].value;
                var qprofile = document.forms["analyze-form"]["quality-profile"].value;
                var author = document.forms["analyze-form"]["author"].value;
                var spp = document.forms["analyze-form"]["spp"].value;

                window.SonarRequest.post(
                    '/api/projects/create',
                    { project: key, name: name }
                ).then(function (response) {
                    log("[INFO] Project creation response: " + response.status);
                    setQualityParams(key, name, folder, qgate, qprofile, spp, author);
                }).catch(function (error) {
                    log("[WARNING] This project was not created. Maybe, it was already created.");
                    setQualityParams(key, name, folder, qgate, qprofile, spp, author);
                });
            }
        }

    }

    // return a function, which is called when the page is being closed
    return function () {
        // we unset the `isDisplayed` flag to ignore to Web API calls finished after the page is closed
        isDisplayed = false;
    };
});