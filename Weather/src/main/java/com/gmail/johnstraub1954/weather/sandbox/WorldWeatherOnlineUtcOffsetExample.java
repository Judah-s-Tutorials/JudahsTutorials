package com.gmail.johnstraub1954.weather.sandbox;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

public class WorldWeatherOnlineUtcOffsetExample {

    // Put your WorldWeatherOnline API key here
    private static final String API_KEY = "VNS12YSM9CHM";

    // Base URL for the Time Zone API (v2 free endpoint)
    private static final String BASE_URL = "http://api.worldweatheronline.com/premium/v1/tz.ashx";

    public static void main(String[] args) {
        // Example query: city name, ZIP, or "lat,lon" like "40.7128,-74.0060"
        String locationQuery = "London";

        try {
            double utcOffset = getUtcOffset(locationQuery);
            System.out.println("UTC offset for " + locationQuery + " = " + utcOffset + " hours");
        } catch (Exception e) {
            System.err.println("Error getting UTC offset: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static double getUtcOffset(String location) throws IOException, InterruptedException {
        String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8);
        String url = BASE_URL
                + "?key=" + API_KEY
                + "&q=" + encodedLocation
                + "&format=json";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("HTTP error: " + response.statusCode()
                    + " body: " + response.body());
        }

        // Example JSON structure:
        // {
        //   "data": {
        //     "time_zone": [
        //       {
        //         "localtime": "2010-11-09 09:05",
        //         "utcOffset": "-8.0"
        //       }
        //     ]
        //   }
        // }
        JSONObject root = new JSONObject(response.body());
        JSONObject data = root.getJSONObject("data");
        JSONArray timeZones = data.getJSONArray("time_zone");
        if (timeZones.isEmpty()) {
            throw new IOException("No time_zone data in response");
        }

        JSONObject tz = timeZones.getJSONObject(0);
        String offsetStr = tz.getString("utcOffset"); // e.g. "-8.0" or "5.50"
        return Double.parseDouble(offsetStr);
    }
}

