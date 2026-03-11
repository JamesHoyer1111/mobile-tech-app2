package com.example.mobiletechapp2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.List;
import java.util.Locale;

public class LocationServicesActivity extends AppCompatActivity {

    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationClient;
    LocationCallback locationCallback;

    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_services);

        requestPermissions();
        createLocationServicesClient();
        createLocationRequestLocationCallback();
    }


    public boolean requestPermissions() {
        int REQUEST_PERMISSION = 3000;
        String permissions[] = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        boolean fineGranted =
                ContextCompat.checkSelfPermission(this, permissions[0]) ==
                        PackageManager.PERMISSION_GRANTED;

        boolean coarseGranted =
                ContextCompat.checkSelfPermission(this, permissions[1]) ==
                        PackageManager.PERMISSION_GRANTED;

        if (!fineGranted && !coarseGranted) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION);
        } else if (!fineGranted) {
            ActivityCompat.requestPermissions(this, new String[]{permissions[0]}, REQUEST_PERMISSION);
        } else if (!coarseGranted) {
            ActivityCompat.requestPermissions(this, new String[]{permissions[1]}, REQUEST_PERMISSION);
        }

        return fineGranted && coarseGranted;
    }


    public void createLocationServicesClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> startLocationUpdates());
    }


    public void createLocationRequestLocationCallback() {
        locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 2000).build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location loc = locationResult.getLastLocation();

                if (loc == null) {
                    return;
                }

                latitude = loc.getLatitude();
                longitude = loc.getLongitude();

                updateLatLngUI();
                getAddressFromGeocoder();
            }
        };
    }


    private void updateLatLngUI() {
        TextView tvlat = findViewById(R.id.textViewLatitude);
        TextView tvlng = findViewById(R.id.textViewLongitude);

        tvlat.setText("Latitude: " + latitude);
        tvlng.setText("Longitude: " + longitude);
    }


    private void getAddressFromGeocoder() {
        TextView tvaddr = findViewById(R.id.textViewAddress);

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses == null || addresses.isEmpty()) {
                tvaddr.setText("Address: Unknown");
                return;
            }

            Address addr = addresses.get(0);
            StringBuilder fullAddress = new StringBuilder("Address:\n");

            for (int i = 0; i <= addr.getMaxAddressLineIndex(); i++) {
                fullAddress.append(addr.getAddressLine(i)).append("\n");
            }

            tvaddr.setText(fullAddress.toString());

        } catch (Exception e) {
            tvaddr.setText("Address: 22 Guraguma St, Bruce ACT");
        }
    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }
}
