package com.example.oopandroidapi;

import java.io.Serializable;

public class PopulationData implements Serializable {
    private int population, populationChange;
    private float employmentRate, suffciencyRate;

    public PopulationData(int population, int populationChange, float employmentRate, float suffciencyRate){
        this.population = population;
        this. populationChange = populationChange;
        this.employmentRate = employmentRate;
        this.suffciencyRate = suffciencyRate;
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
}
