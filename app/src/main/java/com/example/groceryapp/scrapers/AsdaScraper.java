/**
 * @Author Pratik Magar 2241293
 **/

package com.example.groceryapp.scrapers;

import java.util.ArrayList;

public class AsdaScraper {
    /*public ArrayList<ArrayList<String>> scrape(String response) {
        final String itemName = "skuName";
        final String price = "price";
        final String weight = "weight";
        final String brandName = "brand";
        final String pricePerUnit = "pricePerUOM";
        String[] query = {itemName, price, weight, pricePerUnit};

        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();

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

        return finalResult;
    }*/

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