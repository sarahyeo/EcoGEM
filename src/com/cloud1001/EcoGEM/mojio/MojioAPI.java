package com.cloud1001.EcoGEM.mojio;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MojioAPI {
    private static final String MOJIO_API_BASE = "https://api.moj.io:443/v1/Trips?";

    public static StringBuilder getTrips() throws Exception {
        HttpURLConnection tripsConn = null;
        StringBuilder jsonResults = new StringBuilder();

        try {
            tripsConn = getHttpURLConnection(jsonResults, 10);
        } finally {
            if (tripsConn != null) {
                tripsConn.disconnect();
            }
        }

        return jsonResults;
    }

    private static HttpURLConnection getHttpURLConnection(StringBuilder jsonResults, int limit) throws IOException {
        String sb = MOJIO_API_BASE + "limit=" + limit + "&offset=0&sortBy=StartTime&desc=false&criteria=";

        URL url = new URL(sb);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStreamReader in = new InputStreamReader(conn.getInputStream());

        // Load the results into a StringBuilder
        int read;
        char[] buff = new char[1024];
        while ((read = in.read(buff)) != -1) {
            jsonResults.append(buff, 0, read);
        }

        return conn;

    }
}
