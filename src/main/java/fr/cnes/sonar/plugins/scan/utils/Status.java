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

import static fr.cnes.sonar.plugins.scan.utils.StringManager.CNES_LOG_SEPARATOR;
import static fr.cnes.sonar.plugins.scan.utils.StringManager.string;

/**
 * Contain information about a terminated task
 * @author lequal
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

        // merge of the both messages
        String mergedMessage = this.mMessage;
        // if empty we do not add
        if(!mergedMessage.isEmpty()) {
            mergedMessage += string(CNES_LOG_SEPARATOR);
        }
        // add the second message
        mergedMessage += status.getMessage();

        setMessage(mergedMessage);
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
    public String getMessage() {
        return mMessage;
    }

    /**
     * Setter for mMessage
     * @param mMessage mMessage as String
     */
    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }
}
