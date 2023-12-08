package dk.nykredit.pmp.tracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestTrackerService {

    Tracker tracker;

    @BeforeEach
    public void trackerSetup() {
        tracker = Tracker.getTracker();
    }

    @AfterEach
    public void resetTracker() {
        tracker.clearEnvironments();
    }

    @Test
    public void canServiceRegister() {

        Service testService = new Service();
        testService.setName("testService");
        testService.setPmpRoot("1.1.1.1");

        tracker.registerService(testService, "pmp-environment");

        assertTrue(tracker.serviceIsRegistered("pmp-environment", testService));
    }

    @Test
    public void canGetServiceFromAddress() {

        Service testService = new Service();
        testService.setName("testService");
        testService.setPmpRoot("1.1.1.1");

        tracker.registerService(testService, "pmp-environment");

        Service actualService = tracker.getServiceFromAddress("1.1.1.1", "pmp-environment");
        System.out.println("Service: " + actualService);

        assertEquals(testService, actualService);
    }

    @Test
    public void canGetListOfServices() {
        Service testService1 = new Service();
        testService1.setName("testService1");
        testService1.setPmpRoot("1.1.1.1");
        Service testService2 = new Service();
        testService2.setName("testService2");
        testService2.setPmpRoot("2.2.2.2");
        Service testService3 = new Service();
        testService3.setName("testService3");
        testService3.setPmpRoot("3.3.3.3");

        tracker.registerService(testService1, "pmp-environment");
        tracker.registerService(testService2, "pmp-environment");
        tracker.registerService(testService3, "pmp-environment");

        ArrayList<Service> actualServiceList = tracker.readServices("pmp-environment");
        System.out.println("List: " + actualServiceList);

        assertTrue(actualServiceList.size() == 3);
        assertTrue(actualServiceList.contains(testService1));
        assertTrue(actualServiceList.contains(testService2));
        assertTrue(actualServiceList.contains(testService3));
    }

    @Test
    public void canGetEnvironments() {
        Service testService1 = new Service();
        testService1.setName("testService1");
        testService1.setPmpRoot("1.1.1.1");
        Service testService2 = new Service();
        testService2.setName("testService2");
        testService2.setPmpRoot("2.2.2.2");

        tracker.registerService(testService1, "production");
        tracker.registerService(testService2, "development");

        ArrayList<String> actualEnvironments = tracker.getEnvironmentNames();
        System.out.println("Environments: " + actualEnvironments);

        assertTrue(actualEnvironments.size() == 2);
        assertTrue(actualEnvironments.contains("production"));
        assertTrue(actualEnvironments.contains("development"));
    }
}
