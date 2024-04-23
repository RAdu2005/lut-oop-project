package com.example.oopandroidapi.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oopandroidapi.data_classes.DataRetriever;
import com.example.oopandroidapi.R;
import com.example.oopandroidapi.infoview.MunicipalityInfoActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private EditText citySearch;
    private Button searchButton;
    private String searchedCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Handling RecyclerView for internal statistics (search history of cities)
        CityHistoryStorage.getInstance().loadCities(getApplicationContext());
        RecyclerView rvHistory = findViewById(R.id.rvCityHistory);
        rvHistory.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        CityHistoryListAdapter adapter = new CityHistoryListAdapter(getApplicationContext());
        rvHistory.setAdapter(adapter);

        citySearch = (EditText) findViewById(R.id.editCitySearch);
        searchButton = (Button) findViewById(R.id.buttonSearch);

        //Handling incoming Broadcast from ListAdapter class for when a history element is pressed to navigate to a municipality
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, new IntentFilter("com.example.HISTORY_PRESSED"));

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchedCity = citySearch.getText().toString();
                if(searchedCity.length() >= 2){
                    boolean isAlpha = true;
                    char[] chars = searchedCity.toCharArray();

                    for(char c: chars){
                        if(!Character.isLetter(c)){
                            isAlpha = false;
                            break;
                        }
                    }

                    if(isAlpha){
                        searchedCity = searchedCity.toLowerCase();
                        searchedCity = searchedCity.substring(0, 1).toUpperCase() + searchedCity.substring(1, searchedCity.length());
                    }
                }
                ExecutorService service = Executors.newSingleThreadExecutor();
                service.execute(new Runnable() {
                    @Override
                    public void run() {
                        //Checking if municipality exists before switching activity / showing notice if name is invalid
                        if(DataRetriever.municipalityExists(searchedCity)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Adding searched city to history and saving to file
                                    CityHistoryStorage.getInstance().addCity(searchedCity);
                                    CityHistoryStorage.getInstance().saveHistory(getApplicationContext());
                                    adapter.notifyDataSetChanged();
                                }
                            });

                            //Switching activity and putting the name of the searched city as information in Intent
                            Intent i = new Intent(MainActivity.this, MunicipalityInfoActivity.class);
                            i.putExtra("cityName", searchedCity);
                            citySearch.setText("");
                            startActivity(i);
                        } else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Notice when searching for invalid / inappropriately formatted municipality name
                                    Toast.makeText(getApplicationContext(), "Municipality does not exist", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    //Receive Broadcast from ListAdapter class
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("cityName");
            citySearch.setText(data);
            searchButton.performClick();
        }
    };

}
