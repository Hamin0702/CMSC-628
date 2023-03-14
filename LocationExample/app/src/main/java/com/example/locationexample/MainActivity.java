package com.example.locationexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {

    private EditText editTextLat, editTextLong;
    private Button buttonLocation;
    private LocationManager locationManager_;
    private double latitude_, longitude_;
    private Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextLat = (EditText) findViewById(R.id.editTextLatitude);
        editTextLong = (EditText) findViewById(R.id.editTextLongitude);

        buttonLocation = (Button) findViewById(R.id.buttonLocation);
        buttonLocation.setOnClickListener(this);
        locationManager_ = (LocationManager) getSystemService(LOCATION_SERVICE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLocation:
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager_.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager_.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // checls whtether in android manifest that it asked for permission, only if permissions where asked, it
        // moves on to the next step to get the location, this ensures thta tthe code doesnt crash letsgoooooo
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager_.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(@NonNull android.location.Location location) {
        latitude_ = location.getLatitude();
        longitude_ = location.getLongitude();
        Location myLocation = new Location(latitude_, longitude_);
        myHandler.post(myLocation);
    }


    private class Location implements Runnable{
        private double lat_;
        private double long_;
        public Location(double lat, double longi){
            lat_ = lat;
            long_ = longi;
        }

        @Override
        public void run() {
//            String latStr = new Double(lat_).toString();
//            String longStr = new Double(long_).toString();
//            editTextLat.setText(latStr);
//            editTextLong.setText(longStr);
            editTextLat.setText(Double.toString(lat_));
            editTextLong.setText(Double.toString(long_));
        }
    }
}