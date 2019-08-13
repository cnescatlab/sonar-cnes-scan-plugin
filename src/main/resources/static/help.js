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
var isDisplayedHelp = false;
function registerHelp(options){
  // let's create a flag telling if the page is still displayed
  isDisplayedHelp = true;

  // once the request is done, and the page is still displayed (not closed already)
  if (isDisplayedHelp) {

      // Add html template
      var template = document.createElement("div");
      template.setAttribute("id", "template");
      options.el.innerHTML = ""
      options.el.appendChild(template);
      // retrieve template in html file
      $('#template').load('../../static/cnesscan/templates/help.html');

  }



}
window.registerExtension('cnesscan/help', function (options) {
  function waitjQueryHelp(options){
    // check if jquery is loaded...
    if(window.jQuery == null){
      setTimeout(waitjQueryHelp, 1000, options);
    }
    else{
      registerHelp(options)
    }
  }
  var imported = document.createElement('script');
  imported.src = '/static/cnesscan/jquery.min.js';
  if(window.jQuery==null)document.head.appendChild(imported);
  options.el.innerHTML = '<div id="cnesscanload" class="page-wrapper-simple"><div class="page-simple"><h1 class="maintenance-title text-center">cnes-scan is loading, please wait...</h1></div></div>'
  waitjQueryHelp(options)

  // return a function, which is called when the page is being closed
  return function () {
      // we unset the `isDisplayedHelp` flag to ignore to Web API calls finished after the page is closed
      isDisplayedHelp = false;
      // clear elements of this page
      options.el.textContent = '';
  };
});
