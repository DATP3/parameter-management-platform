package dk.nykredit.pmp.core.remote;

import okhttp3.*;
import org.eclipse.jetty.util.ajax.JSON;

import dk.nykredit.pmp.core.util.SystemEnvKeys;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TrackerServiceImpl implements TrackerService {

    private static final int HEARTBEAT_INTERVAL = 1000 * 60 * 15; // 15 minutes.
    private static final URL TRACKER_URL;
    private static final String TRACKER_PATH = "/pmp-tracker/rest/services";

    private static final OkHttpClient http = new OkHttpClient();
    private static final Timer heartbeatTimer = new Timer(true);
    private static boolean heatbeatStarted = false;

    /**
     * Static initializer for the tracker URL
     * Get URL from environment variable or system property. Otherwise use default.
     */
    static {
        try {
            String urlStr = System.getenv(SystemEnvKeys.TRACKER_URL);
            if (urlStr == null)
                urlStr = System.getProperty("dk.nykredit.pmp.core.trackerurl", "http://localhost:8080");
            TRACKER_URL = new URL(urlStr);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void announce(String pmpRoot, String serviceName, String environment) throws IOException {
        // Create a request body with the service info
        RequestBody body = RequestBody.create(JSON.toString(Map.of(
                "pmpRoot", pmpRoot,
                "name", serviceName)), MediaType.get("application/json"));
        URL url;
        // Check that the URL is valid
        try {
            url = new URL(TRACKER_URL, TRACKER_PATH);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        // Create a request to the tracker
        Request req = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("pmp-environment", environment)
                .build();

        // Send the request to the tracker
        try (Response res = http.newCall(req).execute()) {

            // If the request was successful and the heartbeat hasn't started yet, start it.
            if (res.isSuccessful() && !heatbeatStarted) {
                startHeartbeat(pmpRoot, serviceName, environment);
            }
        } catch (IOException e) {
            // TODO: handle exception
            throw e;
        }
    }

    /**
     * Starts a timer that periodically announces the service to the tracker
     *
     * @param pmpRoot     The root path of the PMP API
     * @param serviceName The name of the service
     * @param environment The name of the environment the service is running in
     */
    private void startHeartbeat(String pmpRoot, String serviceName, String environment) throws IOException {
        heatbeatStarted = true;

        heartbeatTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    announce(pmpRoot, serviceName, environment);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }, HEARTBEAT_INTERVAL, HEARTBEAT_INTERVAL);
    }
}
