package de.greenrobot.tutorial.maps2;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

/*
 * Maps/LBS Tutorial.
 * 
 * (c) Copyright Markus Junginger 2010.
 */
public class TutorialMapActivity extends MapActivity implements LocationListener {

    private MapView mapView;
    private LocationManager locationManager;
    private String provider;
    private MyLocationOverlay myLocationOverlay;
    private MarienplatzOverlay marienplatzOverlay;

    private class GeocoderTask extends AsyncTask<Void, Void, Void> {

        private GeoPoint geoPointMarienplatz;

        @Override
        protected Void doInBackground(Void... arg0) {
            Geocoder geocoder = new Geocoder(TutorialMapActivity.this);
            try {
                // String locationName="1600 Amphitheatre Parkway, Mountain View, CA";
                String locationName = "Marienplatz, München";
                List<Address> addresses = geocoder.getFromLocationName(locationName, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    Log.d("MyMaps", "Found " + address);
                    geoPointMarienplatz = convertToGeoPoint(address.getLatitude(), address.getLongitude());
                } else {
                    Log.w("MyMaps", "No address found");
                }

            } catch (IOException e) {
                Log.e("MyMaps", "Geocoder failed", e);
            }
            if (geoPointMarienplatz == null) {
                // Auf mindestens einem Emulator geht das nicht, daher hart-kodiert als Plan B
                geoPointMarienplatz = new GeoPoint(48137794, 11575941);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (geoPointMarienplatz != null) {
                marienplatzOverlay = new MarienplatzOverlay(TutorialMapActivity.this, geoPointMarienplatz);
                // TODO 3: MarienplatzOverlay den Overlays hinzufügen
                mapView.postInvalidate();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mapView = (MapView) findViewById(R.id.mapview);
        // TODO 1: Zoom Buttons aktivieren ("built in zoom controls")

        MapController mapController = mapView.getController();
        // TODO 1: Zoom Stufe auf 16 setzen

        myLocationOverlay = new MyLocationOverlay(this, mapView);
        // TODO 1: myLocationOverlay den Overlays hinzufügen

        myLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
                mapView.getController().animateTo(myLocationOverlay.getMyLocation());
            }
        });

        new GeocoderTask().execute((Void) null);

        initLocationProvider();
        
        mapView.getController().animateTo(new GeoPoint(48137794, 11575941));
    }

    private void initLocationProvider() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteriaCoarse = new Criteria();
        // TODO 2: Genauigkeit Criteria.ACCURACY_COARSE als Kriterium setzen

        Criteria criteriaFine = new Criteria();
        // TODO 2: Genauigkeit Criteria.ACCURACY_FINE als Kriterium setzen

        List<String> providers = locationManager.getProviders(true);
        Log.d("MyMaps", "Enabled providers: " + providers);

        String providerCoarse = "todo"; // TODO 2: über den locationManager den besten "coarse" Provider wählen (getBest...)
        String providerFine = "todo"; // TODO 2: über den locationManager den besten "fine" Provider wählen (getBest...)

        Log.d("MyMaps", "Coarse provider: " + providerCoarse + ", fine provider: " + providerFine);
        provider = providerCoarse != null ? providerCoarse : providerFine;
        if (provider == null) {
            Log.e("MyMaps", "No location provider");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (provider != null) {
         // TODO 2: über den locationManager "location updates" für ausgewählten provider anfordern 
        }
        // TODO 1: den Kompass von myLocationOverlay "enablen"
        // TODO 1: die "Location"-Bestimmung von myLocationOverlay "enablen"
        myLocationOverlay.enableMyLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // TODO 2: über den locationManager "location updates" entfernen
        // TODO 1: den Kompass von myLocationOverlay "disablen"
        // TODO 1: die "Location"-Bestimmung von myLocationOverlay "disablen"
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("MyMaps", "Got new location: " + location);
        GeoPoint point = null; // TODO 2: dummy
        // TODO 2: location zu point mit der Methode convertToGeoPoint umwandeln
        if (marienplatzOverlay != null) {
            marienplatzOverlay.setGeoPointCurrent(point);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO 3: mapView neu zeichnen (vergleiche onPostExecute des GeocoderTask)
                }
            });
        }
    }

    private GeoPoint convertToGeoPoint(double latitude, double longitude) {
        int latE6 = (int) (latitude * 1e6 + 0.5);
        int lonE6 = (int) (longitude * 1e6 + 0.5);
        return new GeoPoint(latE6, lonE6);
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}