package uk.ac.warwick.cs126.util;

import java.lang.Math;

public class HaversineDistanceCalculator {

    private final static float R = 6372.8f;
    private final static float kilometresInAMile = 1.609344f;

    public static float inKilometres(float lat1, float lon1, float lat2, float lon2) {
        // TODO
        lat1 = (float) Math.toRadians((double) lat1);
        lat2 = (float) Math.toRadians((double) lat2);
        lon1 = (float) Math.toRadians((double) lon1);
        lon2 = (float) Math.toRadians((double) lon2);
        double d = Math.pow(Math.sin((lat2 - lat1) / 2), 2);
        d += Math.cos(lat1) * Math.cos(lat2)
             * Math.pow(Math.sin((lon2 - lon1) / 2), 2);
        d = 2 * Math.asin(Math.sqrt(d));
        d *= R;
        String[] num = Double.toString(d).split("\\.");
        float f = Float.parseFloat(num[0] + "." + num[1].substring(0, 1));
        return f;
    }

    public static float inMiles(float lat1, float lon1, float lat2, float lon2) {
        // TODO
        String[] num = Double.toString(inKilometres(lat1, lon1, lat2, lon2) 
                        * (1.0 /  kilometresInAMile)).split("\\.");
        float f = Float.parseFloat(num[0] + "." + num[1].substring(0, 1));
        return f;
    }

}