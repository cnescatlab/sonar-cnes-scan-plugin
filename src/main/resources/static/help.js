window.registerExtension('cnes/help', function (options) {
    // let's create a flag telling if the page is still displayed
    var isDisplayedHelp = true;

    // contain the template of the page
    var htmlTemplate = '\
    <div id="bd" class="page-wrapper-simple">\
        <div id="nonav" class="page-simple" style="width:100%; margin-left: 10%; margin-right: 10%;">\
            <div>\
                <h1 class="maintenance-title text-center">CNES Plugin Help</h1>\
                <form id="generation-form">\
                    <div class="big-spacer-bottom">\
                        <p><a href="https://espace-collaboratif.cnes.fr/sites/DCT/LEQUAL/SONAR/Pages/vm-lequal-1.4-java-analysis.aspx">Effectuer une analyse (et générer le rapport)</p></a>\
                        <p><a href="https://espace-collaboratif.cnes.fr/sites/DCT/LEQUAL/SONAR/Pages/vm-lequal-1.4-reporting.aspx">Générer un rapport</a></p>\
                    </div>\
                </form>\
            </div>\
        </div>\
    </div>';

    // once the request is done, and the page is still displayed (not closed already)
    if (isDisplayedHelp) {

        // Add html template
        var template = document.createElement("template");
        template.innerHTML = htmlTemplate;
        options.el.appendChild(template);
        options.el.appendChild(document.importNode(template.content, true));

    }

    // return a function, which is called when the page is being closed
    return function () {
        // we unset the `isDisplayedHelp` flag to ignore to Web API calls finished after the page is closed
        isDisplayedHelp = false;
        // clear elements of this page
        options.el.textContent = '';
    };
});