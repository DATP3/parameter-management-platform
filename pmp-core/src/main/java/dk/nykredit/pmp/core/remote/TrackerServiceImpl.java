package dk.nykredit.pmp.core.remote;

import okhttp3.*;
import org.eclipse.jetty.util.ajax.JSON;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class TrackerServiceImpl implements TrackerService {

    private static final URL TRACKER_URL;
    private static final OkHttpClient http = new OkHttpClient();

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
}
