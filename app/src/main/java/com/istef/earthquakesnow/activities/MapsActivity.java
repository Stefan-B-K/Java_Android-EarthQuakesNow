package com.istef.earthquakesnow.activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
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
import com.istef.earthquakesnow.R;
import com.istef.earthquakesnow.data.DataPool;
import com.istef.earthquakesnow.databinding.ActivityMapsBinding;
import com.istef.earthquakesnow.model.EarthQuake;
import com.istef.earthquakesnow.services.LocationService;
import com.istef.earthquakesnow.view.CustomInfoWindow;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private LatLng myPosition;
    private DataPool dataPool;
    private GoogleMap.InfoWindowAdapter customInfoWindow;


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
        customInfoWindow = new CustomInfoWindow(this);
        dataPool = new DataPool(this);

        FloatingActionButton fabLocation = findViewById(R.id.fabLocation);
        fabLocation.setOnClickListener(v -> showMyLocation());

        FloatingActionButton fabList = findViewById(R.id.fabList);
        fabList.setOnClickListener(v -> showMyLocation());

        getMyLocation();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        dataPool.getEarthQuakeList().forEach(earthQuake -> {
            Marker marker = mMap.addMarker(markerOptions(earthQuake));
            if (marker != null) marker.setTag(earthQuake.getUrl());
        });
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

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(myPosition)
                .title("My Location")
                .snippet("Lat: " + myPosition.latitude + " Lon: " + myPosition.longitude)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        if (marker != null) marker.setTag("myLocation");

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 13));
    }

    public MarkerOptions markerOptions(EarthQuake earthQuake) {
        float markerColor;
        double mag = earthQuake.getMag();

        if (mag <= 5.0) {
            markerColor = BitmapDescriptorFactory.HUE_YELLOW;
        } else if (mag <= 6) {
            markerColor = BitmapDescriptorFactory.HUE_ORANGE;
        } else if (mag <= 7) {
            markerColor = BitmapDescriptorFactory.HUE_RED;
        } else {
            markerColor = BitmapDescriptorFactory.HUE_VIOLET;
        }
        return new MarkerOptions()
                .position(earthQuake.getPosition())
                .title(earthQuake.getPlace())
                .snippet("Magnitude: " + earthQuake.getMag() + "\nDate: " + earthQuake.getTime())
                .icon(BitmapDescriptorFactory.defaultMarker(markerColor));
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        if (marker.getTag().equals("myLocation")) return;

        View view = getLayoutInflater().inflate(R.layout.detail_web, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        WebView webView = view.findViewById(R.id.wvDetail);
        ImageButton btnClose = view.findViewById(R.id.btnClose);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl(marker.getTag().toString());
        btnClose.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.show();
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        mMap.setInfoWindowAdapter(marker.getTag()
                .equals("myLocation") ? null : customInfoWindow);

        return false;
    }
}