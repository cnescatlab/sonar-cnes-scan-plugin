window.registerExtension('cnes/help', function (options) {
    // let's create a flag telling if the page is still displayed
    var isDisplayedHelp = true;

    // once the request is done, and the page is still displayed (not closed already)
    if (isDisplayedHelp) {

        // Add html template
        var template = document.createElement("div");
        template.setAttribute("id", "template");
        options.el.appendChild(template);
        // retrieve template in html file
        $('#template').load('../../static/cnes/templates/help.html');

    }

    // return a function, which is called when the page is being closed
    return function () {
        // we unset the `isDisplayedHelp` flag to ignore to Web API calls finished after the page is closed
        isDisplayedHelp = false;
        // clear elements of this page
        options.el.textContent = '';
    };
});