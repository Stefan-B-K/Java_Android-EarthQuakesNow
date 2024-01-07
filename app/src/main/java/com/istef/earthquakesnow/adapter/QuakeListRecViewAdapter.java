package com.istef.earthquakesnow.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.istef.earthquakesnow.R;
import com.istef.earthquakesnow.model.EarthQuake;
import com.istef.earthquakesnow.util.Config;

import java.util.List;

public class QuakeListRecViewAdapter extends RecyclerView.Adapter<QuakeListRecViewAdapter.ViewHolder>{

    private final Activity context;
    private List<EarthQuake> earthQuakeList;

    public QuakeListRecViewAdapter(Activity context, List<EarthQuake> earthQuakeList) {
        this.context = context;
        this.earthQuakeList = earthQuakeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.quake_list_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtQuake.setText(earthQuakeList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return earthQuakeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtQuake;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtQuake = itemView.findViewById(R.id.txtQuakeRowTitle);
            txtQuake.setOnClickListener(v -> {
                String url = earthQuakeList.get(getAdapterPosition()).getUrl();
                Config.showWebDetail(context, url);
            });
        }
    }
}
