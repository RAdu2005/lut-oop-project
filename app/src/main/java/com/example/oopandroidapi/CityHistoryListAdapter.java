package com.example.oopandroidapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
    }

    @Override
    public int getItemCount() {
        return CityHistoryStorage.getInstance().getInstance().getCityList().size();
    }
}
