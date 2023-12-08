package dk.nykredit.pmp.tracker;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TestService {

    @Test
    public void serviceIsStale() {
        Service testService = new Service();
        testService.setName("testService");
        testService.setPmpRoot("1.1.1.1");

        try {
            Thread.sleep(100);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }

        assertTrue(testService.isStale(50));
    }

    @Test
    public void serviceIsRefreshed() {
        Service testService = new Service();
        testService.setName("testService");
        testService.setPmpRoot("1.1.1.1");

        try {
            Thread.sleep(100);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }

        testService.refresh();
        assertFalse(testService.isStale(50));
    }
}
