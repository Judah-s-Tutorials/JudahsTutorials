package com.gmail.johnstraub1954.weather.sandbox;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class TimeZoneOffsetExample {

    // Replace with your actual TimeZoneDB API key
    private static final String API_KEY = "VNS12YSM9CHM";

    public static void main(String[] args) {
        // Example coordinates: New York City
        double latitude = 40.7128;
        double longitude = -74.0060;

        try {
            int offsetSeconds = getUtcOffsetSeconds(latitude, longitude);
            System.out.println("UTC offset (seconds): " + offsetSeconds);
            System.out.println("UTC offset (hours): " + (offsetSeconds / 3600.0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getUtcOffsetSeconds(double lat, double lng) throws Exception {
        String urlStr = String.format(
            "https://api.timezonedb.com/v2.1/get-time-zone?key=%s&format=json&by=position&lat=%f&lng=%f",
            API_KEY, lat, lng
        );

        System.out.println( urlStr );
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int status = conn.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("HTTP error code: " + status);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder responseBuilder = new StringBuilder();
        String line;

        while ((line = in.readLine()) != null) {
            responseBuilder.append(line);
        }
        System.out.println( responseBuilder );
        in.close();
        conn.disconnect();

        // Parse JSON response
        JSONObject json = new JSONObject(responseBuilder.toString());

        String statusText = json.getString("status");
        if (!"OK".equalsIgnoreCase(statusText)) {
            String message = json.optString("message", "Unknown error");
            throw new RuntimeException("API error: " + message);
        }

        // gmtOffset is the UTC offset in seconds
        int gmtOffset = json.getInt("gmtOffset");
        return gmtOffset;
    }
}
