package com.example.oopandroidapi.quiz;

import com.example.oopandroidapi.data_classes.MunicipalityData;

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
    private Random rand = new Random();

    public Quiz(String cityName, MunicipalityData municipalityData){
        this.cityName = cityName;
        this.municipalityData = municipalityData;
        this.questionAnswerMap = buildQuiz();
    }

    //Randomizer functions for generating 3 additional, believable answers for each question

    private ArrayList<String> populationRandomizer(int population){
        HashSet<Integer> numbers = new HashSet<>();
        ArrayList<String> returnList = new ArrayList<>();

        numbers.add(population);
        while(numbers.size() < 4){
            int signModifier = rand.nextInt(2) == 1 ? 1 : -1;
            float modifier = signModifier * rand.nextInt(25) / 100.0f;
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
        HashSet<Integer> numbers = new HashSet<>();
        ArrayList<String> returnList = new ArrayList<>();

        numbers.add((int) temperature);
        while(numbers.size() < 4){
            int signModifier = rand.nextInt(2) == 1 ? 1 : -1;
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

    private ArrayList<String> divorcesRandomizer(int divorces){
        HashSet<Integer> numbers = new HashSet<>();
        ArrayList<String> returnList = new ArrayList<>();

        numbers.add(divorces);
        while(numbers.size() < 4){
            int signModifier = rand.nextInt(2) == 1 ? 1 : -1;
            float modifier = signModifier * (rand.nextInt(25) * 2) / 100.0f;
            numbers.add(Math.round(divorces + (divorces * modifier)));
        }

        for(Integer i : numbers){
            returnList.add(String.valueOf(i));
        }

        Collections.shuffle(returnList);
        returnList.add(String.valueOf(divorces));

        return returnList;
    }

    private ArrayList<String> employmentRandomizer(float employmentRate){
        HashSet<Float> numbers = new HashSet<>();
        ArrayList<String> returnList = new ArrayList<>();

        numbers.add(employmentRate);
        while(numbers.size() < 4){
            int signModifier = rand.nextInt(2) == 1 ? 1 : -1;
            float modifier = signModifier * rand.nextInt(32) / 100.0f;
            numbers.add(Math.round((employmentRate + (employmentRate * modifier)) * 10) / 10.0f);
        }

        for(Float i : numbers){
            returnList.add(String.valueOf(i));
        }

        Collections.shuffle(returnList);
        returnList.add(String.valueOf(employmentRate));

        return returnList;
    }

    private LinkedHashMap<String, ArrayList<String>> buildQuiz(){
        //Populating map with questions and answer keys, as well as the answer on the last (undisplayed) position for checking
        questionAnswerMap.put("What is the population of " + cityName + " ?", populationRandomizer(municipalityData.getPopulationData().getPopulation()));
        questionAnswerMap.put("What is the temperature in " + cityName + " right now?", temperatureRandomizer(municipalityData.getWeatherData().getTemperature()));
        questionAnswerMap.put("How many divorces took place in " + cityName + " in 2022?", divorcesRandomizer(municipalityData.getPopulationData().getDivorces()));
        questionAnswerMap.put("What is the employment rate in " + cityName + " in 2022?", employmentRandomizer(municipalityData.getPopulationData().getEmploymentRate()));

        return questionAnswerMap;
    }

    public LinkedHashMap<String, ArrayList<String>> getQuiz(){
        return questionAnswerMap;
    }
}
