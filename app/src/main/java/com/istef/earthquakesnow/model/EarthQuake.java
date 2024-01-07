package com.istef.earthquakesnow.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

public class EarthQuake implements Serializable {

    private class Properties implements Serializable{
        private String title;
        private double mag;
        private String place;
        private long time;
        private String url;
    }

    private class Geometry implements Serializable{
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

    public String getUrl() {
        return properties.url;
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
    public LatLng getPosition() {
        return new LatLng(getLat(), getLon());
    }

}
