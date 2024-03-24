package com.example.oopandroidapi;

public class MunicipalityData {

    private int year;
    private int population;


    public MunicipalityData(int year, int population) {
        this.year = year;
        this.population = population;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public int getPopulation() {
        return population;
    }
    public void setPopulation(int population) {
        this.population = population;
    }

}
