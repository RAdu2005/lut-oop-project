package com.example.oopandroidapi;

public class MunicipalityData {

    private PopulationData population;
    private WeatherData weather;
    private CrimeData crime;
    private HealthData health;
    private PoliticalData politics;


    public MunicipalityData(PopulationData population, WeatherData weather, CrimeData crime, HealthData health, PoliticalData politics) {
        this.population = population;
        this.weather = weather;
        this.crime = crime;
        this.health = health;
        this.politics = politics;
    }
    
    public PopulationData getPopulationData(){
        return population;
    }

    public WeatherData getWeatherData(){
        return weather;
    }

    public CrimeData getCrimeData(){
        return crime;
    }

    public HealthData getHealthData(){
        return health;
    }

    public PoliticalData getPoliticalData(){
        return politics;
    }
}
