package com.istef.earthquakesnow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.istef.earthquakesnow.R;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View view;


    public CustomInfoWindowAdapter(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }


    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        return null;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        TextView title = view.findViewById(R.id.txtInfoTitle);
        TextView snippet = view.findViewById(R.id.txtSnippet);

        title.setText(marker.getTitle());
        snippet.setText(marker.getSnippet());

        return view;
    }
}
