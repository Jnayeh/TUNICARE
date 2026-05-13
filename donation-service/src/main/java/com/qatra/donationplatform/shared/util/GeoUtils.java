package com.qatra.donationplatform.shared.util;

public final class GeoUtils {

  private GeoUtils() {}

  public static double distanceKm(Double lat1, Double lon1, Double lat2, Double lon2) {
    if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) {
      return Double.MAX_VALUE;
    }
    final double r = 6371.0;
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);
    double a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2)
            + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return r * c;
  }
}
