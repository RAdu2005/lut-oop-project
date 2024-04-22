package com.example.oopandroidapi;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CityHistoryViewHolder extends RecyclerView.ViewHolder {
    TextView cityName;
    public CityHistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        cityName = (TextView) itemView.findViewById(R.id.textCityName);
    }
}
