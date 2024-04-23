package com.example.oopandroidapi.main;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oopandroidapi.R;

public class CityHistoryListAdapter extends RecyclerView.Adapter<CityHistoryViewHolder> {
    Context context;
    public CityHistoryListAdapter(Context context){this.context = context;}
    @NonNull
    @Override
    public CityHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CityHistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.city_history_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CityHistoryViewHolder holder, int position) {
        holder.cityName.setText(CityHistoryStorage.getInstance().getCityList().get(position));

        //Functionality for clicking on previously searched cities to search for information
        holder.cityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("com.example.HISTORY_PRESSED");
                i.putExtra("cityName", CityHistoryStorage.getInstance().getCityList().get(holder.getAdapterPosition()));
                LocalBroadcastManager.getInstance(context).sendBroadcast(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return CityHistoryStorage.getInstance().getInstance().getCityList().size();
    }
}
