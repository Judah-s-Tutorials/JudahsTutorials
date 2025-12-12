package com.gmail.johnstraub1954.weather.sandbox;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CityCoordinates {
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";

    public static void main(String[] args) {
//        if (args.length == 0) {
//            System.out.println("Usage: java CityCoordinates <city name>");
//            System.out.println("Example: java CityCoordinates \"Paris, France\"");
//            return;
//        }

        String city = "Paris, France";//String.join(" ", args);
        try {
            String lat = getLatitude(city);
            String lon = getLongitude(city);
            
            if (lat != null && lon != null) {
                System.out.printf("Coordinates for '%s': %.6f, %.6f%n", city, 
                                Double.parseDouble(lat), Double.parseDouble(lon));
            } else {
                System.out.println("City not found or coordinates unavailable.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static String getLatitude(String city) throws Exception {
        return extractCoordinate(getNominatimResponse(city), "lat");
    }

    public static String getLongitude(String city) throws Exception {
        return extractCoordinate(getNominatimResponse(city), "lon");
    }

    private static String getNominatimResponse(String city) throws Exception {
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
        String urlString = String.format("%s?q=%s&format=json&limit=1", NOMINATIM_URL, encodedCity);
        
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "CityCoordinatesApp/1.0");
        
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(conn.getInputStream()));
        
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        
        return response.toString();
    }

    private static String extractCoordinate(String jsonResponse, String coordType) {
        String key = "\"" + coordType + "\":";
        int start = jsonResponse.indexOf(key);
        if (start == -1) return null;
        
        start += key.length();
        int end = jsonResponse.indexOf(",", start);
        if (end == -1) end = jsonResponse.indexOf("}", start);
        
        return jsonResponse.substring(start + 1, end).replace("\"", "");
    }
}
