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
    private boolean mSuccess;
    /**
     * This is a message you can associate to the status (errors or warnings)
     */
    private String mMessage;

    /**
     * Complete constructor
     * @param success If a task finished successfully it is true
     * @param message Description of the status
     */
    public Status(boolean success, String message) {
        this.mSuccess = success;
        this.mMessage = message;
    }

    /**
     * Default constructor
     */
    public Status() {
        this(false, "");
    }

    /**
     * Merge a status with the current one
     * Logic AND is used for mSuccess and mMessage is append
     * @param status Status to merge with this one
     */
    public void merge(Status status) {
        setSuccess(this.mSuccess && status.mSuccess);
        setmMessage(this.mMessage + (this.mMessage.isEmpty()?"":string(CNES_LOG_SEPARATOR)) + status.mMessage);
    }

    /**
     * Getter for mSuccess
     * @return boolean true if it is a mSuccess
     */
    public boolean isSuccess() {
        return mSuccess;
    }

    /**
     * Set success to true if it is a success
     * @param success boolean
     */
    public void setSuccess(boolean success) {
        this.mSuccess = success;
    }

    /**
     * Getter for mMessage
     * @return mMessage as String
     */
    public String getmMessage() {
        return mMessage;
    }

    /**
     * Setter for mMessage
     * @param mMessage mMessage as String
     */
    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }
}
