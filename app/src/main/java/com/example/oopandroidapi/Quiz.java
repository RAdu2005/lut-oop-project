package com.example.oopandroidapi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Random;

public class Quiz {
    private int population;
    private String cityName;
    private LinkedHashMap<String, ArrayList<String>> questionAnswerMap = new LinkedHashMap<>();

    public Quiz(String cityName, int population){
        this.cityName = cityName;

        this.population = population;
    }

    private ArrayList<String> populationRandomizer(int population){
        Random rand = new Random();
        ArrayList<String> returnList = new ArrayList<>();

        for(int i = 0; i < 3; i++){
            int signModifier = rand.nextInt(1) == 1 ? 1 : -1;
            int modifier = signModifier * rand.nextInt(25);

            returnList.add(String.valueOf(modifier * population));
        }

        returnList.add(String.valueOf(population));
        Collections.shuffle(returnList);

        return returnList;
    }

    public LinkedHashMap<String, ArrayList<String>> getQuestionsAndAnswerts(){
        questionAnswerMap.put("What is the population of " + cityName + " ?", populationRandomizer(population));

        return questionAnswerMap;
    }
}
