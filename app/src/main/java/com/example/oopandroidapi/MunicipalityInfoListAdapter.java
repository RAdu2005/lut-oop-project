package com.example.oopandroidapi;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedHashMap;
import java.util.Map;

public class MunicipalityInfoListAdapter extends RecyclerView.Adapter<MunicipalityInfoViewHolder> {

    private final Context context;

    private LinkedHashMap<String, String> displayInformation;
    public MunicipalityInfoListAdapter(Context context, LinkedHashMap<String, String> displayInformation){
        this.context = context;
        this.displayInformation = displayInformation;
    }
    @NonNull
    @Override
    public MunicipalityInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MunicipalityInfoViewHolder(LayoutInflater.from(context).inflate(R.layout.municipality_info_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MunicipalityInfoViewHolder holder, int position) {
        int count = 0;
        for(Map.Entry<String, String> entry : displayInformation.entrySet()){
            if(position == count){
                holder.information.setText(entry.getKey());
                holder.value.setText(entry.getValue());
                if(entry.getKey().equals("Political composition")){
                    holder.information.setClickable(true);
                    holder.value.setClickable(true);

                    holder.information.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent("com.example.POLITICS_REQUESTED");
                            i.putExtra("isPressed", "pressed");
                            LocalBroadcastManager.getInstance(context).sendBroadcast(i);
                            holder.information.setClickable(false);
                            holder.value.setClickable(false);
                        }
                    });
                    holder.value.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent("com.example.POLITICS_REQUESTED");
                            i.putExtra("isPressed", "pressed");
                            LocalBroadcastManager.getInstance(context).sendBroadcast(i);
                            holder.information.setClickable(false);
                            holder.value.setClickable(false);
                        }
                    });
                }
            }
            count++;
        }
    }

    @Override
    public int getItemCount() {
        return displayInformation.size();
    }

}
