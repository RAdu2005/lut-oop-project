package com.example.oopandroidapi.main;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public final class CityHistoryStorage {
    //Singleton class for handling the internal statistics (history) of previously searched cities by writing serialized Strings of city names to the "cities.data" file
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
        //Checking if the new city is unique in the history
        int cityIndexInList = -1;
        for (int i = 0; i < cityList.size(); i++) {
            if (cityList.get(i).equals(city)) {
                cityIndexInList = i;
                break;
            }
        }

        if(cityList.size() < 5 && cityIndexInList == -1){ //Adding a city if it is unique and list is not at max capacity (5)
            cityList.add(city);
        }else if (cityIndexInList == -1) { //Removing last city and adding new unique one on top
            cityList.remove(cityList.size() - 1);
            cityList.add(0, city);
        } else { //Moving already existing city in list to the top
            cityList.remove(cityIndexInList);
            cityList.add(0, city);
        }
    }


    public ArrayList<String> getCityList() {
        return cityList;
    }

    public void saveHistory(Context context) {
        //Writing data to file
        try {
            ObjectOutputStream userWriter = new ObjectOutputStream(context.openFileOutput(FILENAME, Context.MODE_PRIVATE));
            userWriter.writeObject(cityList);
            userWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCities(Context context) {
        //Reading data from file
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
