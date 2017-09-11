package fr.cnes.sonar.plugins.scan.tasks.project;

/**
 * Model of Quality Gate for json parsing
 * @author lequal
 */
public class QualityGate {

    /**
     * Id of the quality gate
     */
    private String id;

    /**
     * Id of the quality gate
     */
    private String name;

    /**
     * Default constructor
     */
    public QualityGate() {
        this.id = "";
        this.name = "";
    }

    /**
     * Get id of the quality gate
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Set the id
     * @param id id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get name of the quality gate
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name
     * @param name name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
