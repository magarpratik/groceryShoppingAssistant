package com.example.groceryapp.viewModel;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.DialogCloseListener;
import com.example.groceryapp.R;
import com.example.groceryapp.adapter.InsideSavedListAdapter;
import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.model.ItemModel;
import com.example.groceryapp.touchHelper.InsideSavedListTouchHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InsideSavedListViewModel extends AppCompatActivity implements DialogCloseListener {
    private DatabaseHandler db;
    private RecyclerView recyclerView;
    private InsideSavedListAdapter adapter;
    private List<ItemModel> itemsList;

    private ImageView insideSavedImageView;
    private TextView basketPriceTextView;
    private TextView insideSavedTextView;
    private int listId;
    private int storeId;
    private FloatingActionButton addFloatingButton;

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        listId = i.getIntExtra("listId", 0);
        storeId = i.getIntExtra("storeId", 0);
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

        basketPriceTextView = findViewById(R.id.basketPriceTextView);

        int totalBasketPrice= 0;
        for (int j = 0; j < itemsList.size(); j++) {
            if (itemsList.get(j).isCrossed()) {
                android.icu.math.BigDecimal price = new android.icu.math.BigDecimal(itemsList.get(j).getPrice());
                android.icu.math.BigDecimal multiplier = new android.icu.math.BigDecimal("100");
                android.icu.math.BigDecimal res = price.multiply(multiplier);
                int number = res.intValue();
                totalBasketPrice = totalBasketPrice + number;
            }

            android.icu.math.BigDecimal answer = new android.icu.math.BigDecimal(String.valueOf(totalBasketPrice));
            android.icu.math.BigDecimal divider = new android.icu.math.BigDecimal("100");
            android.icu.math.BigDecimal res = answer.divide(divider, 2, android.icu.math.BigDecimal.ROUND_HALF_EVEN);

            basketPriceTextView.setText("£" + String.valueOf(res));
        }

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new InsideSavedListTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        addFloatingButton = findViewById(R.id.addSavedFloatingButton);
        // adding a new list
        addFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFloatingButton.hide();
                Bundle bundle = new Bundle();
                bundle.putString("ITEM_NAME", "");
                bundle.putInt("LIST_ID", listId);
                bundle.putInt("STORE_ID", storeId);
                AddNewSavedItemViewModel fragment = new AddNewSavedItemViewModel();
                fragment.setArguments(bundle);
                fragment.show(getSupportFragmentManager(), AddNewSavedItemViewModel.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        itemsList = db.getSavedItemsList(listId, storeId);
        adapter.setSavedItemsList(itemsList);
        adapter.notifyDataSetChanged();

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

        addFloatingButton.show();
    }
}

