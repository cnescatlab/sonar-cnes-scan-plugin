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

function registerScan(options, token) {
    // constants' definition
    const analyzeFormId = "analyze-form";
    const sppPlaceholderName = "<<TO REPLACE>>";

    // let's create a flag telling if the page is still displayed
    isDisplayedAnalysis = true;

    /**
     * Log information in the bottom text area
     * @param string Text to log
     */
    let log = function (string) {
        // get the logging element
        let logging = document.querySelector('#logging');
        // append text to log
        logging.innerHTML = logging.innerHTML + "\n" + string;
        // scroll to bottom
        logging.scrollTop = logging.scrollHeight;
    };

    let displayLog = function (string, color) {
        $('#last_log').html("<pre style='color:" + color + "'>" + string + "</pre>");
    }

    /**
     * Log information in the bottom text area as info
     * @param string Text to log
     */
    let info = function (string) {
        log("[INFO] " + string)
        displayLog(string, "blue")
    };

    /**
     * Log information in the bottom text area as error
     * @param string Text to log
     */
    let error = function (string) {
        log("[ERROR] " + string)
        displayLog(string, "red")
    };

    /**
     * Verify that the permissions are good.
     * @param currentUser user saved in SonarQube context as the logged one
     * @returns {boolean} true if all is good
     */
    let checkPermissions = function (currentUser) {
        // get permissions of the current user
        let perm = currentUser.permissions.global;
        // check that scan, quality gate admin and provisioning rights are in the permission array
        return currentUser.isLoggedIn && perm.indexOf("gateadmin") !== -1 && perm.indexOf("scan") !== -1 && perm.indexOf("provisioning") !== -1;
    };

    /**
     * Verify that the fields are correct.
     * @returns {boolean} true if all is good
     */
    let checkForm = function () {
        // check the field key
        // get it
        let key = document.forms[analyzeFormId]["key"].value;
        // check if void
        if (key === "") {
            // log error
            error("Key must be filled out.");
            // abort the process
            return false;
        }
        // check the field name (project)
        // get it
        let name = document.forms[analyzeFormId]["name"].value;
        // check if void
        if (name === "") {
            error("Name must be filled out.");
            // abort the process
            return false;
        }
        // check the field folder (project)
        // get it
        let folder = document.forms[analyzeFormId]["folder"].value;
        // check if void
        if (folder === "") {
            error("Workspace must be filled out.");
            // abort the process
            return false;
        } else if (folder.indexOf('\\') !== -1) {
            error("Do not use backslashes (\\) in workspace, use slashes (/) instead.");
            // abort the process
            return false;
        }
        // check the field sources (project)
        // get it
        let sources = document.forms[analyzeFormId]["sources"].value;
        // check if void
        if (sources === "") {
            error("Sources must be filled out.");
            // abort the process
            return false;
        } else if (folder.indexOf('\\') !== -1) {
            error("Do not use backslashes (\\) in workspace, use slashes (/) instead.");
            // abort the process
            return false;
        }
        // check the field spp (sonar-project.properties)
        // get it
        let spp = document.forms[analyzeFormId]["spp"].value;
        // check if it contains <<TO REPLACE>>
        if (spp.indexOf(sppPlaceholderName) !== -1) {
            error("sonar-project.properties was not correctly filled out. Replace all '" + sppPlaceholderName + "' fields.");
            // abort the process
            return false;
            // check if the user try to set project key
        } else if (spp.indexOf("sonar.projectKey") !== -1) {
            error("Please do not use 'sonar.projectKey' property.");
            // abort the process
            return false;
            // check if the user try to set project name
        } else if (spp.indexOf("sonar.projectName") !== -1) {
            error("Please do not use 'sonar.projectName' property.");
            // abort the process
            return false;
            // check if the user try to set project sources
        } else if (spp.indexOf("sonar.sources") !== -1) {
            log("Please do not use 'sonar.sources' property.");
            // abort the process
            return false;
            // check if the user try to set project version
        } else if (spp.indexOf("sonar.projectVersion") !== -1) {
            error("Please do not use 'sonar.projectVersion' property.");
            // abort the process
            return false;
            // check if the user try to set project description
        } else if (spp.indexOf("sonar.projectDescription") !== -1) {
            error("Please do not use 'sonar.projectDescription' property.");
            // abort the process
            return false;
        }

        return true;
    };

    /**
     * Clear log information in text area
     */
    let clearLog = function () {
        // get the logging element
        let logging = document.querySelector('#logging');
        // set initial text to log
        logging.innerHTML = "## Logging console ##";
    };

    /**
     *  Lock or unlock the form
     *  @param isEnabled true to unlock, false to lock the form
     */
    let setEnabled = function (isEnabled) {
        // retrieve the form
        let form = document.getElementById(analyzeFormId);
        // get all the components of the form
        let elements = form.elements;
        // change all components readOnly field to (un)lock them
        for (let i = 0, len = elements.length; i < len; ++i) {
            elements[i].readOnly = !isEnabled;
            elements[i].disabled = !isEnabled;
        }

        if (isEnabled) {
            // hide loading when button are enabled
            $('#loading').hide();
        } else {
            // show loading otherwise
            $('#loading').show();
        }
    };

    /**
     * Generate the report
     * @param key
     * @param author
     */
    let produceReport = function (key, author, token) {
        // http GET request to the cnes web service
        info("Analysis successfully finished! The report will be downloaded soon. If it fails go to More => CNES Report to download report again.");
        // unlock form
        setEnabled(true);
        // generate report
        window.location = "/api/cnesreport/report?key=" + key + "&author=" + author + "&token=" + token + "&branch=main";
    };

    /**
     * Complete automatically the sonar-project.properties to add information
     * computable from form's fields
     * @param spp
     * @param key
     * @param name
     * @param qualityprofile
     * @param version
     * @param description
     * @param sources
     * @return Modified spp with automatic data
     */
    let completeSPP = function (spp, key, name, qualityprofile, version, description, token, sources, pylintrcfolder) {
        // complete the spp with projectKey and projectName
        spp = spp.concat("\nsonar.projectKey=" + key);
        spp = spp.concat("\nsonar.projectName=" + name);

        // complete the spp with version and description
        spp = spp.concat("\nsonar.projectVersion=" + version);
        spp = spp.concat("\nsonar.projectDescription=" + description);

        // complete the spp with sonar.token
        spp = spp.concat("\nsonar.token=" + token);

        // complete the spp with sources repository
        spp = spp.concat("\nsonar.sources=" + sources);

        // if a python quality profile is set and there are no pylintrc set
        if (qualityprofile.indexOf("cnes_python") !== -1 && spp.indexOf("sonar.python.pylint_config") === -1) {
            // sonar pylint configuration property
            let pylintrcSonar = "\nsonar.python.pylint_config=";
            // name of the configuration file to use
            let filename = "pylintrc_RNC_sonar_2017_D";
            // we append the appropriate one
            // check if there is a rated A or B profile and add the corresponding file
            if (qualityprofile.indexOf("cnes_python_a") !== -1 || qualityprofile.indexOf("cnes_python_b") != -1) {
                filename = "pylintrc_RNC_sonar_2017_A_B";
                spp = spp.concat(pylintrcSonar + pylintrcfolder + filename);
                info("Use of configuration file " + filename + " for Pylint.");
                // check if there is a rated C profile and add the corresponding file
            } else if (qualityprofile.indexOf("cnes_python_c") !== -1) {
                filename = "pylintrc_RNC_sonar_2017_C";
                spp = spp.concat(pylintrcSonar + pylintrcfolder + filename);
                info("Use of configuration file " + filename + " for Pylint.");
                // otherwise it is a D configuration to use
            } else {
                spp = spp.concat(pylintrcSonar + pylintrcfolder + filename);
                info("Use of configuration file " + filename + " for Pylint.");
            }
        }


        return spp;
    };


    /**
     * Run the analysis
     * @param key
     * @param name
     * @param folder
     * @param qualityprofile
     * @param spp
     * @param author
     * @param version
     * @param description
     * @param sources
     * @param callback
     */
    let runAnalysis = function (key, name, folder, qualityprofile, spp, author,
        version, description, token, sources, callback) {
        window.SonarRequest.getJSON(
            '/api/cnes/configuration'
        ).then(function (response) {
            let pylintrcfolder = response.pylintrc;
            // auto complete the sonar-project properties
            spp = completeSPP(spp, key, name, qualityprofile, version, description, token, sources, pylintrcfolder);

            // log the finally used spp
            info("Here comes the finally used sonar-project.properties:\n" + spp);
            info("The analysis is running, please wait.");

            // send post request to the cnes web service
            window.SonarRequest.postJSON(
                '/api/cnes/analyze',
                { key: key, name: name, folder: folder, sonarProjectProperties: spp }
            ).then(function (response) {
                // on success
                // log output
                info("Project analysis response: \n" + response.logs);
                // wait that sonarqube has finished to import the report to produce the report
                info("SonarQube is still importing the report, please wait.");
                waitSonarQube(key, author, token, callback);
            }).catch(function (response) {
                // log error
                error("Project analysis failed.");
                error(response);
                // unlock form
                setEnabled(true);
            });
        }).catch(function (response) {
            // log error
            error("Impossible to find pylintrc folder property.");
            error(response);
            // unlock form
            setEnabled(true);
        });
    };

    /**
     * Create a project and set the quality gate and profiles of the project
     * @param projectKey Key of the project
     * @param name
     * @param folder
     * @param qualityGate value for the quality gate
     * @param qualityProfile value for the quality profile
     * @param spp
     * @param author
     * @param version
     * @param description
     * @param sources
     * @param callback
     */
    let createProject = function (projectKey, name, folder, qualityGate, qualityProfile, spp, author,
        version, description, token, sources, callback) {
        info("Project creation started...");
        // Request to create a project
        window.SonarRequest.postJSON(
            '/api/projects/create',
            { project: projectKey, name: name }
        ).then(function (response) {
            // if success we call the next function (analysis)
            if (response.project.qualifier === "TRK") {
                info("Project successfully created.");
                callback(projectKey, name, folder, qualityGate, qualityProfile, spp, author, version, description, token, sources, runAnalysis);
            } else {
                // unlock form
                error("Project creation failed...");
                setEnabled(true);
            }
        }).catch(function (response) {
            // log error
            error(response);
            // unlock form
            setEnabled(true);
        });

    };

    /**
     * Set the quality gate and quality profile of the project
     * @param projectKey Key of the project
     * @param name 
     * @param folder 
     * @param qualityGate value for the quality gate
     * @param qualityProfile value for the quality profile
     * @param spp 
     * @param author 
     * @param version 
     * @param description 
     * @param sources 
     * @param callback 
     */
    let setupProject = function (projectKey, name, folder, qualityGate, qualityProfile, spp, author,
        version, description, token, sources, callback) {
        // check if the quality gate field is filled out
        qualityGate = qualityGate === "" ? "CNES" : qualityGate;
        // Request to set the quality gate
        window.SonarRequest.post(
            '/api/qualitygates/select',
            { projectKey: projectKey, gateName: qualityGate }
        ).then(function (response) {
            // if success we call the next function (analysis)
            info("Quality gate successfully set.");
            callback(projectKey, name, folder, qualityProfile, spp, author, version, description, token, sources, produceReport);
        }).catch(function (response) {
            // log error
            error(response);
            // unlock form
            setEnabled(true);
        });

        // Request to set the quality profile
        /*console.log("Setting quality profile...")
        window.SonarRequest.postJSON(
            '/api/qualityprofiles/add_project',
            { project: projectKey, qualityProfile: qualityProfile, language: "java" }
        ).then(function (response) {
            // if success we call the next function (analysis)
            console.log(response);
            if (response.ok) {
                info("Quality profile successfully set.");
                callback(projectKey, name, folder, qualityProfile, spp, author, version, description, sources, produceReport);
            } else {
                // unlock form
                error("Quality profile setting failed.")
                setEnabled(true);
            }
        }).catch(function (response) {
            // log error
            error(response);
            // unlock form
            setEnabled(true);
        });*/
    }

    /**
     * Wait for SonarQube to finish importing the report to run a callback
     * @param {string} key - The project key
     * @param {string} author - The author
     * @param {string} token - The token
     * @param {function} callback - The callback function to run once the report is ready
     */
    const waitSonarQube = async (key, author, token, callback) => {
        try {
            // Send GET request to the SonarQube web service for task information about the project
            const response = await window.SonarRequest.getJSON('/api/ce/component', { component: key });

            // Check if there are no queued tasks, indicating readiness to report
            if (response.queue.length === 0) {
                // Produce the report
                callback(key, author, token);
            } else {
                // Retry after 2 seconds
                setTimeout(() => waitSonarQube(key, author, token, callback), 2000);
            }
        } catch (error) {
            // Log the error
            console.error('Error fetching SonarQube task information:', error);
            // Unlock form or handle error appropriately
            setEnabled(true);
        }
    };


    /**
     * Function which groups element by key
     * @param array
     * @param key
     */
    let groupBy = function (array, key) {
        return array.reduce(function (rv, x) {
            (rv[x[key]] = rv[x[key]] || []).push(x);
            return rv;
        }, {});
    };

    /**
     *  Get quality gates from the server and fill out the combo box
     */
    let initQualityGateDropDownList = function () {
        window.SonarRequest.getJSON(
            '/api/qualitygates/list'
        ).then(function (response) {
            // on success
            // we put each quality gate in the list
            $.each(response.qualitygates, function (i, item) {
                // we create a new option for each quality gate
                // in the json response
                let option = $('<option>', {
                    value: item.name,
                    text: item.name
                });
                // we add it to the drop down list
                $('#quality-gate').append(option);
                // we select as a default value the default quality gate
                if (item.id === response.default) {
                    $('#quality-gate').val(item.name);
                }
            });
        }).catch(function (response) {
            // log error
            error(response);
        });
    };

    /**
     *  Get quality profiles from the server and fill out the combo box
     */
    let initQualityProfileDropDownList = function () {
        let id = '#quality-profile';

        window.SonarRequest.getJSON(
            '/api/qualityprofiles/search'
        ).then(function (response) {
            // on success

            // sort profiles by language
            let sorted = groupBy(response.profiles, 'languageName');

            // and create an option group for each language
            $.each(sorted, function (j, cat) {
                let category = $('<optgroup>', {
                    label: j
                });
                $(id).append(category);
                // we put each quality profiles in the list
                $.each(cat, function (i, item) {
                    // we create a new option for each quality profile
                    // in the json response
                    let option = $('<option>', {
                        value: item.key,
                        text: j + " - " + item.name
                    });
                    // we add it to the drop down list
                    category.append(option);
                    // we select as a default value the default quality profile
                    if (item.isDefault) {
                        //$(id).val(item.name);
                    }
                });
            });
        }).catch(function (response) {
            // log error
            error(response);
        });
    };

    /**
     *  Return a well formatted string for the profile argument of the web service
     *  @param options
     */
    let optionsToString = function (options) {
        let result = "";

        // we concatenate all profiles' name in a string
        // separated by ';'
        for (let i = 0; i < options.length; ++i) {
            // add a separator when necessary
            if (i > 0) {
                result = result + ';';
            }
            result = result + options[i].value;
        }

        return result.replace(new RegExp('\\+', "g"), '%2B');
    };

    // once the request is done, and the page is still displayed (not closed already)
    if (isDisplayedAnalysis) {

        // Add html template
        let template = document.createElement("div");
        template.setAttribute("id", "template");
        options.el.appendChild(template);

        // url of the template to load
        let urlTemplate = checkPermissions(options.currentUser) ? '../../static/cnesscan/templates/analysisForm.html' : '../../static/cnesscan/templates/denied.html';

        // add the form if user has permission otherwise the denied access page
        $('#template').load(urlTemplate, function () {
            // remove loading screen
            $('#cnesscanload').remove();

            // set analyze button action
            let analyzeButton = document.querySelector('#analyze');
            // set its action on click
            analyzeButton.onclick = function () {

                // clear logs
                clearLog();

                // hide loading
                $('#loading').hide();

                // validation of the form
                if (checkForm()) {

                    // Get form values
                    let key = document.forms[analyzeFormId]["key"].value;
                    let name = document.forms[analyzeFormId]["name"].value;
                    let folder = document.forms[analyzeFormId]["folder"].value;
                    let qgate = document.forms[analyzeFormId]["quality-gate"].value;
                    let qprofile = optionsToString(document.forms[analyzeFormId]["quality-profile"].selectedOptions);
                    let author = document.forms[analyzeFormId]["author"].value;
                    let spp = document.forms[analyzeFormId]["spp"].value;
                    let version = document.forms[analyzeFormId]["version"].value;
                    let description = document.forms[analyzeFormId]["description"].value;
                    let sources = document.forms[analyzeFormId]["sources"].value;

                    // lock the form
                    setEnabled(false);

                    // show loading
                    $('#loading').show();

                    // request the creation of the project
                    createProject(key, name, folder, qgate, qprofile, spp, author,
                        version, description, token, sources, setupProject);

                }
            };

            // get copy button in the DOM
            let copyButton = document.querySelector('#copy');
            // set copy button action
            copyButton.onclick = function () {
                // get logging text area
                let toCopy = document.getElementById('logging');
                // select the text area to copy it in the clipboard
                toCopy.select();
                document.execCommand('copy');
                return false;
            }

            // get copy button in the DOM
            let logButton = document.querySelector('#show_logs');
            // set copy button action
            logButton.onclick = function () {
                $('#logging, #copy, #show_logs').toggle();

                return false;
            }

            // fill out quality gate drop down list
            initQualityGateDropDownList();

            // fill out quality profiles drop down list
            initQualityProfileDropDownList();

        });
    }

}


