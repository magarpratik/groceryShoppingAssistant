/**
 * @Author Pratik Magar 2241293
 **/

package com.example.groceryapp.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groceryapp.DialogCloseListener;
import com.example.groceryapp.R;
import com.example.groceryapp.adapters.InsideListAdapter;
import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.models.ItemModel;
import com.example.groceryapp.scrapers.SainsburysScraper;
import com.example.groceryapp.scrapers.TescoScraper;
import com.example.groceryapp.touchHelpers.InsideListTouchHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// This class uses Jsoup library
/*
The jsoup code-base (including source and compiled packages) are distributed under the open source MIT license as described below.

The MIT License
Copyright © 2009 - 2021 Jonathan Hedley (https://jsoup.org/)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

// Volley library is also used   https://github.com/google/volley
/*
google/volley is licensed under the

Apache License 2.0
A permissive license whose main conditions require preservation of copyright and license notices. Contributors provide an express grant of patent rights. Licensed works, modifications, and larger works may be distributed under different terms and without source code.

*/

public class InsideListActivity extends AppCompatActivity implements DialogCloseListener {
    private List<ItemModel> itemsList;
    private RecyclerView insideListRecyclerView;
    private InsideListAdapter insideListAdapter;
    private InsideListActivity insideListActivity = this;

    private FloatingActionButton addItemFloatingButton;
    private Button comparePricesButton;
    private DatabaseHandler db;
    private int listId;
    private String listName;
    private TextView textView;
    private TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the layout
        setContentView(R.layout.activity_inside_list_view_model);
        getSupportActionBar().hide();

        // Database
        db = new DatabaseHandler(InsideListActivity.this);
        db.openDatabase();

        // unpack the intent and set up the activity
        Intent i = getIntent();
        listName = i.getStringExtra("listName");
        listId = i.getIntExtra("listId", 0);

        TextView listNameTextView = findViewById(R.id.listNameTextView);
        listNameTextView.setText(listName);

        // RecyclerView
        insideListRecyclerView = findViewById(R.id.insideListRecyclerView);
        insideListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Adapter
        insideListAdapter = new InsideListAdapter(this, db);
        insideListRecyclerView.setAdapter(insideListAdapter);

        // add a new item button
        addItemFloatingButton = findViewById(R.id.addItemFloatingButton);
        comparePricesButton = findViewById(R.id.comparePricesButton);

        // list of items on the list
        itemsList = new ArrayList<>();
        itemsList = db.getListOfItems(listId);
        insideListAdapter.setItemsList(itemsList);

        textView = findViewById(R.id.textView3);
        textView1 = findViewById(R.id.textView4);
        if (itemsList.size() == 0) {
            textView.setVisibility(View.INVISIBLE);
            textView1.setVisibility(View.INVISIBLE);
            comparePricesButton.setVisibility(View.INVISIBLE);
        }

        // swipe functionality
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new InsideListTouchHelper(insideListAdapter));
        itemTouchHelper.attachToRecyclerView(insideListRecyclerView);

        addItemFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemFloatingButton.hide();
                comparePricesButton.setVisibility(View.INVISIBLE);
                Bundle bundle = new Bundle();
                bundle.putInt("LIST_ID", listId);
                bundle.putString("ITEM_NAME", "");
                AddNewItemActivity fragment = new AddNewItemActivity();
                fragment.setArguments(bundle);
                fragment.show(getSupportFragmentManager(), AddNewItemActivity.TAG);
            }
        });



        comparePricesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.clearResults(listId);

                scraper();

                // start a new activity when you click on an item
                Intent intent = new Intent(insideListActivity, ComparisonActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("itemsList", (Serializable) itemsList);
                bundle.putString("listName", listName);
                bundle.putInt("listId", listId);

                intent.putExtras(bundle);
                insideListActivity.startActivity(intent);
            }
        });
    }

    public void hideUI() {
        textView = findViewById(R.id.textView3);
        textView1 = findViewById(R.id.textView4);
        comparePricesButton = findViewById(R.id.comparePricesButton);

        textView.setVisibility(View.INVISIBLE);
        textView1.setVisibility(View.INVISIBLE);
        comparePricesButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        itemsList = db.getListOfItems(listId);
        insideListAdapter.setItemsList(itemsList);
        insideListAdapter.notifyDataSetChanged();
        addItemFloatingButton.show();
        if (itemsList.size() != 0) {
            comparePricesButton.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            textView1.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sorter(ArrayList<ArrayList<String>> result, int listId, int itemId, int storeId) {

        // create itemModels from the results
        for (int i = 0; i < result.size(); i++) {
            ItemModel itemModel = new ItemModel(listId, result.get(i).get(0));
            itemModel.setStoreId(storeId);
            itemModel.setId(itemId);
            itemModel.setPrice(result.get(i).get(1));
            itemModel.setQuantity(result.get(i).get(2));
            itemModel.setPricePerUnit(result.get(i).get(3));
            itemModel.extractPPU(result.get(i).get(3));
            db.addResults(itemModel);
        }
    }

    public void scraper() {
        // Volley request queue
        RequestQueue requestQueue = Volley.newRequestQueue(insideListActivity);

        // Asda scraper
        for (int i = 0; i < itemsList.size(); i++) {
            int itemId = itemsList.get(i).getId();
            String url = "https://groceries.asda.com/cmscontent/v2/items/autoSuggest?requestorigin=gi&searchTerm="
                    + itemsList.get(i).getName();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject jsonObject = response.getJSONObject("payload");
                                JSONArray items = jsonObject.getJSONArray("autoSuggestionItems");

                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject item = items.getJSONObject(i);
                                    String name = item.getString("skuName");
                                    String price = item.getString("price");
                                    String weight = item.getString("weight");
                                    String pricePerUnit = item.getString("pricePerUOM");

                                    ItemModel itemModel = new ItemModel(listId, name);
                                    // StoreId for ASDA = 0
                                    itemModel.setStoreId(0);
                                    itemModel.setPrice(price.replace("£", ""));
                                    itemModel.setQuantity(weight);
                                    itemModel.setPricePerUnit(pricePerUnit);
                                    itemModel.extractPPU(pricePerUnit);
                                    itemModel.setId(itemId);
                                    db.addResults(itemModel);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(InsideListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(request);
        }

        // Sainsbury's scraper
        for (int i = 0; i < itemsList.size(); i++) {
            int itemId = itemsList.get(i).getId();

            String url = "https://www.sainsburys.co.uk/groceries-api/gol-services/product/v1/product?filter[keyword]="
                    + itemsList.get(i).getName();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onResponse(JSONObject response) {
                            ArrayList<ArrayList<String>> finalResult = new ArrayList<ArrayList<String>>();
                            SainsburysScraper scraper = new SainsburysScraper();
                            finalResult = scraper.scrape(response.toString());
                            sorter(finalResult, listId, itemId, 1);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            requestQueue.add(request);
        }

        // Tesco Scraper
        for (int i = 0; i < itemsList.size(); i++) {
            int itemId = itemsList.get(i).getId();

            String url = "https://www.tesco.com/groceries/en-GB/search?query=" + itemsList.get(i).getName();

            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onResponse(String response) {
                            Document document = Jsoup.parse(response);

                            TescoScraper tescoScraper = new TescoScraper();
                            ArrayList<ArrayList<String>> finalResult = tescoScraper.scrape(document);
                            sorter(finalResult, listId, itemId, 2);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            requestQueue.add(request);
        }
    }
}