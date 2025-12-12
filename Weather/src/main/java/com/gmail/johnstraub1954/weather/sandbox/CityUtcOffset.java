package com.gmail.johnstraub1954.weather.sandbox;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CityUtcOffset {
    private static final String OSM_NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";

    public static void main(String[] args) {
//        if (args.length != 1) {
//            System.out.println("Usage: java CityUtcOffset <city name>");
//            System.exit(1);
//        }

        String cityName = "Paris, France";//args[0];
        try {
            double latitude = getLatitude(cityName);
            double longitude = getLongitude(cityName);
            
            if (latitude == 0.0 || longitude == 0.0) {
                System.out.println("City not found or invalid coordinates.");
                return;
            }
            
            String utcOffset = getUtcOffset(latitude, longitude);
            System.out.printf("UTC offset for %s: %s%n", cityName, utcOffset);
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static double getLatitude(String cityName) throws Exception {
        String query = URLEncoder.encode(cityName, StandardCharsets.UTF_8);
        String urlString = String.format("%s?q=%s&format=json&limit=1", OSM_NOMINATIM_URL, query);
        
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "CityUtcOffset/1.0");
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                // Parse first result's lat
                String latStr = extractJsonValue(line, "\"lat\":");
                return latStr != null ? Double.parseDouble(latStr) : 0.0;
            }
        }
        return 0.0;
    }

    private static double getLongitude(String cityName) throws Exception {
        String query = URLEncoder.encode(cityName, StandardCharsets.UTF_8);
        String urlString = String.format("%s?q=%s&format=json&limit=1", OSM_NOMINATIM_URL, query);
        
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "CityUtcOffset/1.0");
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                // Parse first result's lon
                String lonStr = extractJsonValue(line, "\"lon\":");
                return lonStr != null ? Double.parseDouble(lonStr) : 0.0;
            }
        }
        return 0.0;
    }

    private static String getUtcOffset(double lat, double lon) throws Exception {
        // Use TimeZoneDB API (free tier available) - replace YOUR_KEY with free API key
        // Sign up at https://timezonedb.com/register
        String apiKey = "VNS12YSM9CHM"; 
        String urlString = String.format(
            "https://api.timezonedb.com/v2.1/get-time-zone?key=%s&format=json&lat=%f&lng=%f",
            apiKey, lat, lon
        );
        
        System.out.println( urlString );
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "CityUtcOffset/1.0");
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            
            String json = response.toString();
            String gmtOffset = extractJsonValue(json, "\"gmtOffset\":");
            String zoneName = extractJsonValue(json, "\"zoneName\":");
            
            if (gmtOffset != null) {
                int minutes = Integer.parseInt(gmtOffset);
                int hours = minutes / 60;
                int offsetMinutes = minutes % 60;
                return String.format("UTC%+d:%02d (%s)", hours, offsetMinutes, zoneName);
            }
        }
        return "Unknown";
    }

    private static String extractJsonValue(String json, String key) {
        int start = json.indexOf(key);
        if (start == -1) return null;
        
        start += key.length();
        // Skip whitespace and find opening quote or number
        while (start < json.length() && (json.charAt(start) == ' ' || json.charAt(start) == '\t')) {
            start++;
        }
        
        if (json.charAt(start) == '"') {
            // String value
            start++; // skip opening quote
            int end = json.indexOf('"', start);
            return end != -1 ? json.substring(start, end) : null;
        } else {
            // Number value
            int end = start;
            while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '-')) {
                end++;
            }
            return json.substring(start, end);
        }
    }
}

