package fr.cnes.sonar.plugins.analysis.rules.python;

import com.google.common.base.Charsets;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;

/**
 * Define the additional rules needed by CNES for code analysis.
 * This file is largely inspired by the class PylintRuleRepository
 * from the SonarPython plugin.
 * @author garconb
 */
public class CnesPythonRulesDefinition implements RulesDefinition {

    /**
     * Name of the repository where we want to add the new rules
     */
    private static final String REPOSITORY_NAME = "Pylint";
    /**
     * Key of the repository where we want to add the new rules
     */
    private static final String REPOSITORY_KEY = REPOSITORY_NAME;

    /**
     * Path to the file containing cnes checker rules to import
     */
    private static final String RULES_FILE = "/fr/cnes/sonar/plugins/python/rules/cnes-checker-rules.xml";
    /**
     * Path to the file containing other rules to import
     */
    private static final String OTHER_RULES_FILE = "/fr/cnes/sonar/plugins/python/rules/additional-rules.xml";
    /**
     * Key of the language to extend with the new rules
     */
    private static final String PYTHON_KEY = "py";
    /**
     * Loader for python rules (come from Python Sonar Plugin)
     */
    private final RulesDefinitionXmlLoader xmlLoader;

    /**
     * Default constructor
     * @param xmlLoader loader for python rules (come from Python Sonar Plugin)
     */
    public CnesPythonRulesDefinition(RulesDefinitionXmlLoader xmlLoader) {
        this.xmlLoader = xmlLoader;
    }

    /**
     * Load CNES specific rules for the CNES Checker (Pylint plugin)
     * and may load more rules, for example, unknown Pylint rules
     * @param context The plugin context
     */
    @Override
    public void define(Context context) {
        // create a rules repository to contain new rules
        NewRepository repository = context
                .createRepository(REPOSITORY_KEY, PYTHON_KEY)
                .setName(REPOSITORY_NAME);
        // load rules from xml file with the pylint format
        xmlLoader.load(repository, getClass().getResourceAsStream(RULES_FILE), Charsets.UTF_8.name());
        xmlLoader.load(repository, getClass().getResourceAsStream(OTHER_RULES_FILE), Charsets.UTF_8.name());
        // save modifications
        repository.done();
    }
}
