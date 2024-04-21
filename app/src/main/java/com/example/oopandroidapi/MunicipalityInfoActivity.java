package com.example.oopandroidapi;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MunicipalityInfoActivity extends AppCompatActivity {
    private TextView titleMunicipality;
    private MunicipalityInfoListAdapter adapter;
    private MunicipalityData municipalityData;
    private LinkedHashMap displayInformation = new LinkedHashMap<>();;
    private String municipalityName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_municipality_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        municipalityName = null;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            municipalityName = extras.getString("cityName");
        }

        titleMunicipality = (TextView) findViewById(R.id.textCityName);
        titleMunicipality.setText(municipalityName);

        RecyclerView rvInfo = findViewById(R.id.rvMunicipalityInfo);
        rvInfo.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        adapter = new MunicipalityInfoListAdapter(getApplicationContext(), displayInformation);
        rvInfo.setAdapter(adapter);

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                MunicipalityData municipalityData = new MunicipalityData(DataRetriever.getPopulationData(municipalityName, getApplicationContext()),
                                                                         DataRetriever.getWeatherData(municipalityName),
                                                                         DataRetriever.getCrimeData(municipalityName, getApplicationContext()),
                                                                         DataRetriever.getHealthData(municipalityName),
                                                                         DataRetriever.getPoliticalData(municipalityName, getApplicationContext()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayInformation.put("Population", String.valueOf(municipalityData.getPopulationData().getPopulation()));

                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}