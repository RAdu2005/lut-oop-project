package com.example.oopandroidapi.data_classes;

import java.io.Serializable;

public class PopulationData implements Serializable {
    private int population, populationChange, divorces;
    private float employmentRate, suffciencyRate;

    public PopulationData(int population, int populationChange, float employmentRate, float sufficiencyRate, int divorces){
        this.population = population;
        this. populationChange = populationChange;
        this.employmentRate = employmentRate;
        this.suffciencyRate = sufficiencyRate;
        this.divorces = divorces;
    }

    public int getPopulation(){
        return population;
    }

    public int getPopulationChange(){
        return populationChange;
    }

    public float getEmploymentRate(){
        return employmentRate;
    }

    public float getSufficiencyRate(){
        return suffciencyRate;
    }
    public int getDivorces(){return divorces;}
}
