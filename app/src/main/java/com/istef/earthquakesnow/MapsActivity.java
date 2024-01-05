package com.istef.earthquakesnow;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.istef.earthquakesnow.data.DataPool;
import com.istef.earthquakesnow.databinding.ActivityMapsBinding;
import com.istef.earthquakesnow.model.EarthQuake;
import com.istef.earthquakesnow.services.LocationService;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng myPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        FloatingActionButton fab = findViewById(R.id.fabLocation);
        fab.setOnClickListener(v -> showMyLocation());

        getMyLocation();
        new DataPool(this).getDataList(this::handleEarthQuake);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        } else {
            Toast.makeText(this, "Location service access denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkGooglePlayServices();
    }

    private void checkGooglePlayServices() {
        int responseCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (responseCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = GoogleApiAvailability.getInstance()
                    .getErrorDialog(this, responseCode, responseCode, dialog -> {
                        Toast.makeText(this, "No Google Services", Toast.LENGTH_LONG)
                                .show();
                        finish();
                    });
            assert errorDialog != null;
            errorDialog.show();
        }
    }


    private void getMyLocation() {
        new LocationService(this).runLocationUpdates(location ->
                myPosition = new LatLng(
                        location.getLatitude(),
                        location.getLongitude()));
    }

    private void showMyLocation() {
        if (myPosition == null) return;

        mMap.addMarker(new MarkerOptions()
                .position(myPosition)
                .title("My Location")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 13));
    }


    private void handleEarthQuake(EarthQuake earthQuake) {
        Marker marker = mMap.addMarker(earthQuake.markerOptions());
        if (marker != null) marker.setTag(earthQuake.getDetail());
    }


}