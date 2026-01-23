package com.example.angkorriceapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapPickerActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private LatLng selectedLocation;
    private LatLng userLocation;
    private Button btnBackToCheckout;
    private Button btnConfirmLocation;
    private Button btnSearchLocation;
    private Button btnMyLocation;
    private EditText etSearchAddress;
    private LocationManager locationManager;
    private Geocoder geocoder;

    // Default location: Cambodia center (fallback)
    private static final LatLng CAMBODIA_CENTER = new LatLng(12.5657, 104.9910);
    private static final float ZOOM_LEVEL = 15f;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        
        getWindow().setStatusBarColor(getResources().getColor(R.color.gold, getTheme()));
        
        setContentView(R.layout.activity_map_picker);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeUI();
        requestLocationPermissions();
        setupMap();
    }

    private void initializeUI() {
        btnBackToCheckout = findViewById(R.id.btnBackToCheckout);
        btnConfirmLocation = findViewById(R.id.btnConfirmLocation);
        btnSearchLocation = findViewById(R.id.btnSearchLocation);
        btnMyLocation = findViewById(R.id.btnMyLocation);
        etSearchAddress = findViewById(R.id.etSearchAddress);
        
        geocoder = new Geocoder(this);
        
        btnBackToCheckout.setOnClickListener(v -> finish());
        
        // Search location button
        btnSearchLocation.setOnClickListener(v -> searchLocation());
        
        // My Location button - centers map on user's current location
        btnMyLocation.setOnClickListener(v -> centerMapOnUserLocation());
    }

    private void requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        } else {
            startLocationUpdates();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Permission denied. Using default location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startLocationUpdates() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                // Request GPS updates for accuracy
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        1000, // Update every 1 second
                        0,    // Update every 0 meters
                        this);
                
                // Also request NETWORK_PROVIDER for faster initial fix
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            1000,
                            0,
                            this);
                }
                
                // Try to get the last known location immediately from both providers
                Location lastGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location lastNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                
                // Use the most recent location
                Location bestLocation = lastGpsLocation;
                if (lastNetworkLocation != null && (bestLocation == null || 
                        lastNetworkLocation.getTime() > bestLocation.getTime())) {
                    bestLocation = lastNetworkLocation;
                }
                
                if (bestLocation != null) {
                    onLocationChanged(bestLocation);
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            // Only update selectedLocation if it's still at default Cambodia center
            if (selectedLocation == null || selectedLocation.equals(CAMBODIA_CENTER)) {
                selectedLocation = userLocation;
            }
            if (mMap != null) {
                // Update the map to show user's location
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, ZOOM_LEVEL));
                mMap.clear();
                addMarker(selectedLocation);
            }
        }
    }

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    private void searchLocation() {
        String address = etSearchAddress.getText().toString().trim();
        
        if (address.isEmpty()) {
            Toast.makeText(this, "Please enter an address or location name", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            
            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                selectedLocation = latLng;
                
                // Move map to the found location
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL));
                mMap.clear();
                addMarker(latLng);
                
                Toast.makeText(this, "Location found: " + location.getAddressLine(0), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Location not found. Try another address.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error searching location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;

            // Set map type to normal
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            
            // If we have user location, center on it; otherwise use default
            if (userLocation != null) {
                selectedLocation = userLocation;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, ZOOM_LEVEL));
                addMarker(userLocation);
            } else {
                // Will be centered on user location once GPS is available
                selectedLocation = CAMBODIA_CENTER;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CAMBODIA_CENTER, ZOOM_LEVEL));
            }

            // Handle map clicks to select location
            mMap.setOnMapClickListener(latLng -> {
                selectedLocation = latLng;
                mMap.clear();
                addMarker(latLng);
                Toast.makeText(MapPickerActivity.this, "Location selected", Toast.LENGTH_SHORT).show();
            });

            // Handle marker drag for precision
            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDrag(com.google.android.gms.maps.model.Marker marker) {}

                @Override
                public void onMarkerDragEnd(com.google.android.gms.maps.model.Marker marker) {
                    selectedLocation = marker.getPosition();
                }

                @Override
                public void onMarkerDragStart(com.google.android.gms.maps.model.Marker marker) {}
            });

            // Confirm location button
            btnConfirmLocation.setOnClickListener(v -> {
                if (selectedLocation != null) {
                    // Get full address using reverse geocoding
                    String address = getDetailedAddress(selectedLocation);
                    
                    Intent intent = new Intent();
                    intent.putExtra("address", address);
                    intent.putExtra("latitude", selectedLocation.latitude);
                    intent.putExtra("longitude", selectedLocation.longitude);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(MapPickerActivity.this, "Please select a location on the map", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error loading map: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void addMarker(LatLng location) {
        if (mMap != null) {
            mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title("Delivery Location")
                    .draggable(true));
        }
    }

    private String getDetailedAddress(LatLng location) {
        try {
            List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
            
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder addressText = new StringBuilder();
                
                // Build address from components
                if (address.getThoroughfare() != null) {
                    addressText.append(address.getThoroughfare()).append(" ");
                }
                if (address.getSubLocality() != null) {
                    addressText.append(address.getSubLocality()).append(", ");
                }
                if (address.getLocality() != null) {
                    addressText.append(address.getLocality()).append(", ");
                }
                if (address.getAdminArea() != null) {
                    addressText.append(address.getAdminArea()).append(", ");
                }
                if (address.getCountryName() != null) {
                    addressText.append(address.getCountryName());
                }
                
                if (address.getPostalCode() != null && !address.getPostalCode().isEmpty()) {
                    addressText.append(" ").append(address.getPostalCode());
                }
                
                String result = addressText.toString().trim();
                // If we got an empty result, return fallback
                if (result.isEmpty()) {
                    return String.format("Location: Lat %.4f, Lng %.4f, Cambodia", 
                            location.latitude, location.longitude);
                }
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Fallback to simple address
        return String.format("Location: Lat %.4f, Lng %.4f, Cambodia", 
                location.latitude, location.longitude);
    }

    private String getAddressFromLocation(LatLng location) {
        // Simplified address based on location in Cambodia
        double lat = location.latitude;
        double lng = location.longitude;
        
        if (lat >= 11.3 && lat <= 13.8 && lng >= 104.0 && lng <= 105.8) {
            // Phnom Penh area
            if (lat >= 11.4 && lat <= 11.8 && lng >= 104.7 && lng <= 105.0) {
                return "Central Phnom Penh";
            }
            return "Phnom Penh";
        } else if (lat >= 12.5 && lat <= 13.8 && lng >= 103.8 && lng <= 105.0) {
            return "Northern Cambodia";
        } else if (lat >= 10.0 && lat <= 11.3 && lng >= 104.5 && lng <= 105.5) {
            return "Southern Cambodia";
        } else if (lat >= 11.0 && lat <= 13.0 && lng >= 102.5 && lng <= 104.0) {
            return "Western Cambodia";
        }
        return "Cambodia";
    }

    private void centerMapOnUserLocation() {
        if (userLocation != null && mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, ZOOM_LEVEL));
            selectedLocation = userLocation;
            mMap.clear();
            addMarker(userLocation);
            Toast.makeText(this, "Centered on your location", Toast.LENGTH_SHORT).show();
        } else if (mMap != null) {
            // Try to get the most recent location available
            if (locationManager != null && ActivityCompat.checkSelfPermission(this, 
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                
                Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                
                Location bestLocation = gpsLocation;
                if (networkLocation != null && (bestLocation == null || 
                        networkLocation.getTime() > bestLocation.getTime())) {
                    bestLocation = networkLocation;
                }
                
                if (bestLocation != null) {
                    userLocation = new LatLng(bestLocation.getLatitude(), bestLocation.getLongitude());
                    selectedLocation = userLocation;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, ZOOM_LEVEL));
                    mMap.clear();
                    addMarker(userLocation);
                    Toast.makeText(this, "Centered on your location", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Waiting for location... Please try again in a moment", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Waiting for location...", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Map is not ready", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }
}
