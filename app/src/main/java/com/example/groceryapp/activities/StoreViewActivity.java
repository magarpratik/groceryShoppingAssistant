/**
 * @Author Pratik Magar 2241293
 **/

package com.example.groceryapp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.R;
import com.example.groceryapp.adapters.StoreAdapter;
import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.models.ItemModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StoreViewActivity extends AppCompatActivity {
    private List<ItemModel> itemsList;
    private RecyclerView recyclerView;
    private StoreAdapter adapter;

    private Button chooseStore;
    private DatabaseHandler db;

    private String listName;
    private int listId;
    private int storeId;
    private TextView storeTextView;
    private ImageView storeImageView;
    private ArrayList<ItemModel> comparisonList;
    private TextView totalPriceTextView;
    private Button selectStoreButton;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the layout
        setContentView(R.layout.activity_store);
        getSupportActionBar().hide();

        // Database
        db = new DatabaseHandler(StoreViewActivity.this);
        db.openDatabase();

        itemsList = new ArrayList<>();

        // RecyclerView
        recyclerView = findViewById(R.id.storeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // unpack the intent and set up the activity
        Bundle bundle = getIntent().getExtras();
        itemsList = (ArrayList<ItemModel>) bundle.getSerializable("itemsList");
        listName = bundle.getString("listName");
        listId = bundle.getInt("listId");
        storeId = bundle.getInt("storeId");
        comparisonList = (ArrayList<ItemModel>) bundle.getSerializable("comparisonList");

        storeTextView = findViewById(R.id.storeTextView);
        storeTextView.setText(listName);

        // store logo
        storeImageView = findViewById(R.id.storeImageView);
        switch (storeId) {
            case 0:
                storeImageView.setImageResource(R.drawable.asda_logo);
                break;
            case 1:
                storeImageView.setImageResource(R.drawable.sainsburys_logo);
                break;
            case 2:
                storeImageView.setImageResource(R.drawable.tesco_logo);
                break;
        }

        // Adapter
        adapter = new StoreAdapter(this, db, listName, storeId);
        recyclerView.setAdapter(adapter);

        // get the comparison list from the database
        //comparisonList = db.getComparisonList(itemsList, listId, storeId);

        // add list to database
        /*for (int i = 0; i < comparisonList.size(); i++) {
            db.addFinalItem(comparisonList.get(i));
        }*/

        adapter.setFinalList(comparisonList);
        adapter.setItemsList(itemsList);

        // Calculate the total price
        totalPriceTextView = findViewById(R.id.totalPriceTextView);

        int total = 0;
        // add the prices
        for (int i = 0; i < comparisonList.size(); i++) {
            String price = comparisonList.get(i).getPrice();
            BigDecimal num = new BigDecimal(price.trim());
            BigDecimal multiplier = new BigDecimal("100");
            BigDecimal res = num.multiply(multiplier);
            total = total + res.intValue();
        }

        BigDecimal num = new BigDecimal(String.valueOf(total));
        BigDecimal divisor = new BigDecimal("100");
        BigDecimal result = num.divide(divisor, 2, BigDecimal.ROUND_HALF_EVEN);
        total = 0;

        String totalPrice = String.valueOf(result);
        totalPriceTextView.setText("??" + totalPrice);

        selectStoreButton = findViewById(R.id.selectStoreButton);
        selectStoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change isSaved to 1
                db.saveList(listId, storeId);
                // delete old unneeded items
                db.deleteOldItems(listId);
                for (int i = 0; i < comparisonList.size(); i++) {
                    db.addFinalItem(comparisonList.get(i));
                }
                Intent intent = new Intent(StoreViewActivity.this, SavedListsActivity.class);
                StoreViewActivity.this.startActivity(intent);
            }
        });
    }
}
