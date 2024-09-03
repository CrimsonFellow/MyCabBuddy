package com.travelcompany.bookingmanagement.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FareService {

    private static final Map<String, Map<String, Integer>> distanceMap = new HashMap<>();

    static {
        // Initialize the distance map with distances between cities
        distanceMap.put("New York", Map.of(
                "Boston", 346,
                "Washington, D.C.", 362,
                "Philadelphia", 150,
                "Baltimore", 315,
                "New York", 0
        ));
        distanceMap.put("Boston", Map.of(
                "New York", 346,
                "Washington, D.C.", 725,
                "Philadelphia", 436,
                "Baltimore", 616,
                "Boston", 0
        ));
        distanceMap.put("Washington, D.C.", Map.of(
                "New York", 362,
                "Boston", 725,
                "Philadelphia", 224,
                "Baltimore", 64,
                "Washington, D.C.", 0
        ));
        distanceMap.put("Philadelphia", Map.of(
                "New York", 150,
                "Boston", 436,
                "Washington, D.C.", 224,
                "Baltimore", 157,
                "Philadelphia", 0
        ));
        distanceMap.put("Baltimore", Map.of(
                "New York", 315,
                "Boston", 616,
                "Washington, D.C.", 64,
                "Philadelphia", 157,
                "Baltimore", 0
        ));
    }

    public int calculateDistance(String source, String destination) {
        if (distanceMap.containsKey(source) && distanceMap.get(source).containsKey(destination)) {
            return distanceMap.get(source).get(destination);
        } else {
            throw new IllegalArgumentException("Invalid city pair: " + source + " to " + destination);
        }
    }

    public double calculateFare(String source, String destination) {
        int distance = calculateDistance(source, destination);
        // Define a base fare rate per kilometer (e.g., $0.5 per km)
        double fareRate = 0.5;
        return distance * fareRate;
    }
}

