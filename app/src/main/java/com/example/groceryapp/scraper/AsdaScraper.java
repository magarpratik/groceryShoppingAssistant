package com.example.groceryapp.scraper;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groceryapp.model.ItemModel;
import com.example.groceryapp.viewModel.InsideListViewModel;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

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