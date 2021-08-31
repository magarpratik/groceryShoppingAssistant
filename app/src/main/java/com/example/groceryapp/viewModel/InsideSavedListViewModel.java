package com.example.groceryapp.viewModel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.R;
import com.example.groceryapp.adapter.InsideSavedListAdapter;
import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.model.ItemModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InsideSavedListViewModel extends AppCompatActivity {
    private DatabaseHandler db;
    private RecyclerView recyclerView;
    private InsideSavedListAdapter adapter;
    private List<ItemModel> itemsList;

    private ImageView insideSavedImageView;
    private TextView basketPriceTextView;
    private TextView insideSavedTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the layout
        setContentView(R.layout.activity_inside_saved_list);
        getSupportActionBar().hide();

        // Database
        db = new DatabaseHandler(InsideSavedListViewModel.this);
        db.openDatabase();

        Intent i = getIntent();
        int listId = i.getIntExtra("listId", 0);
        int storeId = i.getIntExtra("storeId", 0);
        String listName = i.getStringExtra("listName");

        // set the store logo
        insideSavedImageView = findViewById(R.id.insideSavedImageView);
        switch (storeId) {
            case 0:
                insideSavedImageView.setImageResource(R.drawable.asda_logo);
                break;
            case 1:
                insideSavedImageView.setImageResource(R.drawable.sainsburys_logo);
                break;
            case 2:
                insideSavedImageView.setImageResource(R.drawable.tesco_logo);
                break;
        }

        // set the list name
        insideSavedTextView = findViewById(R.id.insideSavedTextView);
        insideSavedTextView.setText(listName);

        itemsList = new ArrayList<>();
        itemsList = db.getSavedItemsList(listId, storeId);

        recyclerView = findViewById(R.id.insideSavedRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new InsideSavedListAdapter(this, db);
        recyclerView.setAdapter(adapter);
        adapter.setSavedItemsList(itemsList);

        // Calculate the total price
        TextView estimatedPriceTextView = findViewById(R.id.estimatedPriceTextView);


        int total = 0;
        // add the prices
        for (int j = 0; j < itemsList.size(); j++) {
            String price = itemsList.get(j).getPrice();
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
        estimatedPriceTextView.setText("£" + totalPrice);
    }
}
