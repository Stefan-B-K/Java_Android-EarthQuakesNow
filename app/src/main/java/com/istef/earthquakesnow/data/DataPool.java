package com.istef.earthquakesnow.data;

import android.content.Context;
import android.util.Log;

import androidx.core.util.Consumer;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.istef.earthquakesnow.model.EarthQuake;
import com.istef.earthquakesnow.model.EarthquakeData;
import com.istef.earthquakesnow.services.VolleySingleton;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataPool {

    private List<EarthQuake> earthQuakeList;
    private final Gson gs = new Gson();
    private String URL_STRING = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_week.geojson";


    public void fetchEarthquakeData(Context context) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_STRING, null,
                response -> {
                    EarthquakeData eqData = gs.fromJson(String.valueOf(response), EarthquakeData.class);
                    earthQuakeList = Arrays.stream(eqData.getFeatures()).collect(Collectors.toList());
                },
                error -> {
                    Log.e("===========", "getDataList ERROR: " + error.getLocalizedMessage());
                });
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public List<EarthQuake> getEarthQuakeList() {
        return earthQuakeList;
    }

}
