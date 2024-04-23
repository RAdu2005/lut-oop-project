package com.example.oopandroidapi.main;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public final class CityHistoryStorage {
    private static CityHistoryStorage storage;

    final private static String FILENAME = "cities.data";

    private ArrayList<String> cityList = new ArrayList<>();

    private CityHistoryStorage() {
    }

    public static CityHistoryStorage getInstance() {
        if (storage == null) {
            storage = new CityHistoryStorage();
        }
        return storage;
    }

    public void addCity(String city) {
        int cityIndexInList = -1;
        for (int i = 0; i < cityList.size(); i++) {
            if (cityList.get(i).equals(city)) {
                cityIndexInList = i;
                break;
            }
        }
        if(cityList.size() < 5 && cityIndexInList == -1){
            cityList.add(city);
        }else if (cityIndexInList == -1) {
            cityList.remove(cityList.size() - 1);
            cityList.add(0, city);
        } else {
            cityList.remove(cityIndexInList);
            cityList.add(0, city);
        }
    }


    public ArrayList<String> getCityList() {
        return cityList;
    }

    public void saveHistory(Context context) {
        try {
            ObjectOutputStream userWriter = new ObjectOutputStream(context.openFileOutput(FILENAME, Context.MODE_PRIVATE));
            userWriter.writeObject(cityList);
            userWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCities(Context context) {
        try {
            ObjectInputStream userReader = new ObjectInputStream(context.openFileInput(FILENAME));
            cityList = (ArrayList<String>) userReader.readObject();
            userReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
