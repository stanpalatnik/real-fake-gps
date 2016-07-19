package spalatnik.com.realfakegps.util;


import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;

class LocationUtil {

    public static Location createLocation(double latitude, double longitude, int bearing) {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(Double.valueOf(latitude));
        location.setLongitude(Double.valueOf(longitude));
        location.setBearing(bearing);
        location.setAccuracy(1);
        location.setTime(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        }
        return location;
    }
}
