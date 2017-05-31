window.registerExtension('cnes/reporting', function (options) {
    // let's create a flag telling if the page is still displayed
    var isDisplayedReporting = true;

    // contain the template of the page
    var htmlTemplate = '\
    <div id="bd" class="page-wrapper-simple">\
        <div id="nonav" class="page-simple" style="width:100%; margin-left: 10%; margin-right: 10%;">\
            <div>\
                <h1 class="maintenance-title text-center">Generate a report</h1>\
                <form id="generation-form"><!-- react-empty: 33 -->\
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
                        <div class="text-center overflow-hidden">\
                            <input id="generation" name="generation" type="button" value="Generate">\
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
        var key = document.forms["generation-form"]["key"].value;
        // check if void
        if (key === "") {
            // log error
            log("Key must be filled out.");
            // abort the process
            return false;
        }
        // check the field name (project)
        // get it
        var name = document.forms["generation-form"]["name"].value;
        // check if void
        if (name === "") {
            log("Name must be filled out.");
            // abort the process
            return false;
        }
        // check the field author (report)
        // get it
        var author = document.forms["generation-form"]["author"].value;
        // check if void
        if (author === "") {
            log("Author must be filled out.");
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
            log("############################################################\n\tGeneration of the report terminated!\n############################################################\n")
        }).catch(function (error) {
            // log error
            log("[ERROR] Project report generation failed.");
        });
    };

    // once the request is done, and the page is still displayed (not closed already)
    if (isDisplayedReporting) {

        // Add html template
        var template = document.createElement("template");
        template.innerHTML = htmlTemplate;
        options.el.appendChild(template);
        options.el.appendChild(document.importNode(template.content, true));

        // set generation button action
        var generation = document.querySelector('#generation');
        // set its action on click
        generation.onclick = function () {

            // validation of the form
            if(checkForm()) {

                // Get form values
                var key = document.forms["generation-form"]["key"].value;
                var name = document.forms["generation-form"]["name"].value;
                var qgate = document.forms["generation-form"]["quality-gate"].value;
                var qprofile = document.forms["generation-form"]["quality-profile"].value;
                var author = document.forms["generation-form"]["author"].value;

                // request the creation of the report
                produceReport(key, name, qgate, qprofile, author);
            }
        }

    }

    // return a function, which is called when the page is being closed
    return function () {
        // we unset the `isDisplayedReporting` flag to ignore to Web API calls finished after the page is closed
        isDisplayedReporting = false;
        options.el.textContent = '';
    };
});