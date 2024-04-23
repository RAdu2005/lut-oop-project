package com.example.oopandroidapi.data_classes;

import android.content.Context;

import java.io.Serializable;

public class MunicipalityData implements Serializable {
    //Class to store all data about a municipality with appropriate constructor and getters
    private PopulationData population = null;
    private WeatherData weather = null;
    private HealthData health = null;
    private PoliticalData politics = null;

    public MunicipalityData(String municipalityName, Context context) {
        this.population = DataRetriever.getPopulationData(municipalityName, context);
        this.weather = DataRetriever.getWeatherData(municipalityName);
        this.health = DataRetriever.getHealthData(municipalityName);
        this.politics = DataRetriever.getPoliticalData(municipalityName, context);
    }

    public PopulationData getPopulationData() {
        return population;
    }

    public WeatherData getWeatherData() {
        return weather;
    }


    public HealthData getHealthData() {
        return health;
    }

    public PoliticalData getPoliticalData() {
        return politics;
    }

}
