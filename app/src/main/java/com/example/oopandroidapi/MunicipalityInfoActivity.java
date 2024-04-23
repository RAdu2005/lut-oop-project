package com.example.oopandroidapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MunicipalityInfoActivity extends AppCompatActivity {
    private TextView titleMunicipality;
    private MunicipalityInfoListAdapter adapter;
    private MunicipalityData municipalityData;
    private LinkedHashMap displayInformation = new LinkedHashMap<>();;
    private ImageView imagePopulation;
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

        imagePopulation = (ImageView) findViewById(R.id.imagePopulation);

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
                municipalityData = new MunicipalityData(municipalityName, getApplicationContext());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Determining the appropriate population image
                        int population = municipalityData.getPopulationData().getPopulation();
                        if(population < 40000){imagePopulation.setImageDrawable(getDrawable(R.drawable.image_small));}
                        else if(population < 100000){imagePopulation.setImageDrawable(getDrawable(R.drawable.image_medium));}
                        else{imagePopulation.setImageDrawable(getDrawable(R.drawable.image_large));}

                        //Displaying population data
                        displayInformation.put("Population", String.valueOf(municipalityData.getPopulationData().getPopulation()));
                        updateList();

                        displayInformation.put("Population change", String.valueOf(municipalityData.getPopulationData().getPopulationChange()));
                        updateList();

                        //Displaying weather data
                        displayInformation.put("Weather", municipalityData.getWeatherData().getWeather());
                        updateList();

                        displayInformation.put("Temperature (°C)", String.valueOf(municipalityData.getWeatherData().getTemperature()));
                        updateList();

                        displayInformation.put("Wind speed (m/s)", String.valueOf(municipalityData.getWeatherData().getWindSpeed()));
                        updateList();

                        displayInformation.put("Humidity (%)", String.valueOf(municipalityData.getWeatherData().getHumidity()));
                        updateList();

                        //Displaying health data
                        displayInformation.put("Patients admitted to primary healthcare after more than 14 days (%)", String.valueOf(municipalityData.getHealthData().getPercentageOver14Days()));
                        updateList();

                        //Displaying political data
                        displayInformation.put("Political composition", "Click to show more");
                        updateList();

                        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, new IntentFilter("com.example.POLITICS_REQUESTED"));
                        }
                });
            }
        });

        Button quizButton = (Button) findViewById(R.id.butttonQuiz);
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MunicipalityInfoActivity.this, QuizActivity.class);
                i.putExtra("cityName", municipalityName);
                i.putExtra("cityData", municipalityData);
                startActivity(i);
            }
        });
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("isPressed");
            if(data.equals("pressed")){
                for(Map.Entry<String, Float> entry : municipalityData.getPoliticalData().getSortedAndNonNullPartyList().entrySet()){
                    displayInformation.put(entry.getKey(), String.valueOf(entry.getValue()));
                    updateList();
                }
            }
        }
    };

    private void updateList(){adapter.notifyItemInserted(adapter.getItemCount() - 1);}
}