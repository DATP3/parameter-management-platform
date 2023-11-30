package dk.nykredit.pmp.core.remote;

import java.io.IOException;
import java.util.Timer;

public interface TrackerService {
    /**
     * Announces to the tracker that the service has started
     *
     * @param pmpRoot     The root path of the PMP API
     * @param serviceName The name of the service
     * @param environment The name of the environment the service is running in
     *                                       TODO: Should this be an enum? Maybe it just comes from a file who knows
     * @return Whether the service successfully contacted the tracker
     */
    boolean announce(String pmpRoot, String serviceName, String environment) throws IOException;

    // TODO: Should this just be done when calling announce? Or maybe in the constructor?
    /**
     * Starts a heartbeat to the tracker
     *
     * @param pmpRoot     The root path of the PMP API
     * @param serviceName The name of the service
     * @param environment The name of the environment the service is running in
     * @return A timer that can be used to cancel the heartbeat
     */
    Timer startHeartbeat(String pmpRoot, String serviceName, String environment) throws IOException;
}
