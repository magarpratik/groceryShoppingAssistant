package com.example.groceryapp.scraper;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SainsburysScraper {
    public ArrayList<ArrayList<String>> scrape(String response) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<String> finalPriceList = new ArrayList<String >();
        ArrayList<String> finalPricePerUnitList = new ArrayList<String>();
        ArrayList<String> finalNameList = new ArrayList<String>();
        ArrayList<String> finalBrandList = new ArrayList<String>();
        ArrayList<String> finalQuantityList = new ArrayList<String>();
        ArrayList<String> finalUnitList = new ArrayList<String>();

        Parser parser = new Parser();

        // extract price
        String retail_price = parser.getKey(jsonObject, "retail_price").toString().replace("[", "{\"retail_price\": [").replace("]", "]}");
        parser.setCounter(0);
        parser.resultArray.clear();
        JSONObject jsonObjRetailPrice = null;
        try {
            jsonObjRetailPrice = new JSONObject(retail_price);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList<String> priceList = parser.getKey(jsonObjRetailPrice, "price");
        for (String s: priceList) {
            finalPriceList.add(s);
        }
        parser.setCounter(0);
        parser.resultArray.clear();

        // extract price per unit
        String unit_price = parser.getKey(jsonObject, "unit_price").toString().replace("[", "{\"unit_price\": [").replace("]", "]}");
        parser.setCounter(0);
        parser.resultArray.clear();
        JSONObject jsonObjUnitPrice = null;
        try {
            jsonObjUnitPrice = new JSONObject(unit_price);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList<String> pricePerUnitList = parser.getKey(jsonObjUnitPrice, "price");
        for (String s: pricePerUnitList) {
            finalPricePerUnitList.add(s);
        }
        parser.setCounter(0);
        parser.resultArray.clear();
        ArrayList<String> unitList = parser.getKey(jsonObjUnitPrice, "measure");
        for (String s: unitList) {
            finalUnitList.add(s);
        }
        parser.setCounter(0);
        parser.resultArray.clear();

        // extract dirty names list
        ArrayList<String> namesList = parser.getKey(jsonObject, "name");

        // extract quantity
        for(String s: namesList) {
            // break up the string
            String[] splitString = s.split(" ");
            // look if there is extra info in brackets at the end of the String

            String quantity;
            if(splitString[splitString.length - 1].endsWith(")")) {
                quantity = splitString[splitString.length - 3];
            }
            else {
                quantity = splitString[splitString.length - 1];
            }
            finalQuantityList.add(quantity);
        }

        // extract name
        for(String s: namesList) {
            // break up the string
            String[] splitString = s.split(" ");
            // look if there is extra info in brackets at the end of the String

            StringBuilder itemName = new StringBuilder();

            // check if there is extra info at the end of the string
            if(splitString[splitString.length - 1].endsWith(")")) {
                for (int i = 0; i < (splitString.length - 3); i++) {
                    itemName.append(splitString[i]);
                    if(i != (splitString.length - 4)) {
                        itemName.append(" ");
                    }
                }
            }
            else {
                for (int i = 0; i < (splitString.length - 1); i++) {
                    itemName.append(splitString[i]);
                    if(i != (splitString.length - 2)) {
                        itemName.append(" ");
                    }
                }
            }
            finalNameList.add(itemName.toString().replace("Sainsbury's ", "").replace("British ", ""));
        }

        // extract brand
        parser.setCounter(0);
        parser.resultArray.clear();
        /*ArrayList<String> brandList = parser.getKey(jsonObject, "brand");
        for (String s: brandList) {
            finalBrandList.add(s.replace("[\"", "").replace("\"]", ""));
        }
        parser.setCounter(0);
        parser.resultArray.clear();*/

        ArrayList<String> finalCombinedPricePerUnitList = new ArrayList<String>();
        for (int i = 0; i < finalPricePerUnitList.size(); i++) {
            finalCombinedPricePerUnitList.add(finalPricePerUnitList.get(i) + "/" + finalUnitList.get(i));
        }

        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        result.add(finalNameList);
        result.add(finalPriceList);
        result.add(finalQuantityList);
        // result.add(finalBrandList);
        result.add(finalCombinedPricePerUnitList);

        ArrayList<ArrayList<String>> finalResult = AsdaScraper.resultSorter(result);
        return finalResult;
    }
}
