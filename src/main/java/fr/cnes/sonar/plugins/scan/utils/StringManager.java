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
package fr.cnes.sonar.plugins.scan.utils;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Centralized the management of strings
 *
 * @author lequal
 */
public final class StringManager {

    /**
     * Property for controller description
     */
    public static final String CNES_CTRL_DESCRIPTION = "cnes.ctrl.description";
    /**
     * Property for controller key
     */
    public static final String CNES_CTRL_KEY = "cnes.ctrl.key";
    /**
     * Property for action 1 (scan) key
     */
    public static final String ANALYZE_KEY = "cnes.action.analyze.key";
    /**
     * Property for action 1 (scan) description
     */
    public static final String ANALYZE_DESC = "cnes.action.analyze.desc";
    /**
     * Property for action 1 (scan) param 1 description
     */
    public static final String ANALYZE_KEY_DESC = "cnes.action.analyze.param.key.desc";
    /**
     * Property for action 1 (scan) param 2 description
     */
    public static final String ANALYZE_NAME_DESC = "cnes.action.analyze.param.name.desc";
    /**
     * Property for action 1 (scan) param 5 description
     */
    public static final String ANALYZE_FOLDER_DESC = "cnes.action.analyze.param.folder.desc";
    /**
     * Property for action 1 (scan) param 6 description
     */
    public static final String ANALYZE_SPP_DESC = "cnes.action.analyze.param.spp.desc";
    /**
     * Property for quality profiles separator
     */
    public static final String CNES_COMMAND_PROJECT_PROFILES_SEPARATOR =
            "cnes.command.project.profiles.separator";
    /**
     * Property for action 2 (reporting) param 1 name
     */
    public static final String CNES_ACTION_REPORT_PARAM_KEY_NAME =
            "cnes.action.report.param.key.name";
    /**
     * Define the name of the author parameter
     */
    public static final String CNES_ACTION_REPORT_PARAM_AUTHOR_NAME =
            "cnes.action.report.param.author.name";
    /**
     * Property for action 2 (reporting) response's field 1
     */
    public static final String REPORT_RESPONSE_LOG = "cnes.action.report.response.log";
    /**
     * Property for action 2 (reporting) param 1 description
     */
    public static final String REPORT_KEY_DESC = "cnes.action.report.param.key.desc";
    /**
     * Property for action 2 (reporting) param 3 description
     */
    public static final String REPORT_NAME_DESC = "cnes.action.report.param.name.desc";
    /**
     * Property for action 2 (reporting) param 4 description
     */
    public static final String REPORT_AUTHOR_DESC = "cnes.action.report.param.author.desc";
    /**
     * Property for action 2 (reporting) key
     */
    public static final String REPORT_KEY = "cnes.action.report.key";
    /**
     * Property for action 2 (reporting) description
     */
    public static final String REPORT_DESC = "cnes.action.report.desc";
    /**
     * Property name of the command pattern to report an scan
     */
    public static final String CNES_COMMAND_REPORT = "cnes.command.report";
    /**
     * Property name of SonarQube server url
     */
    public static final String SONAR_URL = "sonar.url";
    /**
     * Property name of output
     */
    public static final String CNES_REPORT_PATH = "cnes.reporter.path";
    /**
     * Property name of results' folder
     */
    public static final String CNES_REPORTS_FOLDER = "cnes.reports.folder";
    /**
     * Property for error message when it was impossible to create a directory
     */
    public static final String CNES_MKDIR_ERROR = "cnes.mkdir.error";
    /**
     * Property name of the date pattern
     */
    public static final String DATE_PATTERN = "date.pattern";
    /**
     * Property name of scan page key
     */
    public static final String CNES_PAGE_ANALYSIS_KEY = "cnes.page.analysis.key";
    /**
     * Property name of scan page name
     */
    public static final String CNES_PAGE_ANALYSIS_NAME = "cnes.page.analysis.name";
    /**
     * Property name of report page key
     */
    public static final String CNES_PAGE_REPORT_KEY = "cnes.page.report.key";
    /**
     * Property name of report page name
     */
    public static final String CNES_PAGE_REPORT_NAME = "cnes.page.report.name";
    /**
     * Property name of help page key
     */
    public static final String CNES_PAGE_HELP_KEY = "cnes.page.help.key";
    /**
     * Property name of help page name
     */
    public static final String CNES_PAGE_HELP_NAME = "cnes.page.help.name";
    /**
     * Path pattern for the sonar-project.properties file
     * arg1: shared folder
     * arg2: project folder
     */
    public static final String CNES_SPP_PATH = "cnes.spp.path";
    /**
     * Path pattern for the log file
     * arg1: shared folder
     * arg2: date
     * arg3: project name
     */
    public static final String CNES_LOG_PATH = "cnes.log.path";
    /**
     * Command pattern to run an scan
     */
    public static final String CNES_COMMAND_SCAN = "cnes.command.scan";
    /**
     * Define the minimal version of sonarqube
     */
    public static final String SONAR_VERSION = "sonar.version";
    /**
     * Define the name of the projects's key parameter
     */
    public static final String ANALYZE_KEY_NAME = "cnes.action.analyze.param.key.name";
    /**
     * Define the name of the project's name parameter
     */
    public static final String ANALYZE_NAME_NAME = "cnes.action.analyze.param.name.name";
    /**
     * Define the name of the projects's folder's name parameter
     */
    public static final String ANALYZE_FOLDER_NAME = "cnes.action.analyze.param.folder.name";
    /**
     * Define the name of the projects's sonar-project.properties parameter
     */
    public static final String ANALYZE_SPP_NAME = "cnes.action.analyze.param.spp.name";
    /**
     * Define the name of the returned log filed
     */
    public static final String ANALYZE_RESPONSE_LOG = "cnes.action.analyze.response.log";

