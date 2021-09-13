/**
 * @Author Pratik Magar 2241293
 **/

package com.example.groceryapp.activities;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.R;
import com.example.groceryapp.adapters.ComparisonAdapter;
import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.models.ItemModel;
import com.example.groceryapp.models.ShoppingListModel;

import java.util.ArrayList;

public class ComparisonActivity extends AppCompatActivity {
    private String listName;
    private int listId;
    private RecyclerView recyclerView;
    private ComparisonAdapter adapter;
    private DatabaseHandler db;
    private ArrayList<ShoppingListModel> list;
    private ArrayList<ItemModel> itemsList;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the layout
        setContentView(R.layout.activity_comparison);
        getSupportActionBar().hide();

        // unpack the intent and set up the activity
        Bundle bundle = getIntent().getExtras();
        itemsList = (ArrayList<ItemModel>) bundle.getSerializable("itemsList");
        listName = bundle.getString("listName");
        listId = bundle.getInt("listId");

        // List name at the top of the screen
        TextView listNameTextView = findViewById(R.id.comparisonListNameTextView);
        listNameTextView.setText(listName);

        // Database
        db = new DatabaseHandler(this);
        db.getReadableDB();

        // RecyclerView
        recyclerView = findViewById(R.id.comparisonRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Adapter
        adapter = new ComparisonAdapter(this, db, itemsList, listId, listName);
        recyclerView.setAdapter(adapter);

        // Arraylist
        list = new ArrayList<>();
        for (int j = 0; j < 3; j++) {
            ShoppingListModel shoppingListModel = new ShoppingListModel("Testing");
            list.add(shoppingListModel);
        }
        adapter.setList(list);

        Button hiddenButton = findViewById(R.id.hiddenButton);
        hiddenButton.setText("View Prices");

        hiddenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> prices = db.getComparisonPrices(itemsList);
                adapter.setPrices(prices);
            }
        });

        hiddenButton.setEnabled(false);


        // This is a temporary measure to compensate for the bug
        // BUG: If you press the hiddenButton too soon after this activity is opened,
        // the app crashes because the scraper() method will not have finished all the scraped entries
        // to the database.
        // The exception comes up because hiddenButton tries to access the database entry before
        // the scraper() finishes adding them to the database

        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                hiddenButton.setText("Wait for " + (millisUntilFinished / 1000) + " seconds");
            }

            public void onFinish() {
                hiddenButton.setEnabled(true);
                hiddenButton.setText("View Prices");
            }
        }.start();

        /*ArrayList<String> finalPrices = new ArrayList<>();

        for (int j = 0; j < 3; j++) {
            int storeId = j;
            ArrayList<ItemModel> pricesList = new ArrayList<>();
            pricesList = db.getComparisonList(itemsList, listId, storeId);

            int total = 0;

            // add the prices
            for (int i = 0; i < pricesList.size(); i++) {
                String price = pricesList.get(i).getPrice();
                BigDecimal num = new BigDecimal(price.trim());
                BigDecimal multiplier = new BigDecimal("100");
                BigDecimal res = num.multiply(multiplier);
                total = total + res.intValue();
            }

            BigDecimal num = new BigDecimal(String.valueOf(total));
            BigDecimal divisor = new BigDecimal("100");
            BigDecimal result = num.divide(divisor, 2, BigDecimal.ROUND_HALF_EVEN);

            String totalPrice = String.valueOf(result);
            finalPrices.add(totalPrice);
        }

        adapter.setPrices(finalPrices);*/
    }
}
