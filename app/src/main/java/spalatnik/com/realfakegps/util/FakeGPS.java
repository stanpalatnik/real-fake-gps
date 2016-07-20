package spalatnik.com.realfakegps.util;


import android.content.ContentResolver;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FakeGPS {

    private final Context context;
    private final ContentResolver contentResolver;
    private final LocationManager locationManager;
    private final static Random randomizer = new Random();
    private Location lastLocation;

    private ScheduledExecutorService executor;

    Runnable periodicTask = new Runnable() {
        public void run() {
            moveRandomly();
        }
    };

    private static final double DELTA_LAT = 0.0005;
    private static final double DELTA_LON = 0.0005;


    public FakeGPS(Context context, ContentResolver contentResolver) {
        this.context = context;
        this.contentResolver = contentResolver;
        this.locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
    }

    private int setMockLocationSettings(ContentResolver contentResolver) {
        int value = 1;
        try {
            value = Settings.Secure.getInt(contentResolver,
                    Settings.Secure.ALLOW_MOCK_LOCATION);
            Settings.Secure.putInt(contentResolver,
                    Settings.Secure.ALLOW_MOCK_LOCATION, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    private void restoreMockLocationSettings(int restore_value) {
        try {
            Settings.Secure.putInt(contentResolver,
                    Settings.Secure.ALLOW_MOCK_LOCATION, restore_value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fakeLocation(String latitude, String longitude) {
        Location location = LocationUtil.createLocation(Double.valueOf(latitude), Double.valueOf(longitude), randomizer.nextInt(360));
        lastLocation = location;
        setFakeLocation(location);
    }

    private void setFakeLocation(Location fake_location) {
        /* every time you mock location, you should use these code */
        int value = setMockLocationSettings(contentResolver);//toggle ALLOW_MOCK_LOCATION on
        try {
            //locationManager.addTestProvider("gps", true, true, false,
              //      false, true, true, true, 3, 1);
           // locationManager.setTestProviderEnabled("gps", true);
            locationManager.setTestProviderLocation("gps", fake_location);
           // locationManager.removeTestProvider("gps");
        } catch (SecurityException e) {
            e.printStackTrace();
        } finally {
            restoreMockLocationSettings(value);//toggle ALLOW_MOCK_LOCATION off
        }
    }

    private void moveRandomly() {
        double latitude = lastLocation.getLatitude() + scaleOffset(DELTA_LAT);
        double longitude = lastLocation.getLongitude() + scaleOffset(DELTA_LON);
        Location location = LocationUtil.createLocation(latitude, longitude, randomizer.nextInt(360));
        lastLocation = location;
        setFakeLocation(location);
    }

    public void enableRandom() {
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(periodicTask, 0, 10, TimeUnit.SECONDS);
    }

    public void disableRandom() {
        executor.shutdown();
    }

    private double scaleOffset(double value) {
        return (randomizer.nextDouble() - 0.5) * value;
    }
}
