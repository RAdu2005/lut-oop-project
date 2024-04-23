package com.example.oopandroidapi.infoview;

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

import com.example.oopandroidapi.data_classes.MunicipalityData;
import com.example.oopandroidapi.R;
import com.example.oopandroidapi.quiz.QuizActivity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MunicipalityInfoActivity extends AppCompatActivity {
    private TextView titleMunicipality ,textIndex;
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

        //Disabling the quiz button until all data is fetched
        Button quizButton = (Button) findViewById(R.id.butttonQuiz);
        quizButton.setEnabled(false);

        imagePopulation = (ImageView) findViewById(R.id.imagePopulation);

        //Getting municipality name from MainActivity intent
        municipalityName = null;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            municipalityName = extras.getString("cityName");
        }

        //Setting the title to the searched municipality
        titleMunicipality = (TextView) findViewById(R.id.textCityName);
        titleMunicipality.setText(municipalityName);

        //Building the RecyclerView
        RecyclerView rvInfo = findViewById(R.id.rvMunicipalityInfo);
        rvInfo.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        adapter = new MunicipalityInfoListAdapter(getApplicationContext(), displayInformation);
        rvInfo.setAdapter(adapter);

        //Runnable to get API data in the background and display after requests are complete
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                //Creating data object and completing API requests
                municipalityData = new MunicipalityData(municipalityName, getApplicationContext());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Determining the appropriate population image based on the size of municipality (3 possibilities)
                        int population = municipalityData.getPopulationData().getPopulation();
                        if(population < 40000){imagePopulation.setImageDrawable(getDrawable(R.drawable.image_small));}
                        else if(population < 100000){imagePopulation.setImageDrawable(getDrawable(R.drawable.image_medium));}
                        else{imagePopulation.setImageDrawable(getDrawable(R.drawable.image_large));}

                        //Displaying population data
                        displayInformation.put("Population", String.valueOf(municipalityData.getPopulationData().getPopulation()));
                        updateList();

                        displayInformation.put("Population change", String.valueOf(municipalityData.getPopulationData().getPopulationChange()));
                        updateList();

                        //Displaying employment data
                        displayInformation.put("Employment rate (%)", String.valueOf(municipalityData.getPopulationData().getEmploymentRate()));
                        updateList();

                        displayInformation.put("Workplace self-sufficiency rate (%)", String.valueOf(municipalityData.getPopulationData().getSufficiencyRate()));

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

                        //Calculate and display index
                        calculateAndDisplayCAI(municipalityData);

                        //Enabling the quiz button since all data has been fetched
                        quizButton.setEnabled(true);

                        //Handling the display-on-request of political composition by OnClickListener in Adapter class via Broadcast and Intent
                        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, new IntentFilter("com.example.POLITICS_REQUESTED"));
                        }
                });
            }
        });

        //Launching quiz activity on "Quiz" button press
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

    //Handling the broadcast from the Adapter class (for political data)
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

    //Adding a single item to the RecyclerView list
    private void updateList(){adapter.notifyItemInserted(adapter.getItemCount() - 1);}

    private void calculateAndDisplayCAI(MunicipalityData municipalityData){
        textIndex = (TextView) findViewById(R.id.textIndex);

        float score = 1.8f;

        float percentageAfter14Days = municipalityData.getHealthData().getPercentageOver14Days();
        int population = municipalityData.getPopulationData().getPopulation();
        int populationChange = municipalityData.getPopulationData().getPopulationChange();
        float employmentRate = municipalityData.getPopulationData().getEmploymentRate();
        float sufficiencyRate = municipalityData.getPopulationData().getSufficiencyRate();

        score += (-1 * percentageAfter14Days) / 100.0f +
                (populationChange * 1.0f / population) * 100 +
                (employmentRate - 70) / 10.0f +
                (sufficiencyRate - 100) / 12.5f;

        score = Math.round(score * 10) / 10.0f;

        if(score <= 0){
            score = 0.1f;
        }else if(score >= 5){
            score = 5.0f;
        }

        textIndex.setText("City Attractiveness Index: " + score + "⭐");
    }
}