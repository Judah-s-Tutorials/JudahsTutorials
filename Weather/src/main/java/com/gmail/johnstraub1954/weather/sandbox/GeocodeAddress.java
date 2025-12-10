package com.gmail.johnstraub1954.weather.sandbox;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONArray;
import org.json.JSONObject;

public class GeocodeAddress {

    // Replace with your actual Google Maps Geocoding API key
    private static final String API_KEY = "YOUR_API_KEY";

    public static void main(String[] args) {
        String address = "1600 Amphitheatre Parkway, Mountain View, CA";
        try {
            double[] latLng = getCoordinates(address);
            if (latLng != null) {
                System.out.println("Latitude: " + latLng[0]);
                System.out.println("Longitude: " + latLng[1]);
            } else {
                System.out.println("Coordinates not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double[] getCoordinates(String address) throws Exception {
        // URL encode the address
        String encodedAddress = URLEncoder.encode(address, "UTF-8");
        // Build the request URL
        String urlString = "https://maps.googleapis.com/maps/api/geocode/json?address=" + encodedAddress + "&key=" + API_KEY;

        // Create a URL object
        URL url = new URL(urlString);
        // Open HTTP connection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // Read the response
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder jsonResults = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonResults.append(line);
        }
        reader.close();

        // Parse JSON response
        JSONObject jsonObject = new JSONObject(jsonResults.toString());
        JSONArray results = jsonObject.getJSONArray("results");
        if (results.length() > 0) {
            JSONObject location = results.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
            double lat = location.getDouble("lat");
            double lng = location.getDouble("lng");
            return new double[]{lat, lng};
        } else {
            return null;
        }
    }
}
