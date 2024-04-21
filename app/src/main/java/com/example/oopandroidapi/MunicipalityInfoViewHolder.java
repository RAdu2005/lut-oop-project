package com.example.oopandroidapi;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MunicipalityInfoViewHolder extends RecyclerView.ViewHolder {
    TextView information, value;
    public MunicipalityInfoViewHolder(@NonNull View itemView) {
        super(itemView);
        information = itemView.findViewById(R.id.textInfo);
        value = itemView.findViewById(R.id.textValue);
    }
}
