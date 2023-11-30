package dk.nykredit.pmp.core.remote;

import okhttp3.*;
import org.eclipse.jetty.util.ajax.JSON;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TrackerServiceImpl implements TrackerService {

    private static final URL TRACKER_URL;
    private static final OkHttpClient http = new OkHttpClient();
    private static final int HEARTBEAT_INTERVAL = 1000 * 60 * 15; // 15 minutes

    static {
        try {
            // TODO: Add the real URL here
            String urlStr = System.getProperty("dk.nykredit.pmp.core.trackerurl", "http://localhost:45754");
            TRACKER_URL = new URL(urlStr);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean announce(String pmpRoot, String serviceName, String environment) throws IOException {
        RequestBody body = RequestBody.create(JSON.toString(Map.of(
                "pmpRoot", pmpRoot,
                "name", serviceName,
                "permissions", new String[0]
        )), MediaType.get("application/json"));
        URL url;
        try {
            url = new URL(TRACKER_URL, "/register");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Request req = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("pmp-enviroment", environment)
                .build();

        try (Response res = http.newCall(req).execute()) {
            return res.isSuccessful();
        }
    }

    @Override
    public Timer startHeartbeat(String pmpRoot, String serviceName, String environment) throws IOException {

        Timer timer = new Timer(true);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    announce(pmpRoot, serviceName, environment);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, HEARTBEAT_INTERVAL, HEARTBEAT_INTERVAL);

        return null;
    }
}
