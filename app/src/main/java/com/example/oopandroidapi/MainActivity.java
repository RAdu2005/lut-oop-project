package com.example.oopandroidapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;
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

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, new IntentFilter("com.example.HISTORY_PRESSED"));

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
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            Intent i = new Intent(MainActivity.this, MunicipalityInfoActivity.class);
                            i.putExtra("cityName", searchedCity);
                            citySearch.setText("");
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
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("cityName");
            citySearch.setText(data);
            searchButton.performClick();
        }
    };

}