function formatDate(date) {
    let d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;

    return [year, month, day].join('-');
}

// Function used to revoke the plugin token
function revokeToken(name) {
    return window.SonarRequest.post("/api/user_tokens/revoke", { "name": name });
}

// Function used to create the plugin token
function createToken(name) {
    const expireDate = formatDate(new Date(Date.now() + 24 * 60 * 60 * 1000));
    return window.SonarRequest.postJSON("/api/user_tokens/generate", { "name": name, "expirationDate": expireDate })
        .then(function (response) {
            // if success we call the next function (analysis)
            return response.token;
        });
}

function initiatePluginToken() {
    const name = "cnes-report";

    return revokeToken(name).then(() => {
        return createToken(name).then(tokenResponse => {
            return tokenResponse;
        });
    });
}

function waitjQueryScan(options, token) {
    // Check if jQuery is loaded
    if (window.jQuery == null || token == null) {
        setTimeout(() => waitjQueryScan(options, token), 100);
    } else {
        registerScan(options, token);
    }
}

window.registerExtension('cnesscan/analysis', function (options) {
    // Dynamically load jQuery if it's not already loaded
    if (window.jQuery == null) {
        let imported = document.createElement('script');
        imported.src = '/static/cnesscan/jquery.min.js';
        imported.onload = () => {
            initiatePluginToken().then(token => {
                waitjQueryScan(options, token);
            });
        };
        document.head.appendChild(imported);
    } else {
        initiatePluginToken().then(token => {
            waitjQueryScan(options, token);
        });
    }

    options.el.innerHTML = `
        <div id="cnesscanload" class="page-wrapper-simple">
            <div class="page-simple">
                <h1 class="maintenance-title text-center">cnes-scan is loading, please wait...</h1>
            </div>
        </div>`;
});

