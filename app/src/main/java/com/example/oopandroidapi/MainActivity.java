package com.example.oopandroidapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private EditText citySearch;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CityHistoryStorage.getInstance().loadCities(getApplicationContext());
        RecyclerView rvHistory = findViewById(R.id.rvCityHistory);
        rvHistory.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        CityHistoryListAdapter adapter = new CityHistoryListAdapter(getApplicationContext());
        rvHistory.setAdapter(adapter);

        citySearch = (EditText) findViewById(R.id.editCitySearch);
        searchButton = (Button) findViewById(R.id.buttonSearch);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchedCity = citySearch.getText().toString();

                ExecutorService service = Executors.newSingleThreadExecutor();
                service.execute(new Runnable() {
                    @Override
                    public void run() {
                        if(DataRetriever.municipalityExists(searchedCity)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CityHistoryStorage.getInstance().addCity(searchedCity);
                                    CityHistoryStorage.getInstance().saveHistory(getApplicationContext());
                                    adapter.notifyItemInserted(CityHistoryStorage.getInstance().getCityList().size() - 1);
                                }
                            });
                            Intent i = new Intent(MainActivity.this, MunicipalityInfoActivity.class);
                            i.putExtra("cityName", searchedCity);
                            startActivity(i);
                        } else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Municipality does not exist", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });
    }





}
