window.registerExtension('cnesscan/reporting', function (options) {
    // let's create a flag telling if the page is still displayed
    var isDisplayedReporting = true;

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
        var form = document.getElementById("generation-form");
        // get all the components of the form
        var elements = form.elements;
        // change all components readOnly field to (un)lock them
        for (var i = 0, len = elements.length; i < len; i++) {
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
     * @param name
     * @param qualitygate
     * @param author
     */
    var produceReport = function (key, name, qualitygate, author) {
        // http GET request to the cnes web service
        window.SonarRequest.getJSON(
            '/api/cnes/report',
            { key: key, name: name, qualitygate: qualitygate, author: author }
        ).then(function (response) {
            // on success log generation
            log("[INFO] Project report generation response: \n" + response.logs);
            log("############################################################\n\tGeneration of the report terminated!\n############################################################\n");
            setEnabled(true);
        }).catch(function (error) {
            // log error
            log("[ERROR] Project report generation failed.");
            setEnabled(true);
        });
    };

    // once the request is done, and the page is still displayed (not closed already)
    if (isDisplayedReporting) {

        // Add html template
        var template = document.createElement("div");
        template.setAttribute("id", "template");
        options.el.appendChild(template);
        // retrieve template from html
        $('#template').load('../../static/cnesscan/templates/reportForm.html', function(){
            // set generation button action
            // set its action on click
            document.querySelector('#generation').onclick = function () {

                // clear logs
                clearLog();

                // hide loading
                $('#loading').hide();

                // validation of the form
                if(checkForm()) {

                    // Get form values
                    var key = document.forms["generation-form"]["key"].value;
                    var name = document.forms["generation-form"]["name"].value;
                    var qgate = document.forms["generation-form"]["quality-gate"].value;
                    var author = document.forms["generation-form"]["author"].value;

                    // lock the form
                    setEnabled(false);

                    // show loading
                    $('#loading').show();

                    // request the creation of the report
                    produceReport(key, name, qgate, author);
                }
            };
        });

    }

    // return a function, which is called when the page is being closed
    return function () {
        // we unset the `isDisplayedReporting` flag to ignore to Web API calls finished after the page is closed
        isDisplayedReporting = false;
        // clean elements of this page
        options.el.textContent = '';
    };
});