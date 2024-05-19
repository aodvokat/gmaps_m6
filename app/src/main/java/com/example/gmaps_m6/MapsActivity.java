package com.example.gmaps_m6;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.example.gmaps_m6.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Button currentLocationButton;
    private static final int REQUEST_CODE = 101; // Variable to determine the location permission request code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Getting the SupportMapFragment and notifying when the map is ready to be used
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize the current location button
        currentLocationButton = findViewById(R.id.currentLocationButton);
        currentLocationButton.setOnClickListener(v -> getCurrentLocation());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng place = new LatLng(-6.975882418579812, 108.47741382095178);
        float zoomLevel = 17.0f;

        // Set map type to satellite
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, zoomLevel));

        // Add a marker at the specified location
        mMap.addMarker(new MarkerOptions().position(place)
                .title("Universitas Kuningan Kampus 2")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin4)));
    }

    private void getCurrentLocation() {
        // Getting the current location
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If location permission is not granted, request permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        // Get the last known location and display it on the map
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Get latitude and longitude
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    // Display marker on the map
                    LatLng currentLocation = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(currentLocation).title("I'm here"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17.0f));
                } else {
                    // Location is not available
                    Toast.makeText(MapsActivity.this, "Location not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
