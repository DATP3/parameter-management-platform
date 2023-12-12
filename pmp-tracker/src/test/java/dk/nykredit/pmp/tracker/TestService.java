package dk.nykredit.pmp.tracker;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TestService {

    /**
     * Test to check if the service is stale.
     * 
     * @staleTimeLimit is the time before the service goes stale
     */
    @Test
    public void serviceIsStale() {
        // Instantiate service and set name and address
        Service testService = new Service();
        testService.setName("testService");
        testService.setPmpRoot("1.1.1.1");

        // Sleep for 100ms
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }

        // As 100ms has passed, the service should be stale
        assertTrue(testService.isStale(50));
    }

    /**
     * Test to check if the service is refreshed.
     * 
     * @staleTimeLimit is the time before the service goes stale
     */
    @Test
    public void serviceIsRefreshed() {
        // Instantiate service and set name and address
        Service testService = new Service();
        testService.setName("testService");
        testService.setPmpRoot("1.1.1.1");

        // Sleep for 100ms
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }

        // As 100ms has passed, the service should be stale
        assertTrue(testService.isStale(50));
        // As the service is refreshed, it should no longer be stale
        testService.refresh();
        assertFalse(testService.isStale(50));
    }
}
