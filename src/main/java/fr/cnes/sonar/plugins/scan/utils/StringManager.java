package fr.cnes.sonar.plugins.scan.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Centralized the management of strings
 *
 * @author begarco
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
     * Property for action 1 (scan) param 3 description
     */
    public static final String ANALYZE_QUALITYPROFILE_DESC = "cnes.action.analyze.param.qualityprofile.desc";
    /**
     * Property for action 1 (scan) param 4 description
     */
    public static final String ANALYZE_QUALITYGATE_DESC = "cnes.action.analyze.param.qualitygate.desc";
    /**
     * Property for action 1 (scan) param 5 description
     */
    public static final String ANALYZE_FOLDER_DESC = "cnes.action.analyze.param.folder.desc";
    /**
     * Property for action 1 (scan) param 6 description
     */
    public static final String ANALYZE_SPP_DESC = "cnes.action.analyze.param.spp.desc";
    /**
     * Property for action 2 (reporting) param 1 name
     */
    public static final String CNES_ACTION_REPORT_PARAM_KEY_NAME = "cnes.action.report.param.key.name";
    /**
     * Property for action 2 (reporting) param 2 name
     */
    public static final String CNES_ACTION_REPORT_PARAM_QUALITYGATE_NAME = "cnes.action.report.param.qualitygate.name";
    /**
     * Property for action 2 (reporting) param 3 name
     */
    public static final String CNES_ACTION_REPORT_PARAM_NAME_NAME = "cnes.action.report.param.name.name";
    /**
     * Define the name of the author parameter
     */
    public static final String CNES_ACTION_REPORT_PARAM_AUTHOR_NAME = "cnes.action.report.param.author.name";
    /**
     * Property for action 2 (reporting) response's field 1
     */
    public static final String REPORT_RESPONSE_LOG = "cnes.action.report.response.log";
    /**
     * Property for action 2 (reporting) param 1 description
     */
    public static final String REPORT_KEY_DESC = "cnes.action.report.param.key.desc";
    /**
     * Property for action 2 (reporting) param 2 description
     */
    public static final String REPORT_QUALITYGATE_DESC = "cnes.action.report.param.qualitygate.desc";
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
     * Property for quality profiles separator
     */
    public static final String CNES_COMMAND_PROJECT_PROFILES_SEPARATOR = "cnes.command.project.profiles.separator";
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
     * Define the name of the quality profile parameter
     */
    public static final String ANALYZE_QUALITYPROFILE_NAME = "cnes.action.analyze.param.qualityprofile.name";
    /**
     * Define the name of the quality gate parameter
     */
    public static final String ANALYZE_QUALITYGATE_NAME = "cnes.action.analyze.param.qualitygate.name";
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
    public static final String PROJECT_PARAM_PROFILES_NAME = "cnes.action.project.param.profiles.name";
    /**
     * Property for the action 3 (project creation) parameter 3 description (quality profiles)
     */
    public static final String PROJECT_PARAM_PROFILES_DESC = "cnes.action.project.param.profiles.desc";
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
    public static final String DEFAULT_STRING = "unknown string";
    /**
     * Key for the timeout property
     */
    public static final String TIMEOUT_PROP_DEF_KEY = "property.definition.timeout.key";
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
    public static String string(String key) {
        return getInstance().getProperty(key, DEFAULT_STRING);
    }

    /**
     * Get the value of a property through its key
     *
     * @param key Key of the string to string
     * @return the property as String or the DEFAULT_STRING
     */
    private String getProperty(String key, String defaultString) {
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
