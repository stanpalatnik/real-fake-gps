package spalatnik.com.realfakegps.util;


import android.content.ContentResolver;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;

import java.util.Random;

public class FakeGPS {

    final Context context;
    final ContentResolver contentResolver;
    final LocationManager locationManager;
    private final static Random randomizer = new Random();
    final String provider;

    public FakeGPS(Context context, ContentResolver contentResolver) {
        this.context = context;
        this.contentResolver = contentResolver;
        this.locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy( Criteria.ACCURACY_FINE );
        provider = locationManager.getBestProvider( criteria, true );
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
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(Double.valueOf(latitude));
        location.setLongitude(Double.valueOf(longitude));
        location.setBearing(randomizer.nextInt(360));
        location.setAccuracy(1);
        location.setTime(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        }
        setFakeLocation(location);
    }

    private void setFakeLocation(Location fake_location) {
        /* every time you mock location, you should use these code */
        int value = setMockLocationSettings(contentResolver);//toggle ALLOW_MOCK_LOCATION on
        try {
            locationManager.setTestProviderLocation(provider, fake_location);
        } catch (SecurityException e) {
            e.printStackTrace();
        } finally {
            restoreMockLocationSettings(value);//toggle ALLOW_MOCK_LOCATION off
        }
    }
}
