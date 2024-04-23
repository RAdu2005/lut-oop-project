package com.example.oopandroidapi.data_classes;

import java.io.Serializable;

public class WeatherData implements Serializable {
    private String weather;
    private long temperature, windSpeed, humidity;
    private float lat, lon;

    public WeatherData(String weather, long temperature, long windSpeed, long humidity, float lat, float lon){
        this.weather = weather;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.lat = lat;
        this.lon = lon;
    }

    public String getWeather(){
        return weather;
    }

    public long getTemperature(){
        return temperature;
    }

    public long getWindSpeed(){
        return windSpeed;
    }

    public long getHumidity(){
        return humidity;
    }

    public float getLatitude(){
        return lat;
    }

    public float getLongitude(){
        return lon;
    }
}
