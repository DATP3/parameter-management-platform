package dk.nykredit.pmp.core.remote;

public interface TrackerService {
    /**
     * Announces to the tracker that the service has started
     * @param url The url where the PMP API is available
     */
    void announce(String serviceUrl);
}
