package fr.sonar.plugins.scan.tests;

import fr.cnes.sonar.plugins.scan.utils.Status;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Check Status class
 * @author begarco
 */
public class StatusTest {

    /**
     *  Simple text for testing messages
     */
    private static final String TEST_STRING = "This is a test string.";

    private Status status;
    private Status status2;

    /**
     * Executed each time before running a single test
     */
    @Before
    public void prepare() {
        status = new Status();
        status2 = new Status();
    }

    /**
     * Check the default value, must be a failure with empty message
     */
    @Test
    public void defaultStatusValuesTest() {
        assertEquals("", status.getMessage());
        assertEquals(false, status.isSuccess());
    }

    /**
     * Assert that setters and getters do not act on data
     */
    @Test
    public void simpleModificationsTest() {
        status.setMessage(TEST_STRING);
        status.setSuccess(true);
        assertEquals(TEST_STRING, status.getMessage());
        assertEquals(true, status.isSuccess());
    }

    /**
     * Assert that a merge of default status result in a
     * failure with an empty message
     */
    @Test
    public void defaultMergeTest() {
        status.merge(status2);

        assertEquals("", status.getMessage());
        assertEquals(false, status.isSuccess());
    }

    /**
     * Assert that merging only success is a success
     */
    @Test
    public void successMergeTest() {
        status.setSuccess(true);
        status2.setSuccess(true);
        status.setMessage("message 1");
        status2.setMessage("message 2");

        status.merge(status2);

        assertEquals("message 1\nmessage 2", status.getMessage());
        assertEquals(true, status.isSuccess());
    }

    /**
     * Assert that the merge of at means one failure and one is success is a failure.
     */
    @Test
    public void differentMergeTest() {
        status.setSuccess(true);
        status2.setSuccess(false);
        status.setMessage("message 1");

        status.merge(status2);

        assertEquals("message 1\n", status.getMessage());
        assertEquals(false, status.isSuccess());
    }

}
