package org.wepush.open_tour.utils;


import org.wepush.open_tour.structures.Site;

/**
 * Created by antoniocoppola on 30/05/15.
 */
public class SphericalMercator {

    public static double getDistanceFromLatLonInKm(Site pos1, Site pos2) {
        return getDistanceFromLatLonInKm(
                pos1.latitude,
                pos1.longitude,
                pos2.latitude,
                pos2.longitude
        );
    }

    public static float getDistanceFromLatLonInKm(
            double lat1,
            double lon1,
            double lat2,
            double lon2
    ) {
        double R = 6371; // Radius of the earth in km
        double dLat = deg2rad(lat2 - lat1);  // deg2rad below
        double dLon = deg2rad(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(deg2rad(lat1)) *
                Math.cos(deg2rad(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return ((float)(R * c));
    }

    private static double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }
}