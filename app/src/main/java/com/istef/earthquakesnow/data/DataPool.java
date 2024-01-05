package com.istef.earthquakesnow.data;

import android.content.Context;

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
    private final Context context;

    public DataPool(Context context) {
        this.context = context;
    }

    public void getDataList(final AsyncJsonResponse callback) {
        String urlString = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_week.geojson";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlString, null,
                response -> {
                    EarthquakeData eqData = gs.fromJson(String.valueOf(response), EarthquakeData.class);
                    earthQuakeList = Arrays.stream(eqData.getFeatures()).collect(Collectors.toList());

                    if (callback != null) earthQuakeList.forEach(callback::handle);
                },
                error -> {
                });
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }
}
