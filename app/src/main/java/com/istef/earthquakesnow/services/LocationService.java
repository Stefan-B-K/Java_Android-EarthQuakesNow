package com.istef.earthquakesnow.services;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;
import androidx.core.util.Consumer;


import static android.content.Context.LOCATION_SERVICE;


public class LocationService {
    private final LocationManager locationManager;
    private final Activity context;

    private final String[] locationPerms = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };


    public LocationService(Activity context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
    }


    public void runLocationUpdates(Consumer<Location> callback) {
        int granted = PackageManager.PERMISSION_GRANTED;
        if (ActivityCompat.checkSelfPermission(context, locationPerms[0]) != granted
                && ActivityCompat.checkSelfPermission(context, locationPerms[1]) != granted) {

            ActivityCompat.requestPermissions(context, locationPerms, 2);
            return;
        }

        LocationListener locationListener = callback::accept;

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locationListener);
    }

}