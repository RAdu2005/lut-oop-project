package com.example.oopandroidapi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Random;

public class Quiz {
    private int population;
    private String cityName;
    private LinkedHashMap<String, ArrayList<String>> questionAnswerMap = new LinkedHashMap<>();
    private MunicipalityData municipalityData;

    public Quiz(String cityName, MunicipalityData municipalityData){
        this.cityName = cityName;
        this.municipalityData = municipalityData;

        this.questionAnswerMap = buildQuiz();
    }

    private ArrayList<String> populationRandomizer(int population){
        Random rand = new Random();
        ArrayList<String> returnList = new ArrayList<>();

        for(int i = 0; i < 3; i++){
            int signModifier = rand.nextInt(1) == 1 ? 1 : -1;
            float modifier = signModifier * rand.nextInt(25) / 100.0f;
            //UNIQUE VALUES!!!!!!!!!!!!!!!!!!!!!!!!!! + testing selecting no option
            returnList.add(String.valueOf(Math.round(population + (population * modifier))));
        }

        returnList.add(String.valueOf(population));
        Collections.shuffle(returnList);
        returnList.add(String.valueOf(population));

        return returnList;
    }

    private ArrayList<String> temperatureRandomizer(long temperature){
        Random rand = new Random();
        ArrayList<String> returnList = new ArrayList<>();

        for(int i = 0; i < 3; i++){
            int signModifier = rand.nextInt(1) == 1 ? 1 : -1;
            float modifier = signModifier * rand.nextInt(25) / 100.0f;

            returnList.add(String.valueOf(Math.round(temperature + (temperature * modifier))));
        }

        returnList.add(String.valueOf(temperature));
        Collections.shuffle(returnList);
        returnList.add(String.valueOf(temperature));

        return returnList;
    }

    private LinkedHashMap<String, ArrayList<String>> buildQuiz(){
        questionAnswerMap.put("What is the population of " + cityName + " ?", populationRandomizer(municipalityData.getPopulationData().getPopulation()));
        questionAnswerMap.put("What is the temperature in " + cityName + " right now?", temperatureRandomizer(municipalityData.getWeatherData().getTemperature()));

        return questionAnswerMap;
    }

    public LinkedHashMap<String, ArrayList<String>> getQuiz(){
        return questionAnswerMap;
    }
}
