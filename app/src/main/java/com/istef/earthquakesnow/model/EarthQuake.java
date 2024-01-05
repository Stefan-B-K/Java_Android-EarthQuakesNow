package com.istef.earthquakesnow.model;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.util.Date;

public class EarthQuake {
    private class Properties {
        private String title;
        private double mag;
        private String place;
        private long time;
        private String detail;
    }

    private class Geometry {
        private double[] coordinates;                                   // [ lon, lat, depth ]   ! ! !
    }


    private Properties properties;
    private Geometry geometry;


    public String getTitle() {
        return properties.title;
    }

    public double getMag() {
        return properties.mag;
    }

    public String getPlace() {
        return properties.place;
    }

    public String getTime() {
        DateFormat dateFormat = DateFormat.getDateInstance();
        return dateFormat.format(new Date(properties.time));
    }

    public String getDetail() {
        return properties.detail;
    }

    public double getLon() {
        return geometry.coordinates[0];
    }

    public double getLat() {
        return geometry.coordinates[1];
    }

    // km
    public double getDepth() {
        return geometry.coordinates[2];
    }


    // Map properties
    private LatLng position;
    private MarkerOptions markerOptions;

    public LatLng getPosition() {
        if (position != null) {
            return position;
        }
        position = new LatLng(getLat(), getLon());
        return position;
    }

    public MarkerOptions markerOptions() {
        if (markerOptions != null) {
            return markerOptions;
        }
        markerOptions = new MarkerOptions()
                .position(getPosition())
                .title(getTitle())
                .icon(BitmapDescriptorFactory.defaultMarker(markerColor()));
        return markerOptions;
    }

    private float markerColor() {
        if (getMag() <= 3.0) {
            return BitmapDescriptorFactory.HUE_YELLOW;
        } else if (getMag() <= 5) {
            return BitmapDescriptorFactory.HUE_ORANGE;
        } else if (getMag() <= 6) {
            return BitmapDescriptorFactory.HUE_RED;
        } else {
            return BitmapDescriptorFactory.HUE_VIOLET;
        }

    }
}