    /**
     * Template to use
     */
    public static final String CNES_REPORTER_TEMPLATE = "cnes.reporter.template";
    /**
     * Template to use for xlsx
     */
    public static final String CNES_ISSUES_TEMPLATE = "cnes.issues.template";
    /**
     * Property for the action's 3 key (project creation)
     */
    public static final String PROJECT_KEY = "cnes.action.project.key";
    /**
     * Property for the action's 3 description (project creation)
     */
    public static final String PROJECT_DESC = "cnes.action.project.desc";
    /**
     * Property for the action 3 (project creation) parameter 1 name (project key)
     */
    public static final String PROJECT_PARAM_KEY_NAME = "cnes.action.project.param.key.name";
    /**
     * Property for the action 3 (project creation) parameter 1 description (project key)
     */
    public static final String PROJECT_PARAM_KEY_DESC = "cnes.action.project.param.key.desc";
    /**
     * Property for the action 3 (project creation) parameter 2 name (project name)
     */
    public static final String PROJECT_PARAM_NAME_NAME = "cnes.action.project.param.name.name";
    /**
     * Property for the action 3 (project creation) parameter 2 description (project name)
     */
    public static final String PROJECT_PARAM_NAME_DESC = "cnes.action.project.param.name.desc";
    /**
     * Property for the action 3 (project creation) parameter 3 name (quality profiles)
     */
    public static final String PROJECT_PARAM_PROFILES_NAME =
            "cnes.action.project.param.profiles.name";
    /**
     * Property for the action 3 (project creation) parameter 3 description (quality profiles)
     */
    public static final String PROJECT_PARAM_PROFILES_DESC =
            "cnes.action.project.param.profiles.desc";
    /**
     * Property for the action 3 (project creation) parameter 4 name (quality gate)
     */
    public static final String PROJECT_PARAM_GATE_NAME = "cnes.action.project.param.gate.name";
    /**
     * Property for the action 3 (project creation) parameter 4 description (quality gate)
     */
    public static final String PROJECT_PARAM_GATE_DESC = "cnes.action.project.param.gate.desc";
    /**
     * Property for action 3 (project) response's field 1
     */
    public static final String PROJECT_REPONSE_LOG = "cnes.action.project.response.log";
    /**
     * Property for action 3 (project) response's field 2
     */
    public static final String PROJECT_REPONSE_STATUS = "cnes.action.project.response.status";
    /**
     * Separator between two log entries
     */
    public static final String CNES_LOG_SEPARATOR = "cnes.log.separator";
    /**
     * Property for the url of the list request for quality gates
     */
    public static final String CNES_REQUESTS_QUALITYGATES_LIST = "cnes.requests.qualitygates.list";
    /**
     * Default string to return when a key is unknown
     */
    public static final String DEFAULT_STRING = "unknown string";
    /**
     * Just a new line
     */
    public static final String NEW_LINE = "\n";
    /**
     * Key for the timeout property
     */
    public static final String TIMEOUT_PROP_DEF_KEY = "property.definition.timeout.key";
    /**
     * Key for the timeout property in API
     */
    public static final String TIMEOUT_PROP_DEF_API_KEY = "property.definition.timeout.api.key";
    /**
     * Name for the timeout property
     */
    public static final String TIMEOUT_PROP_DEF_NAME = "property.definition.timeout.name";
    /**
     * Description for the timeout property
     */
    public static final String TIMEOUT_PROP_DEF_DESC = "property.definition.timeout.desc";
    /**
     * Default value for the timeout property
     */
    public static final String TIMEOUT_PROP_DEF_DEFAULT = "property.definition.timeout.default";
    /**
     * Key for the workspace property
     */
    public static final String WORKSPACE_PROP_DEF_KEY = "property.definition.workspace.key";
    /**
     * Key for the workspace property in API
     */
    public static final String WORKSPACE_PROP_DEF_API_KEY = "property.definition.workspace.api.key";
    /**
     * Name for the workspace property
     */
    public static final String WORKSPACE_PROP_DEF_NAME = "property.definition.workspace.name";
    /**
     * Description for the workspace property
     */
    public static final String WORKSPACE_PROP_DEF_DESC = "property.definition.workspace.desc";
    /**
     * Default value for the workspace property
     */
    public static final String WORKSPACE_PROP_DEF_DEFAULT = "property.definition.workspace.default";
    /**
     * Key for the Sonarqube location property
     */
    public static final String HOME_PROP_DEF_KEY = "property.definition.home.key";
    /**
     * Key name for the Sonarqube location property in api
     */
    public static final String HOME_PROP_DEF_API_KEY = "property.definition.home.api.key";
    /**
     * Name for the timeout property
     */
    public static final String HOME_PROP_DEF_NAME = "property.definition.home.name";
    /**
     * Description for the timeout property
     */
    public static final String HOME_PROP_DEF_DESC = "property.definition.home.desc";
    /**
     * Default value for the timeout property
     */
    public static final String HOME_PROP_DEF_DEFAULT = "property.definition.home.default";
    /**
     * Key for the  scanner location property
     */
    public static final String SCANNER_PROP_DEF_KEY = "property.definition.scanner.key";
    /**
     * Key for the  scanner location property in API
     */
    public static final String SCANNER_PROP_DEF_API_KEY = "property.definition.scanner.api.key";
    /**
     * Name for the scanner property
     */
    public static final String SCANNER_PROP_DEF_NAME = "property.definition.scanner.name";
    /**
     * Description for the scanner property
     */
    public static final String SCANNER_PROP_DEF_DESC = "property.definition.scanner.desc";
    /**
     * Default value for the scanner property
     */
    public static final String SCANNER_PROP_DEF_DEFAULT = "property.definition.scanner.default";
    /**
     * Key for the report output destination property
     */
    public static final String REPORT_TEMPLATE_PROP_DEF_KEY = "property.definition.report.template.key";
    /**
     * Key for the report output destination property in API
     */
    public static final String REPORT_TEMPLATE_PROP_DEF_API_KEY = "property.definition.report.template.api.key";
    /**
     * Name for the report output destination property
     */
    public static final String REPORT_TEMPLATE_PROP_DEF_NAME = "property.definition.report.template.name";

