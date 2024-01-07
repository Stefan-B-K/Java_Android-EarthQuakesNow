package com.istef.earthquakesnow.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;

import com.istef.earthquakesnow.R;
import com.istef.earthquakesnow.adapter.QuakeListRecViewAdapter;
import com.istef.earthquakesnow.model.EarthQuake;

import java.util.List;

public class QuakeListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quake_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<EarthQuake> earthQuakeList = (List<EarthQuake>) getIntent().getExtras().getSerializable("earthquakeList");
        QuakeListRecViewAdapter quakeListRecViewAdapter = new QuakeListRecViewAdapter(this, earthQuakeList);
        recyclerView.setAdapter(quakeListRecViewAdapter);

        ImageButton btnClose = findViewById(R.id.btnCloseList);
        btnClose.setOnClickListener(v -> finish());

    }
}