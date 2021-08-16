package com.example.groceryapp.scraper;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AsdaScraper {

    public void scrape(String searchTerm) {
        final String itemName = "skuName";
        final String price = "price";
        final String weight = "weight";
        final String brandName = "brand";
        final String pricePerUnit = "pricePerUOM";
        String[] query = {itemName, price, weight, pricePerUnit};

        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();

        // Get the JSON data
        String response = null;
        try {
            response = Unirest.get("https://groceries.asda.com/cmscontent/v2/items/autoSuggest?requestorigin=gi&searchTerm=" + searchTerm.replace(" ", "%20")).asString().getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Get the data from the JSONObject
        for (String q: query) {
            Parser parser = new Parser();
            ArrayList<String> tempArray = parser.getKey(jsonObject, q);
            result.add(tempArray);
        }

        ArrayList<ArrayList<String>> finalResult = resultSorter(result);

        System.out.println(finalResult);
    }

    public static ArrayList<ArrayList<String>> resultSorter(ArrayList<ArrayList<String>> result) {
        ArrayList<ArrayList<String>> finalResult = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < result.get(0).size(); i++) {
            ArrayList<String> singleItemDetails = new ArrayList<String>();
            for (int j = 0; j < result.size(); j++) {
                singleItemDetails.add(result.get(j).get(i).replace("£",""));
            }
            finalResult.add(singleItemDetails);
        }
        return finalResult;
    }
}