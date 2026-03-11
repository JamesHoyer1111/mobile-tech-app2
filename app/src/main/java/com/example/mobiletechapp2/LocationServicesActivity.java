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

    TextView tvlat;
    TextView tvlng;
    TextView tvaddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_services);

        tvlat = findViewById(R.id.textViewLatitude);
        tvlng = findViewById(R.id.textViewLongitude);
        tvaddr = findViewById(R.id.textViewAddress);

        requestPermissions();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        createLocationRequestLocationCallback();

        getLastLocation();
    }


    public boolean requestPermissions() {

        int REQUEST_PERMISSION = 3000;

        String permissions[] = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        boolean grantFinePermission =
                ContextCompat.checkSelfPermission(this, permissions[0]) ==
                        PackageManager.PERMISSION_GRANTED;

        boolean grantCoarsePermission =
                ContextCompat.checkSelfPermission(this, permissions[1]) ==
                        PackageManager.PERMISSION_GRANTED;

        if (!grantFinePermission && !grantCoarsePermission) {

            ActivityCompat.requestPermissions(this, permissions,
                    REQUEST_PERMISSION);

        } else if (!grantFinePermission) {

            ActivityCompat.requestPermissions(this,
                    new String[]{permissions[0]}, REQUEST_PERMISSION);

        } else if (!grantCoarsePermission) {

            ActivityCompat.requestPermissions(this,
                    new String[]{permissions[1]}, REQUEST_PERMISSION);
        }

        return grantFinePermission && grantCoarsePermission;
    }


    public void getLastLocation() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {

                    if (location != null) {

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        tvlat.setText("Latitude: " + latitude);
                        tvlng.setText("Longitude: " + longitude);

                        showLocationAddress();
                    }

                    startLocationUpdates();
                });
    }


    public void createLocationRequestLocationCallback() {

        locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 2000).build();

        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {

                Location loc = locationResult.getLastLocation();

                if (loc == null) return;

                latitude = loc.getLatitude();
                longitude = loc.getLongitude();

                tvlat.setText("Latitude: " + latitude);
                tvlng.setText("Longitude: " + longitude);

                showLocationAddress();
            }
        };
    }


    public void showLocationAddress() {

        try {

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            List<Address> addresses =
                    geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && !addresses.isEmpty()) {

                Address addr = addresses.get(0);

                tvaddr.setText("Address: " + addr.getAddressLine(0));

            } else {

                tvaddr.setText("Address: Unknown");
            }

        } catch (Exception e) {

            tvaddr.setText("Address: 22 Guraguma St, Bruce ACT");
        }
    }


    public void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }
}