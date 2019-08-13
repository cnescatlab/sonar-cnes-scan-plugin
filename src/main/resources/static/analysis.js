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

function registerScan(options){
  // constants' definition
  const analyzeFormId = "analyze-form";
  const sppPlaceholderName = "<<TO REPLACE>>";

  // let's create a flag telling if the page is still displayed
  isDisplayedAnalysis = true;

  /**
   * Log information in the bottom text area
   * @param string Text to log
   */
  var log = function (string) {
      // get the logging element
      var logging = document.querySelector('#logging');
      // append text to log
      logging.innerHTML = logging.innerHTML + "\n" + string;
      // scroll to bottom
      logging.scrollTop = logging.scrollHeight;
  };

  var displayLog = function (string,color){
      $('#last_log').html("<pre style='color:"+color+"'>"+string+"</pre>")
  }

  /**
   * Log information in the bottom text area as info
   * @param string Text to log
   */
  var info = function (string) {
      log("[INFO] "+string)
      displayLog(string,"blue")
  };

  /**
   * Log information in the bottom text area as error
   * @param string Text to log
   */
  var error = function (string) {
      log("[ERROR] "+string)
      displayLog(string,"red")
  };

  /**
   * Verify that the permissions are good.
   * @param currentUser user saved in SonarQube context as the logged one
   * @returns {boolean} true if all is good
   */
  var checkPermissions = function (currentUser) {
      // get permissions of the current user
      var perm = currentUser.permissions.global;
      // check that scan, quality gate admin and provisioning rights are in the permission array
      return currentUser.isLoggedIn && perm.indexOf("gateadmin")!==-1 && perm.indexOf("scan")!==-1 && perm.indexOf("provisioning")!==-1;
  };

  /**
   * Verify that the fields are correct.
   * @returns {boolean} true if all is good
   */
  var checkForm = function () {
      // check the field key
      // get it
      var key = document.forms[analyzeFormId]["key"].value;
      // check if void
      if (key === "") {
          // log error
          error("Key must be filled out.");
          // abort the process
          return false;
      }
      // check the field name (project)
      // get it
      var name = document.forms[analyzeFormId]["name"].value;
      // check if void
      if (name === "") {
          error("Name must be filled out.");
          // abort the process
          return false;
      }
      // check the field folder (project)
      // get it
      var folder = document.forms[analyzeFormId]["folder"].value;
      // check if void
      if (folder === "") {
          error("Workspace must be filled out.");
          // abort the process
          return false;
      } else if (folder.indexOf('\\')!==-1) {
          error("Do not use backslashes (\\) in workspace, use slashes (/) instead.");
          // abort the process
          return false;
      }
      // check the field sources (project)
      // get it
      var sources = document.forms[analyzeFormId]["sources"].value;
      // check if void
      if (sources === "") {
          error("Sources must be filled out.");
          // abort the process
          return false;
      } else if (folder.indexOf('\\')!==-1) {
          error("Do not use backslashes (\\) in workspace, use slashes (/) instead.");
          // abort the process
          return false;
      }
      // check the field spp (sonar-project.properties)
      // get it
      var spp = document.forms[analyzeFormId]["spp"].value;
      // check if it contains <<TO REPLACE>>
      if(spp.indexOf(sppPlaceholderName)!==-1) {
          error("sonar-project.properties was not correctly filled out. Replace all '"+sppPlaceholderName+"' fields.");
          // abort the process
          return false;
      // check if the user try to set project key
      } else if(spp.indexOf("sonar.projectKey")!==-1) {
          error("Please do not use 'sonar.projectKey' property.");
          // abort the process
          return false;
      // check if the user try to set project name
      } else if(spp.indexOf("sonar.projectName")!==-1) {
          error("Please do not use 'sonar.projectName' property.");
          // abort the process
          return false;
      // check if the user try to set project sources
      } else if(spp.indexOf("sonar.sources")!==-1) {
          log("Please do not use 'sonar.sources' property.");
          // abort the process
          return false;
      // check if the user try to set project version
      } else if(spp.indexOf("sonar.projectVersion")!==-1) {
          error("Please do not use 'sonar.projectVersion' property.");
          // abort the process
          return false;
      // check if the user try to set project description
      } else if(spp.indexOf("sonar.projectDescription")!==-1) {
          error("Please do not use 'sonar.projectDescription' property.");
          // abort the process
          return false;
      }

      return true;
  };

  /**
   * Clear log information in text area
   */
  var clearLog = function () {
      // get the logging element
      var logging = document.querySelector('#logging');
      // set initial text to log
      logging.innerHTML = "## Logging console ##";
  };

  /**
   *  Lock or unlock the form
   *  @param isEnabled true to unlock, false to lock the form
   */
  var setEnabled = function (isEnabled) {
      // retrieve the form
      var form = document.getElementById(analyzeFormId);
      // get all the components of the form
      var elements = form.elements;
      // change all components readOnly field to (un)lock them
      for (var i = 0, len = elements.length; i < len; ++i) {
          elements[i].readOnly = !isEnabled;
          elements[i].disabled = !isEnabled;
      }

      if(isEnabled) {
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
  var produceReport = function (key, author) {
      // http GET request to the cnes web service
      info("Analysis successfully finished! The report will be downloaded soon. If fail go to More => CNES Report to download report again.");
      // unlock form
      setEnabled(true);
      // generate report
      window.location = "/api/cnesreport/report?key="+key+"&author="+author+"&token=noauth"
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
  var createProject = function (  projectKey, name, folder, qualityGate, qualityProfile, spp, author,
                                  version, description, sources, callback) {

      // check if the quality gate field is filled out
      qualityGate = qualityGate === "" ? "CNES" : qualityGate;

      info("Project creation started...");

      // Request to create a project with quality parameters
      window.SonarRequest.getJSON(
          '/api/cnes/create_project?key='+projectKey+'&name='+name+'&gate='+qualityGate+'&profiles='+qualityProfile
      ).then(function (response) {
          // log response
          info('Project created successfully: ' + response.success);
          info('Project creation log:\n' + response.logs);
          // if success we call the next function (analysis)
          if(response.success) {
              info("Project successfully created.");
              callback(projectKey, name, folder, qualityProfile, spp, author,
                       version, description, sources, produceReport);
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
   * Wait that sonarqube has finished to import the report to run a callback
   * @param key
   * @param author
   * @param callback
   */
  var waitSonarQube = function(key, author, callback) {
      // send get request to the cnes web service
      // we ask for information about task about a project
      window.SonarRequest.getJSON(
          '/api/ce/component',
          { componentKey: key }
      ).then(function (response) {
          // on success

          // if there are no queued task
          // so it is ready to report
          if(response.queue.length === 0) {
              // produce the report
              callback(key, author);
          } else {
              // retry later (in 2 seconds)
              window.setTimeout(waitSonarQube(key, author, callback), 2000);
          }

      }).catch(function (response) {
          // log error
          error(response);
          // unlock form
          setEnabled(true);
      });
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
  var completeSPP = function (spp, key, name, qualityprofile, version, description, sources, pylintrcfolder) {
      // complete the spp with projectKey and projectName
      spp = spp.concat("\nsonar.projectKey="+key);
      spp = spp.concat("\nsonar.projectName="+name);

      // complete the spp with version and description
      spp = spp.concat("\nsonar.projectVersion="+version);
      spp = spp.concat("\nsonar.projectDescription="+description);

      // complete the spp with sources repository
      spp = spp.concat("\nsonar.sources="+sources);

      // if a python quality profile is set and there are no pylintrc set
      if(qualityprofile.indexOf("cnes_python")!==-1 && spp.indexOf("sonar.python.pylint_config")===-1) {
          // sonar pylint configuration property
          var pylintrcSonar = "\nsonar.python.pylint_config=";
          // name of the configuration file to use
          var filename = "pylintrc_RNC_sonar_2017_D";
          // we append the appropriate one
          // check if there is a rated A or B profile and add the corresponding file
          if(qualityprofile.indexOf("cnes_python_a") !== -1 || qualityprofile.indexOf("cnes_python_b") != -1) {
              filename = "pylintrc_RNC_sonar_2017_A_B";
              spp = spp.concat(pylintrcSonar+pylintrcfolder+filename);
              info("Use of configuration file "+filename+" for Pylint.");
          // check if there is a rated C profile and add the corresponding file
          } else if(qualityprofile.indexOf("cnes_python_c") !== -1) {
              filename = "pylintrc_RNC_sonar_2017_C";
              spp = spp.concat(pylintrcSonar+pylintrcfolder+filename);
              info("Use of configuration file "+filename+" for Pylint.");
          // otherwise it is a D configuration to use
          } else {
              spp = spp.concat(pylintrcSonar+pylintrcfolder+filename);
              info("Use of configuration file "+filename+" for Pylint.");
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
  var runAnalysis = function (key, name, folder, qualityprofile, spp, author,
                              version, description, sources, callback) {
      window.SonarRequest.getJSON(
          '/api/cnes/configuration'
      ).then(function(response){
          var pylintrcfolder = response.pylintrc;

          // auto complete the sonar-project properties
          spp = completeSPP(spp, key, name, qualityprofile, version, description, sources, pylintrcfolder);

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
              waitSonarQube(key, author, callback);
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
   * Function which groups element by key
   * @param array
   * @param key
   */
  var groupBy = function(array, key) {
      return array.reduce(function(rv, x) {
          (rv[x[key]] = rv[x[key]] || []).push(x);
          return rv;
      }, {});
  };

  /**
   *  Get quality gates from the server and fill out the combo box
   */
  var initQualityGateDropDownList = function() {
      window.SonarRequest.getJSON(
          '/api/qualitygates/list'
      ).then(function (response) {
          // on success
          // we put each quality gate in the list
          $.each(response.qualitygates, function (i, item) {
              // we create a new option for each quality gate
              // in the json response
              var option = $('<option>', {
                               value: item.name,
                               text : item.name
                           });
              // we add it to the drop down list
              $('#quality-gate').append(option);
              // we select as a default value the default quality gate
              if(item.id===response.default) {
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
  var initQualityProfileDropDownList = function() {
      var id = '#quality-profile';

      window.SonarRequest.getJSON(
          '/api/qualityprofiles/search'
      ).then(function (response) {
          // on success

          // sort profiles by language
          var sorted = groupBy(response.profiles, 'languageName');

          // and create an option group for each language
          $.each(sorted, function (j, cat) {
              var category = $('<optgroup>', {
                                  label: j
                              });
              $(id).append(category);
              // we put each quality profiles in the list
              $.each(cat, function (i, item) {
                  // we create a new option for each quality profile
                  // in the json response
                  var option = $('<option>', {
                                   value: item.key,
                                   text : item.name
                               });
                  // we add it to the drop down list
                  category.append(option);
                  // we select as a default value the default quality profile
                  if(item.isDefault) {
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
   *  Check the cnescxx status and call the callback function
   *  only if the plugin is up
   *  @param callback callback to call
   */
  var checkCnescxx = function(callback) {
      // we check if the cnescxx is available
      window.SonarRequest.getJSON(
          '/api/cnescxx/health'
      ).then(function (response) {
          // on success
          callback();
      }).catch(function (response) {
          // log error
          error(response);
          error("Plugin cnescxx is not installed.");
      });
  };

  /**
   *  Add a special part for c/c++ in the form.
   *  Work only if the cnescxx plugin is installed.
   */
  var initCxxForm = function() {
      var cxxPart = $("#cxx-part");
      var urlTemplate = '../../static/cnesscan/templates/cxx.html';
      // we check if the cnescxx is available
      // and we load its form if yes
      checkCnescxx(function() {
          cxxPart.load(urlTemplate, function () {});
      });
  };

  /**
   *  Return a well formatted string for the profile argument of the web service
   *  @param options
   */
  var optionsToString = function(options) {
      var result = "";

      // we concatenate all profiles' name in a string
      // separated by ';'
      for (var i = 0; i < options.length; ++i) {
          // add a separator when necessary
          if(i>0) {
              result = result + ';';
          }
          result = result + options[i].value;
      }

      return result.replace(new RegExp('\\+', "g"), '%2B');
  };

  // once the request is done, and the page is still displayed (not closed already)
  if (isDisplayedAnalysis) {


      // Add html template
      var template = document.createElement("div");
      template.setAttribute("id", "template");
      options.el.appendChild(template);

      // url of the template to load
      var urlTemplate = checkPermissions(options.currentUser) ? '../../static/cnesscan/templates/analysisForm.html' : '../../static/cnesscan/templates/denied.html';

      // add the form if user has permission otherwise the denied access page
      $('#template').load(urlTemplate, function () {
          // remove loading screen
          $('#cnesscanload').remove();

          // set analyze button action
          var analyzeButton = document.querySelector('#analyze');
          // set its action on click
          analyzeButton.onclick = function () {

              // clear logs
              clearLog();

              // hide loading
              $('#loading').hide();

              // validation of the form
              if (checkForm()) {

                  // Get form values
                  var key = document.forms[analyzeFormId]["key"].value;
                  var name = document.forms[analyzeFormId]["name"].value;
                  var folder = document.forms[analyzeFormId]["folder"].value;
                  var cxx = document.forms[analyzeFormId]["cxx-analysis"].checked;
                  var qgate = document.forms[analyzeFormId]["quality-gate"].value;
                  var qprofile = optionsToString(document.forms[analyzeFormId]["quality-profile"].selectedOptions);
                  var author = document.forms[analyzeFormId]["author"].value;
                  var spp = document.forms[analyzeFormId]["spp"].value;
                  var version = document.forms[analyzeFormId]["version"].value;
                  var description = document.forms[analyzeFormId]["description"].value;
                  var sources = document.forms[analyzeFormId]["sources"].value;

                  // lock the form
                  setEnabled(false);

                  // show loading
                  $('#loading').show();

                  // call cxx tools
                  if(cxx) {
                      info("Please wait during C++ tools are running...");
                      window.SonarRequest.getJSON(
                          '/api/cnescxx/scan',
                          { project: folder}
                      ).then(function (response) {
                          // on success
                          // request the creation of the project
                          info(response.log);
                          info("C++ tools' execution finished.");
                          createProject(key, name, folder, qgate, qprofile, spp, author,
                                      version, description, sources, runAnalysis);
                      }).catch(function (response) {
                          // log error
                          error(response);
                      });
                  } else {
                      // request the creation of the project
                      createProject(key, name, folder, qgate, qprofile, spp, author,
                                  version, description, sources, runAnalysis);
                  }
              }
          };

          // get copy button in the DOM
          var copyButton = document.querySelector('#copy');
          // set copy button action
          copyButton.onclick = function () {
              // get logging text area
              var toCopy = document.getElementById('logging');
              // select the text area to copy it in the clipboard
              toCopy.select();
              document.execCommand('copy');
              return false;
          }

          // get copy button in the DOM
          var logButton = document.querySelector('#show_logs');
          // set copy button action
          logButton.onclick = function () {
              $('#logging, #copy, #show_logs').toggle();

              return false;
          }

          // fill out quality gate drop down list
          initQualityGateDropDownList();

          // fill out quality profiles drop down list
          initQualityProfileDropDownList();

          // initialize the c/c++ part of the form if the ws correctly answers
          initCxxForm();

      });
  }

}

var isDisplayedAnalysis = false
function waitjQueryScan(options){
  // check if jquery is loaded...
  if(window.jQuery == null){
    setTimeout(waitjQueryScan, 100, options);
  }
  else{
    registerScan(options)
  }
}
window.registerExtension('cnesscan/analysis', function (options) {
    var imported = document.createElement('script');
    imported.src = '/static/cnesscan/jquery.min.js';
    if(window.jQuery==null)document.head.appendChild(imported);
    options.el.innerHTML = '<div id="cnesscanload" class="page-wrapper-simple"><div class="page-simple"><h1 class="maintenance-title text-center">cnes-scan is loading, please wait...</h1></div></div>'
    waitjQueryScan(options);


    // return a function, which is called when the page is being closed
    return function () {
        // we unset the `isDisplayedAnalysis` flag to ignore to Web API calls finished after the page is closed
        isDisplayedAnalysis = false;
        // clear elements of this page
        options.el.textContent = '';
    };
});
