package com.example.oopandroidapi;

import java.io.Serializable;

public class HealthData implements Serializable {
    private float percentageOver14Days;

    public HealthData(float percentageOver14Days){
        this.percentageOver14Days = percentageOver14Days;
    }

    public float getPercentageOver14Days(){
        return percentageOver14Days;
    }
}
