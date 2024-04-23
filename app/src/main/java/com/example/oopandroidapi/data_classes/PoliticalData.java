package com.example.oopandroidapi.data_classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class PoliticalData implements Serializable {
    private HashMap<String, Float> councilComposition = new HashMap<>();

    public PoliticalData(HashMap<String, Float> councilComposition){
        this.councilComposition = councilComposition;
    }

    public HashMap<String, Float> getCouncilComposition(){
        return councilComposition;
    }

    
    public LinkedHashMap<String, Float> getSortedAndNonNullPartyList(){
        LinkedHashMap<String, Float> sortedCouncilComposition = new LinkedHashMap<>();
        ArrayList<Float> list = new ArrayList<>();
        for(Map.Entry<String, Float> entry : councilComposition.entrySet()){
            list.add(entry.getValue());
        }

        Collections.sort(list, Collections.reverseOrder());
        for(Float num : list){
            for(Entry<String, Float> entry : councilComposition.entrySet()){
                if(entry.getValue().equals(num) && num != 0.0){
                    sortedCouncilComposition.put(entry.getKey(), num);
                }
            }
        }
        return sortedCouncilComposition;
    }
}