    /**
     * Description for the report output destination property
     */
    public static final String REPORT_TEMPLATE_PROP_DEF_DESC = "property.definition.report.template.desc";
    /**
     * Default value for the report output destination property
     */
    public static final String REPORT_TEMPLATE_PROP_DEF_DEFAULT = "property.definition.report.template.default";
    /**
     * Key for the report output destination property
     */
    public static final String REPORT_OUTPUT_PROP_DEF_KEY = "property.definition.report.output.key";
    /**
     * Key for the report output destination property in API
     */
    public static final String REPORT_OUTPUT_PROP_DEF_API_KEY = "property.definition.report.output.api.key";
    /**
     * Name for the report output destination property
     */
    public static final String REPORT_OUTPUT_PROP_DEF_NAME = "property.definition.report.output.name";

    /**
     * Description for the report output destination property
     */
    public static final String REPORT_OUTPUT_PROP_DEF_DESC = "property.definition.report.output.desc";
    /**
     * Default value for the report output destination property
     */
    public static final String REPORT_OUTPUT_PROP_DEF_DEFAULT = "property.definition.report.output.default";
    /**
     * Key for the report output destination property
     */
    public static final String PYLINTRC_PROP_DEF_KEY = "property.definition.pylintrc.key";
    /**
     * Key for the report output destination property
     */
    public static final String PYLINTRC_PROP_DEF_API_KEY = "property.definition.pylintrc.api.key";
    /**
     * Name for the report output destination property
     */
    public static final String PYLINTRC_PROP_DEF_NAME = "property.definition.pylintrc.name";
    /**
     * Description for the report output destination property
     */
    public static final String PYLINTRC_PROP_DEF_DESC = "property.definition.pylintrc.desc";
    /**
     * Default value for the report output destination property
     */
    public static final String PYLINTRC_PROP_DEF_DEFAULT = "property.definition.pylintrc.default";

