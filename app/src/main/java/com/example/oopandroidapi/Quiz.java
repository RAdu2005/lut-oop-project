package com.example.oopandroidapi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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
        HashSet<Integer> numbers = new HashSet<>();
        ArrayList<String> returnList = new ArrayList<>();

        numbers.add(population);
        while(numbers.size() < 4){
            int signModifier = rand.nextInt(1) == 1 ? 1 : -1;
            float modifier = signModifier * rand.nextInt(25) / 100.0f;
            //UNIQUE VALUES!!!!!!!!!!!!!!!!!!!!!!!!!! + testing selecting no option
            numbers.add(Math.round(population + (population * modifier)));
        }

        for(Integer i : numbers){
            returnList.add(String.valueOf(i));
        }

        Collections.shuffle(returnList);
        returnList.add(String.valueOf(population));

        return returnList;
    }

    private ArrayList<String> temperatureRandomizer(long temperature){
        Random rand = new Random();
        HashSet<Integer> numbers = new HashSet<>();
        ArrayList<String> returnList = new ArrayList<>();

        numbers.add((int) temperature);
        while(numbers.size() < 4){
            int signModifier = rand.nextInt(1) == 1 ? 1 : -1;
            float modifier = signModifier * rand.nextInt(45) / 100.0f;
            numbers.add(Math.round(temperature + (temperature * modifier) + rand.nextInt(5) * signModifier));
        }

        for(Integer i : numbers){
            returnList.add(String.valueOf(i));
        }

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
