package com.example.homework2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, SensorEventListener {

    private GoogleMap myMap;
    private MapFragment mapFragment;
    private LocationManager locationManager;
    private Location lastLocation;
    private LatLng currentLocation;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private Handler handler;
    private Runnable runnable;

    private boolean mapready = false;
    private double acclx = 0, accly = 0, distx = 0, disty = 0;
    private double r_earth = 6371000.0;
    private float now = 0, before = 0, diff = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = (Sensor) sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        handler = new Handler();
        runnable = new Runnable(){

            @Override
            public void run() {
                if(mapready){
                    updateGPS();
                }
                handler.postDelayed(this,5000);
            }
        };
        handler.postDelayed(runnable, 5000);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapready = true;
        myMap = googleMap;

        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        myMap.setMyLocationEnabled(true);
        myMap.getUiSettings().setZoomControlsEnabled(true);

        updateGPS();
    }

    public void updateGPS() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        currentLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());

        // Set the map marker
        myMap.clear();
        MarkerOptions markerOptions = new MarkerOptions().position(currentLocation);
        myMap.addMarker(markerOptions);

        // Focus the camera on the marker
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 16);
        myMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            now = event.timestamp / 1000f;
            diff = now - before;

            // Get acceleration values
            acclx = event.values[0];
            accly = event.values[1];

            // Calculate the distance travelled
            distx = 0.5 * acclx * diff * diff;
            disty = 0.5 * accly * diff * diff;

            double lastLatitude = lastLocation.getLatitude();
            double lastLongitude = lastLocation.getLongitude();

            // Calculate the new Latitude and Longitude from the distance changed
            double newLatitude = lastLatitude + (disty / r_earth) * (180 / Math.PI);
            double newLongitude = lastLongitude + (distx / r_earth) * (180 / Math.PI) / Math.cos(lastLatitude * Math.PI / 180);
            LatLng newLocation = new LatLng(newLatitude, newLongitude);

            // Update the map with the new location
            myMap.clear();
            MarkerOptions markerOptions = new MarkerOptions().position(newLocation);
            myMap.addMarker(markerOptions);

            // Focus the camera on the marker
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(newLocation, 16);
            myMap.animateCamera(cameraUpdate);

            before = now;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}