    /**
     * Logger of this class
     */
    private static final Logger LOGGER = Logger.getLogger(StringManager.class.getName());
    /**
     * Properties file for the current plugin
     */
    private static final String PLUGIN_PROPERTIES = "strings.properties";
    /**
     * Unique instance of this class (singleton)
     */
    private static StringManager ourInstance = null;
    /**
     * Gather all the properties concerning the plugin
     */
    private Properties properties = new Properties();

    /**
     * Private constructor to make a singleton of this class
     */
    private StringManager() {
        try {
            load();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Get the singleton
     *
     * @return unique instance of StringManager
     */
    public static synchronized StringManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new StringManager();
        }
        return ourInstance;
    }

    /**
     * Get the value of a property through its key
     *
     * @param key Key of the string to string
     * @return the property as String or the DEFAULT_STRING
     */
    public static String string(final String key) {
        return getInstance().getProperty(key, DEFAULT_STRING);
    }

    /**
     * Get the value of a property through its key
     *
     * @param key Key of the string to string
     * @param defaultString Default value to return
     * @return the property as String or the DEFAULT_STRING
     */
    private String getProperty(final String key, final String defaultString) {
        return this.properties.getProperty(key, defaultString);
    }

    /**
     * load properties from a specific file
     *
     * @throws IOException when an error occurred during file reading
     */
    private void load() throws IOException {
        // store properties
        this.properties = new Properties();

        final ClassLoader classLoader = StringManager.class.getClassLoader();

        // read the file
        // load properties file as a stream
        try (InputStream input = classLoader.getResourceAsStream(PLUGIN_PROPERTIES)) {
            if (input != null) {
                // load properties from the stream in an adapted structure
                this.properties.load(input);
            }
        }
    }
}
