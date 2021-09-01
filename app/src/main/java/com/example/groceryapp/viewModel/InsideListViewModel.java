package com.example.groceryapp.viewModel;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import com.example.groceryapp.adapter.InsideListAdapter;
import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.model.ItemModel;
import com.example.groceryapp.scraper.AsdaScraper;
import com.example.groceryapp.scraper.SainsburysScraper;
import com.example.groceryapp.scraper.TescoScraper;
import com.example.groceryapp.touchHelper.InsideListTouchHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InsideListViewModel extends AppCompatActivity implements DialogCloseListener {
    private List<ItemModel> itemsList;
    private RecyclerView insideListRecyclerView;
    private InsideListAdapter insideListAdapter;
    private InsideListViewModel insideListViewModel = this;

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
        db = new DatabaseHandler(InsideListViewModel.this);
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
                AddNewItemViewModel fragment = new AddNewItemViewModel();
                fragment.setArguments(bundle);
                fragment.show(getSupportFragmentManager(), AddNewItemViewModel.TAG);
            }
        });



        comparePricesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.clearResults(listId);

                scraper();

                // start a new activity when you click on an item
                Intent intent = new Intent(insideListViewModel, ComparisonViewModel.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("itemsList", (Serializable) itemsList);
                bundle.putString("listName", listName);
                bundle.putInt("listId", listId);

                intent.putExtras(bundle);
                insideListViewModel.startActivity(intent);
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
        RequestQueue requestQueue = Volley.newRequestQueue(insideListViewModel);

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
                    Toast.makeText(InsideListViewModel.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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