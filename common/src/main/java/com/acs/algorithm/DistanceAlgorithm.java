package com.acs.algorithm;

import com.acs.models.Location;

public class DistanceAlgorithm {

    /**
     * Earth's radius in meters
     */
    private static final Double R = 6372800.0D;

    /**
     * Calculates a great-circle distance (distance between two points on the surface of a sphere) using Haversine formula.
     * <p>
     * param Location location1 location of first point
     * param Location location2 location of second point
     *
     * @return Distance rounded to full meters
     * @see <a href="https://en.wikipedia.org/wiki/Haversine_formula">http://wikipedia.org</a>
     */
    public static Double distance(Location location1, Location location2) {
        Double longitude1 = location1.getLongitude();
        Double latitude1 = location1.getLatitude();

        Double longitude2 = location2.getLongitude();
        Double latitude2 = location2.getLatitude();

        Double deltaLat = Math.toRadians(latitude2 - latitude1);
        Double deltaLon = Math.toRadians(longitude2 - longitude1);

        latitude1 = Math.toRadians(latitude1);
        latitude2 = Math.toRadians(latitude2);

        Double a = Math.pow(Math.sin(deltaLat / 2), 2) + Math.pow(Math.sin(deltaLon / 2), 2) * Math.cos(latitude1) * Math.cos(latitude2);
        Double c = 2 * Math.asin(Math.sqrt(a)) * R;

        return c;
    }
}