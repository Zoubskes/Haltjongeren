package com.example.deelproduct21.controller;

import com.example.deelproduct21.model.DatasetEntry;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.control.ComboBox;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DatasetController {

    public List<DatasetEntry> fetchDatasetEntries(String datasetName) {
        List<DatasetEntry> datasetEntries = new ArrayList<>();
        try {
            // URL of the dataset
            URL url = new URL("https://opendata.cbs.nl/ODataApi/odata/71930ned/" + datasetName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder jsonString = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                jsonString.append(output);
            }
            conn.disconnect();

            // Parse the JSON string to create a JsonObject
            JsonObject jsonObject = JsonParser.parseString(jsonString.toString()).getAsJsonObject();
            JsonArray valueArray = jsonObject.getAsJsonArray("value");

            for (JsonElement element : valueArray) {
                JsonObject obj = element.getAsJsonObject();
                JsonElement keyElement = obj.get("Key");
                JsonElement titleElement = obj.get("Title");

                if (keyElement != null && titleElement != null) {
                    String key = keyElement.getAsString();
                    String title = titleElement.getAsString();
                    datasetEntries.add(new DatasetEntry(key, title));
                } else {
                    System.out.println("Key or Title is null for an element");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datasetEntries;
    }
    public void populateComboBox(ComboBox<DatasetEntry> comboBox, String datasetName) {
        List<DatasetEntry> datasetEntries = fetchDatasetEntries(datasetName);
        if (datasetEntries.size() > 2) {
            comboBox.getItems().addAll(datasetEntries.subList(1, datasetEntries.size() - 1));
        }
    }
}