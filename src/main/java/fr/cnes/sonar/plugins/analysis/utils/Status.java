package fr.cnes.sonar.plugins.analysis.utils;

import static fr.cnes.sonar.plugins.analysis.utils.StringManager.CNES_LOG_SEPARATOR;
import static fr.cnes.sonar.plugins.analysis.utils.StringManager.string;

/**
 * Contain information about a terminated task
 * @author garconb
 */
public class Status {
    /**
     * Success describe if the task ended successfully (true) or not (false)
     */
    private boolean success;
    /**
     * This is a message you can associate to the status (errors or warnings)
     */
    private String message;

    /**
     * Complete constructor
     * @param success If a task finished successfully it is true
     * @param message Description of the status
     */
    public Status(boolean success, String message) {
        this.setSuccess(success);
        this.setMessage(message);
    }

    /**
     * Default constructor
     */
    public Status() {
        this(false, "");
    }

    /**
     * Merge a status with the current one
     * Logic AND is used for success and message is append
     * @param status Stattus to merge with this one
     */
    public void merge(Status status) {
        setSuccess(this.success && status.success);
        setMessage(this.message + (this.message.isEmpty()?"":string(CNES_LOG_SEPARATOR)) + status.message);
    }

    /**
     * Getter for success
     * @return boolean true if it is a success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Set success to true if it is a success
     * @param success boolean
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Getter for message
     * @return message as String
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter for message
     * @param message message as String
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
