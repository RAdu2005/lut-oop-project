package com.example.oopandroidapi;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.content.Context;

import androidx.annotation.NonNull;
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
        holder.information.setText("Population");
        holder.value.setText(displayInformation.get("Population"));
    }

    @Override
    public int getItemCount() {
        return displayInformation.size();
    }
}
