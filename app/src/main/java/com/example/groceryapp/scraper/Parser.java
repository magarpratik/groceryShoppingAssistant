package com.example.groceryapp.scraper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Parser {
    private int counter = 0;
    ArrayList<String> resultArray = new ArrayList<>();

    public ArrayList<String> getKey(JSONObject jsonObject, String key) {
        // check if key exists in the JSON object
        boolean exists = jsonObject.has(key);
        Iterator<?> keys;
        String nextKeys;

        // Get info for top 5 search results only
        if (counter < 10) {
            // Go to the inner level of the JSON object if key not found on the outer level
            if (!exists) {
                keys = jsonObject.keys();
                while (keys.hasNext()) {
                    nextKeys = (String) keys.next();
                    try {
                        // check if key contains JSON object
                        if (jsonObject.get(nextKeys) instanceof JSONObject) {
                            getKey(jsonObject.getJSONObject(nextKeys), key);
                        }
                        // check if key contains JSON array
                        else if (jsonObject.get(nextKeys) instanceof JSONArray) {
                            JSONArray jsonArray = jsonObject.getJSONArray(nextKeys);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                // turn jsonArray to String
                                String jsonArrayString = jsonArray.get(i).toString();
                                // Convert jsonString to jsonObject
                                JSONObject innerJson = new JSONObject(jsonArrayString);
                                getKey(innerJson, key);
                            }
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
            else {
                resultArray.add(parseObject(jsonObject, key));
                counter++;
            }
        }
        return resultArray;
    }

    public String parseObject(JSONObject jsonObject, String key) {
        String result = null;
        try {
            result = jsonObject.get(key).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}