package fr.cnes.sonar.plugins.analysis.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Centralized the management of strings
 *
 * @author garconb
 */
public class StringManager {

    /**
     * Property for controller description
     */
    public static final String CNES_CTRL_DESCRIPTION = "cnes.ctrl.description";
    /**
     * Property for controller key
     */
    public static final String CNES_CTRL_KEY = "cnes.ctrl.key";
    /**
     * Property for action 1 (analysis) key
     */
    public static final String CNES_ACTION_1_KEY = "cnes.action.1.key";
    /**
     * Property for action 1 (analysis) description
     */
    public static final String CNES_ACTION_1_DESC = "cnes.action.1.desc";
    /**
     * Property for action 1 (analysis) param 1 description
     */
    public static final String CNES_ACTION_1_PARAM_1_DESC = "cnes.action.1.param.1.desc";
    /**
     * Property for action 1 (analysis) param 2 description
     */
    public static final String CNES_ACTION_1_PARAM_2_DESC = "cnes.action.1.param.2.desc";
    /**
     * Property for action 1 (analysis) param 3 description
     */
    public static final String CNES_ACTION_1_PARAM_3_DESC = "cnes.action.1.param.3.desc";
    /**
     * Property for action 1 (analysis) param 4 description
     */
    public static final String CNES_ACTION_1_PARAM_4_DESC = "cnes.action.1.param.4.desc";
    /**
     * Property for action 1 (analysis) param 5 description
     */
    public static final String CNES_ACTION_1_PARAM_5_DESC = "cnes.action.1.param.5.desc";
    /**
     * Property for action 1 (analysis) param 6 description
     */
    public static final String CNES_ACTION_1_PARAM_6_DESC = "cnes.action.1.param.6.desc";
    /**
     * Property for action 2 (reporting) param 1 name
     */
    public static final String CNES_ACTION_2_PARAM_1_NAME = "cnes.action.2.param.1.name";
    /**
     * Property for action 2 (reporting) param 2 name
     */
    public static final String CNES_ACTION_2_PARAM_2_NAME = "cnes.action.2.param.2.name";
    /**
     * Property for action 2 (reporting) param 3 name
     */
    public static final String CNES_ACTION_2_PARAM_3_NAME = "cnes.action.2.param.3.name";
    /**
     * Define the name of the author parameter
     */
    public static final String CNES_ACTION_2_PARAM_4_NAME = "cnes.action.2.param.4.name";
    /**
     * Property for action 2 (reporting) response's field 1
     */
    public static final String CNES_ACTION_2_FIELD_1 = "cnes.action.2.field.1";
    /**
     * Property for action 2 (reporting) param 1 description
     */
    public static final String CNES_ACTION_2_PARAM_1_DESC = "cnes.action.2.param.1.desc";
    /**
     * Property for action 2 (reporting) param 2 description
     */
    public static final String CNES_ACTION_2_PARAM_2_DESC = "cnes.action.2.param.2.desc";
    /**
     * Property for action 2 (reporting) param 3 description
     */
    public static final String CNES_ACTION_2_PARAM_3_DESC = "cnes.action.2.param.3.desc";
    /**
     * Property for action 2 (reporting) param 4 description
     */
    public static final String CNES_ACTION_2_PARAM_4_DESC = "cnes.action.2.param.4.desc";
    /**
     * Property for action 2 (reporting) key
     */
    public static final String CNES_ACTION_2_KEY = "cnes.action.2.key";
    /**
     * Property for action 2 (reporting) description
     */
    public static final String CNES_ACTION_2_DESC = "cnes.action.2.desc";
    /**
     * Property for quality profiles separator
     */
    public static final String CNES_COMMAND_PROJECT_PROFILES_SEPARATOR = "cnes.command.project.profiles.separator";
    /**
     * Property name of the command pattern to report an analysis
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
     * Property name of analysis page key
     */
    public static final String CNES_PAGE_ANALYSIS_KEY = "cnes.page.analysis.key";
    /**
     * Property name of analysis page name
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
     * Property for the path to the working directory
     */
    public static final String CNES_WORKSPACE = "cnes.workspace";
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
     * Command pattern to run an analysis
     */
    public static final String CNES_COMMAND_SCAN = "cnes.command.scan";
    /**
     * Define the minimal version of sonarqube
     */
    public static final String SONAR_VERSION = "sonar.version";
    /**
     * Define the name of the quality profile parameter
     */
    public static final String CNES_ACTION_1_PARAM_3_NAME = "cnes.action.1.param.3.name";
    /**
     * Define the name of the quality gate parameter
     */
    public static final String CNES_ACTION_1_PARAM_4_NAME = "cnes.action.1.param.4.name";
    /**
     * Define the name of the project's name parameter
     */
    public static final String CNES_ACTION_1_PARAM_2_NAME = "cnes.action.1.param.2.name";
    /**
     * Define the name of the projects's key parameter
     */
    public static final String CNES_ACTION_1_PARAM_1_NAME = "cnes.action.1.param.1.name";
    /**
     * Define the name of the returned log filed
     */
    public static final String CNES_ACTION_1_FIELD_1 = "cnes.action.1.field.1";
    /**
     * Define the name of the projects's folder's name parameter
     */
    public static final String CNES_ACTION_1_PARAM_5_NAME = "cnes.action.1.param.5.name";
    /**
     * Define the name of the projects's sonar-project.properties parameter
     */
    public static final String CNES_ACTION_1_PARAM_6_NAME = "cnes.action.1.param.6.name";
    /**
     * Path where the report must be exported
     */
    public static final String CNES_REPORTER_OUTPUT = "cnes.reporter.output";
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
    public static final String CNES_ACTION_3_KEY = "cnes.action.3.key";
    /**
     * Property for the action's 3 description (project creation)
     */
    public static final String CNES_ACTION_3_DESC = "cnes.action.3.desc";
    /**
     * Property for the action 3 (project creation) parameter 1 name (project key)
     */
    public static final String CNES_ACTION_3_PARAM_1_NAME = "cnes.action.3.param.1.name";
    /**
     * Property for the action 3 (project creation) parameter 1 description (project key)
     */
    public static final String CNES_ACTION_3_PARAM_1_DESC = "cnes.action.3.param.1.desc";
    /**
     * Property for the action 3 (project creation) parameter 2 name (project name)
     */
    public static final String CNES_ACTION_3_PARAM_2_NAME = "cnes.action.3.param.2.name";
    /**
     * Property for the action 3 (project creation) parameter 2 description (project name)
     */
    public static final String CNES_ACTION_3_PARAM_2_DESC = "cnes.action.3.param.2.desc";
    /**
     * Property for the action 3 (project creation) parameter 3 name (quality profiles)
     */
    public static final String CNES_ACTION_3_PARAM_3_NAME = "cnes.action.3.param.3.name";
    /**
     * Property for the action 3 (project creation) parameter 3 description (quality profiles)
     */
    public static final String CNES_ACTION_3_PARAM_3_DESC = "cnes.action.3.param.3.desc";
    /**
     * Property for the action 3 (project creation) parameter 4 name (quality gate)
     */
    public static final String CNES_ACTION_3_PARAM_4_NAME = "cnes.action.3.param.4.name";
    /**
     * Property for the action 3 (project creation) parameter 4 description (quality gate)
     */
    public static final String CNES_ACTION_3_PARAM_4_DESC = "cnes.action.3.param.4.desc";
    /**
     * Property for action 3 (project) response's field 1
     */
    public static final String CNES_ACTION_3_FIELD_1 = "cnes.action.3.field.1";
    /**
     * Property for action 3 (project) response's field 2
     */
    public static final String CNES_ACTION_3_FIELD_2 = "cnes.action.3.field.2";
    /**
     * Separator between two log entries
     */
    public static final String CNES_LOG_SEPARATOR = "cnes.log.separator";
    /**
     * Property for the url of the list request for quality gates
     */
    public static final String CNES_REQUESTS_QUALITYGATES_LIST = "cnes.requests.qualitygates.list";
    /**
     * Logger of this class
     */
    private static final Logger LOGGER = Logger.getLogger(StringManager.class.getName());
    /**
     * Properties file for the current plugin
     */
    private static final String PLUGIN_PROPERTIES = "strings.properties";
    /**
     * Default string to return when a key is unknown
     */
    private static final String DEFAULT_STRING = "unknown string";
    /**
     * Unique instance of this class (singleton)
     */
    private static final StringManager ourInstance = new StringManager();
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
            for (StackTraceElement ste : e.getStackTrace()) {
                LOGGER.severe(ste.toString());
            }
        }
    }

    /**
     * Get the singleton
     *
     * @return unique instance of StringManager
     */
    public static StringManager getInstance() {
        return ourInstance;
    }

    /**
     * Get the value of a property through its key
     *
     * @param key Key of the string to string
     * @return the property as String or the DEFAULT_STRING
     */
    public static String string(String key) {
        return getInstance().properties.getProperty(key, DEFAULT_STRING);
    }

    /**
     * load properties from a specific file
     *
     * @throws IOException when an error occurred during file reading
     */
    private void load() throws IOException {
        // store properties
        this.properties = new Properties();

        // read the file
        // load properties file as a stream
        try (InputStream input = StringManager.class.getClassLoader().getResourceAsStream(PLUGIN_PROPERTIES)) {
            if (input != null) {
                // load properties from the stream in an adapted structure
                this.properties.load(input);
            }
        }
    }
}
