package com.example.oopandroidapi.data_classes;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.example.oopandroidapi.R;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class DataRetriever {

    private static final String populationDataURL = "https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/synt/statfin_synt_pxt_12dy.px";
    private static final String employmentDataURL = "https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/tyokay/statfin_tyokay_pxt_115x.px";
    private static final String sufficiencyDataURL = "https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/tyokay/statfin_tyokay_pxt_125s.px";
    private static final String municipalityAVICodeURL = "https://data.stat.fi/api/classifications/v2/correspondenceTables/kunta_1_20240101%23avi_1_20240101/maps?content=data&format=json&lang=en&meta=min";
    private static final String healthDataURL = "https://sampo.thl.fi/pivot/prod/fi/avohpaasy/pthjono01/fact_ahil_pthjono01.json";
    private static final String politicalDataURL = "https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/kvaa/statfin_kvaa_pxt_12xf.px";
    private static final String openWeatherAPIKey = "870af1c72b0a087b06c3655ea9a7ee6c";
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode areas = readAreaDataFromTheAPIURL(objectMapper);
    public static HashMap<String, String> municipalityNamesToCodesMap = createMunicipalityNamesToCodesMap(areas);

    //Getting data via GET with ObjectMapper
    private static JsonNode getData(String sourceURL){
        try {            
            URL locationUrl = new URL(sourceURL);

            JsonNode municipalityData = objectMapper.readTree(locationUrl);

            return municipalityData;

        } catch(MalformedURLException e){
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Getting data via POST with HttpURLConnection
    private static JsonNode getData(InputStream query, String sourceURL, int queryAreaPos, String code){
        try {
            // The query for fetching data from a single municipality is stored in a json file in the res/raw folder
            JsonNode jsonQuery = objectMapper.readTree(query);
            // Replacing the code in the query with the desired one
            ((ObjectNode)jsonQuery.findValue("query").get(queryAreaPos).get("selection")).putArray("values").add(code);

            HttpURLConnection con = connectToAPIAndSendPostRequest(objectMapper, jsonQuery, sourceURL);

            //Reading from the connection
            try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                JsonNode municipalityData = objectMapper.readTree(response.toString());

                return municipalityData;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Establishing the connection with the API and sending the query via POST
    private static HttpURLConnection connectToAPIAndSendPostRequest(ObjectMapper objectMapper, JsonNode jsonQuery, String sourceURL)
            throws MalformedURLException, IOException, ProtocolException, JsonProcessingException {
        URL url = new URL (sourceURL);

        HttpURLConnection con = (HttpURLConnection)url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = objectMapper.writeValueAsBytes(jsonQuery);
            os.write(input, 0, input.length);			
        }
        return con;
    }

    //Creating the initial mapping of city names to municipality codes
    private static HashMap<String, String> createMunicipalityNamesToCodesMap(JsonNode areas) {
        JsonNode codes = null;
        JsonNode names = null;

        for (JsonNode node :areas.findValue("variables")) {
            if (node.findValue("text").asText().equals("Area")) {
                 codes =  node.findValue("values");
                 names = node.findValue("valueTexts");
            }
        }

        HashMap<String, String> municipalityNamesToCodesMap = new HashMap<>();

        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i).asText();
            String code = codes.get(i).asText();
            municipalityNamesToCodesMap.put(name, code);

        }
        return municipalityNamesToCodesMap;
    }


    //Initial GET request for getting municipality codes
    private static JsonNode readAreaDataFromTheAPIURL(ObjectMapper objectMapper) {
        JsonNode areas = null;
        try {
            areas = objectMapper.readTree(new URL("https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/synt/statfin_synt_pxt_12dy.px"));
   
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return areas;
    }

    //Bool function to check if a municipality exists (case- and diacritic-sensitive)
    public static boolean municipalityExists(String municipalityName){
        String code = municipalityNamesToCodesMap.get(municipalityName);
        if(code == null){return false;}
        return true;
    } 

    //Getting population data and creating object
    public static PopulationData getPopulationData(String municipalityName, Context context){
        String code = municipalityNamesToCodesMap.get(municipalityName);
        //Getting population and population change data (with Statistics Finland)

        JsonNode populationData = getData(context.getResources().openRawResource(R.raw.query_population), populationDataURL, 0, code);

        int population, populationChange, divorces;
        ArrayNode populationDataArray = objectMapper.createArrayNode();

        populationData.get("value").elements().forEachRemaining(populationDataArray::add);
        divorces = populationDataArray.get(0).intValue();
        populationChange = populationDataArray.get(1).intValue();
        population = populationDataArray.get(2).intValue();

        //Getting employment rate data (with Statistics Finland)

        JsonNode employmentData = getData(context.getResources().openRawResource(R.raw.query_employment), employmentDataURL, 0, code);

        float employmentRate;
        employmentRate = employmentData.get("value").get(0).floatValue();

        //Getting workplace self-sufficiency data (with Statistics Finland)
            
        JsonNode sufficiencyData = getData(context.getResources().openRawResource(R.raw.query_sufficiency), sufficiencyDataURL, 0, code);
        float sufficiencyRate;
        sufficiencyRate = sufficiencyData.get("value").get(0).floatValue();

        return new PopulationData(population, populationChange, employmentRate, sufficiencyRate, divorces);
    }

    //Getting weather data and creating object
    public static WeatherData getWeatherData(String municipalityName){
        //Geolocation of city (with OpenWeather)
        String geolocationURL = "https://api.openweathermap.org/geo/1.0/direct?q=" + municipalityName +",FI&limit=1&appid=" + openWeatherAPIKey;
        JsonNode geolocationData = getData(geolocationURL);
        float cityLat, cityLon;
        cityLat = geolocationData.get(0).get("lat").floatValue();
        cityLon = geolocationData.get(0).get("lon").floatValue();

        //Relevant weather data in the city (with OpenWeather)
        String weatherURL = "https://api.openweathermap.org/data/2.5/weather?lat=" + cityLat + "&lon=" + cityLon +"&appid=" + openWeatherAPIKey;
        JsonNode weatherData = getData(weatherURL);
        String weatherMain = weatherData.get("weather").get(0).get("main").asText();
        long weatherTemp = Math.round(weatherData.get("main").get("temp").floatValue() - 273.15);
        long weatherWind = Math.round(weatherData.get("wind").get("speed").floatValue());
        long weatherHumidity = Math.round(weatherData.get("main").get("humidity").intValue());

        return new WeatherData(weatherMain, weatherTemp, weatherWind, weatherHumidity, cityLat, cityLon);
    }

    //Getting health data and creating obbject
    public static HealthData getHealthData(String municipalityName){
        String code = municipalityNamesToCodesMap.get(municipalityName);
        code = code.substring(2, 5);
        //Getting code of AVI the municipality is part of (with Statistics Finland)

        JsonNode municipalityCodeToAVICode = getData(municipalityAVICodeURL);
        int aviCode = -1;
        if(municipalityCodeToAVICode.isArray()){
            for(JsonNode node : municipalityCodeToAVICode){
                String foundCode = node.get("sourceItem").get("code").asText();
                if(foundCode.equals(code)){
                    aviCode = Integer.parseInt(node.get("targetItem").get("code").asText()) - 1;
                    break;
                }
            }
        }

        //Getting wait times by AVI (with THL)
        JsonNode healthData = getData(healthDataURL);

        //Parsing the returned matrix and calculating the position of relevant data
        int patientsOver14Days, patientsTotal = -1, rowStartOfDataByAVI = aviCode * 6;
        patientsTotal = Integer.parseInt(healthData.get("dataset").get("value").get(String.valueOf(rowStartOfDataByAVI + 5)).asText());
        patientsOver14Days = Integer.parseInt(healthData.get("dataset").get("value").get(String.valueOf(rowStartOfDataByAVI + 2)).asText()) +
                             Integer.parseInt(healthData.get("dataset").get("value").get(String.valueOf(rowStartOfDataByAVI + 3)).asText()) +
                             Integer.parseInt(healthData.get("dataset").get("value").get(String.valueOf(rowStartOfDataByAVI + 4)).asText());
        
        //Calculating the percentage of patients admitted to healthcare after more than 14 days + checking for division by zero
        if(patientsTotal != 0){
            float percentageOver14Days = Math.round((patientsOver14Days *  1.0f) / patientsTotal * 10000) / 100.0f;
            return new HealthData(percentageOver14Days);
        }
        return new HealthData(0);
    }

    //Getting political data and creating object
    public static PoliticalData getPoliticalData(String municipalityName, Context context){
        HashMap<String, Float> returnMap = new HashMap<>();

        String code = municipalityNamesToCodesMap.get(municipalityName);
        code = code.substring(2, 5);

        //Getting constituency code of the municipality (with Statistics Finland)
        JsonNode politicalCorrespondenceData = getData(politicalDataURL);
        String constituencyCode = null;
        if(politicalCorrespondenceData.get("variables").get(1).get("values").isArray()){
            for(JsonNode node : politicalCorrespondenceData.get("variables").get(1).get("values")){
                if(node.asText().substring(3, 6).equals(code)){
                    constituencyCode = node.asText();
                    break;
                }
            }
        }

        //Handling data for Aland (is not included in the 2021 Municipal elections dataset)
        if(constituencyCode == null){
            returnMap.put("Political data for Åland not available", -1.0f);
            return new PoliticalData(returnMap);
        }
        
        //Getting local council composition by % (with Statistics Finland)
        JsonNode politicalData = getData(context.getResources().openRawResource(R.raw.query_political), politicalDataURL, 0, constituencyCode);
        ArrayList<String> keys = new ArrayList<>();
        Iterator<String> keyIterator = politicalData.get("dimension").get("Puolue").get("category").get("label").fieldNames();
        while(keyIterator.hasNext()){
            keys.add(keyIterator.next());
        }

        //Building HashMap of key: party name and value: percentage of seats in council
        if(politicalData.get("value").isArray()){
            int count = 0;
            for(JsonNode node: politicalData.get("value")){
                String key = keys.get(count);
                String partyName = politicalData.get("dimension").get("Puolue").get("category").get("label").get(key).asText();
                returnMap.put(partyName, node.floatValue());
                count++;
            }
        }
        
        return new PoliticalData(returnMap);
    }
}
