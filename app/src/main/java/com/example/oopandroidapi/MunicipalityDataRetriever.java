package com.example.oopandroidapi;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MunicipalityDataRetriever {

    static ObjectMapper objectMapper = new ObjectMapper();

    static HashMap<String, String> municipalityNamesToCodesMap = null;

    /**
     * Get municipality codes, we need to do this only once
     *
     */
    public static void getMunicipalityCodesMap() {
        if (municipalityNamesToCodesMap == null) {
            JsonNode areas = readAreaDataFromTheAPIURL(objectMapper);
            municipalityNamesToCodesMap = createMunicipalityNamesToCodesMap(areas);
        }
    }

    public ArrayList<MunicipalityData> getData(Context context, String municipalityName) {
        //System.out.println(municipalityNamesToCodesMap);

        String code = municipalityNamesToCodesMap.get(municipalityName);


        try {
            // The query for fetching data from a single municipality is stored in query.json
            JsonNode jsonQuery = objectMapper.readTree(context.getResources().openRawResource(R.raw.query));
            // Let's replace the municipality code in the query with the municipality that the user gave
            // as input
            ((ObjectNode) jsonQuery.findValue("query").get(0).get("selection")).putArray("values").add(code);


            HttpURLConnection con = connectToAPIAndSendPostRequest(objectMapper, jsonQuery);


            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                JsonNode municipalityData = objectMapper.readTree(response.toString());

                ArrayList<String> years = new ArrayList<>();
                JsonNode populations = null;

                //System.out.println(municipalityData.toPrettyString() );

                for (JsonNode node : municipalityData.get("dimension").get("Vuosi")
                        .get("category").get("label")) {
                    years.add(node.asText());
                }

                // System.out.println(years);

                populations = municipalityData.get("value");

                ArrayList<MunicipalityData> populationData = new ArrayList<>();


                for (int i = 0; i < populations.size(); i++) {
                    Integer population = populations.get(i).asInt();
                    populationData.add(new MunicipalityData(Integer.parseInt(years.get(i)), population));
                }

                System.out.println(municipalityName);
                System.out.println("==========================");


                for (MunicipalityData data : populationData) {
                    System.out.print(data.getYear() + ": " + data.getPopulation() + " ");

                    for (int i = 0; i < data.getPopulation() / 10000; i++) {
                        System.out.print("*");
                    }

                    System.out.println();

                }

                return populationData;
                //System.out.println(municipalityData.toPrettyString());

                // System.out.println(populations.toPrettyString());

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return null;
    }


    private static HttpURLConnection connectToAPIAndSendPostRequest(ObjectMapper objectMapper, JsonNode jsonQuery)
            throws MalformedURLException, IOException, ProtocolException, JsonProcessingException {
        URL url = new URL("https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/synt/statfin_synt_pxt_12dy.px");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = objectMapper.writeValueAsBytes(jsonQuery);
            os.write(input, 0, input.length);
        }
        return con;
    }


    /**
     * In this method, we find all the municipality names and codes from the Json and put them into a HashMap,
     * so that we can get search for the municipality code by providing the municipality name
     *
     * @param areas
     * @return HashMap where municipality name is mapped to municipality code
     */
    private static HashMap<String, String> createMunicipalityNamesToCodesMap(JsonNode areas) {
        JsonNode codes = null;
        JsonNode names = null;

        // Here we find the element "variables", and inside it we have the element "text", that has value "Area".
        // Within the same element, we have the keys "values" which contains the municipality codes (e.g. KU123) as a list
        // and "valueTexts" which contains the municipality names (e.g. Lahti) as a list
        for (JsonNode node : areas.findValue("variables")) {
            if (node.findValue("text").asText().equals("Area")) {
                codes = node.findValue("values");
                names = node.findValue("valueTexts");
            }
        }

        // Let's store the municipality names as keys, and municipality codes as values in a HashMap

        HashMap<String, String> municipalityNamesToCodesMap = new HashMap<>();

        // Here we can assume that the size of names and codes are equal, at there are as many municipality codes
        // as there are municipality names
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i).asText();
            String code = codes.get(i).asText();
            municipalityNamesToCodesMap.put(name, code);

        }
        return municipalityNamesToCodesMap;
    }


    /**
     * Here we read the all the JSON from the URL to a JsonNode
     * <p>
     * How to improve this: instead of fetching the same data all over again when restarting the app, we could store
     * the areas JSON to a file and read it from there. Then we would only need to fetch it once, if the file does
     * not yet exist.
     *
     * @param objectMapper
     * @return JsonNode with municipality data
     */
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


}
