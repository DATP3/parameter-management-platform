package dk.nykredit.pmp.tracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestTrackerService {

    Tracker tracker;

    /**
     * Make sure the tracker exists before each test
     */
    @BeforeEach
    public void trackerSetup() {
        tracker = Tracker.getTracker();
    }

    /**
     * Make sure the tracker is reset after each test.
     * clearEnvironments effectively resets the tracker, 
     * as services exist in environments
     */
    @AfterEach
    public void resetTracker() {
        tracker.clearEnvironments();
    }

    /**
     * Test to check if the tracker can register a service
     */
    @Test
    public void canServiceRegister() {

        // Setup service with name and address
        Service testService = new Service();
        testService.setName("testService");
        testService.setPmpRoot("1.1.1.1");

        // Register service to tracker
        tracker.registerService(testService, "pmp-environment");

        // testService should be registered to the tracker in the pmp-environment
        assertTrue(tracker.serviceIsRegistered("pmp-environment", testService));
    }

    /**
     * Test to check if the tracker can get a service from the services' address
     */
    @Test
    public void canGetServiceFromAddress() {

        // Setup service with name and address
        Service testService = new Service();
        testService.setName("testService");
        testService.setPmpRoot("1.1.1.1");

        // Register service to tracker
        tracker.registerService(testService, "pmp-environment");

        // Get service from address
        Service actualService = tracker.getServiceFromAddress("1.1.1.1", "pmp-environment");

        // testService and actualService should be the same
        assertEquals(testService, actualService);
    }

    /**
     * Test to check if the tracker can get a list of registered services,
     * and that the list contains the correct services
     */
    @Test
    public void canGetListOfServices() {
        // Setup services with name and address
        Service testService1 = new Service();
        testService1.setName("testService1");
        testService1.setPmpRoot("1.1.1.1");
        Service testService2 = new Service();
        testService2.setName("testService2");
        testService2.setPmpRoot("2.2.2.2");
        Service testService3 = new Service();
        testService3.setName("testService3");
        testService3.setPmpRoot("3.3.3.3");

        // Register services to tracker
        tracker.registerService(testService1, "pmp-environment");
        tracker.registerService(testService2, "pmp-environment");
        tracker.registerService(testService3, "pmp-environment");

        // Get list of services from tracker
        ArrayList<Service> actualServiceList = tracker.readServices("pmp-environment");

        // The list should contain all three services
        assertTrue(actualServiceList.size() == 3);
        assertTrue(actualServiceList.contains(testService1));
        assertTrue(actualServiceList.contains(testService2));
        assertTrue(actualServiceList.contains(testService3));
    }

    /**
     * Test to check if the tracker can get environments in which services are
     * registered
     */
    @Test
    public void canGetEnvironments() {
        // Setup services with name and address
        Service testService1 = new Service();
        testService1.setName("testService1");
        testService1.setPmpRoot("1.1.1.1");
        Service testService2 = new Service();
        testService2.setName("testService2");
        testService2.setPmpRoot("2.2.2.2");

        // Register services to tracker in different environments
        tracker.registerService(testService1, "production");
        tracker.registerService(testService2, "development");

        // Get list of environments from tracker
        ArrayList<String> actualEnvironments = tracker.getEnvironmentNames();

        // The list should contain both environments
        assertTrue(actualEnvironments.size() == 2);
        assertTrue(actualEnvironments.contains("production"));
        assertTrue(actualEnvironments.contains("development"));
    }
}
