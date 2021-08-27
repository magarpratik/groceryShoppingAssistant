package com.example.groceryapp.viewModel;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.R;
import com.example.groceryapp.adapter.ComparisonAdapter;
import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.model.ItemModel;
import com.example.groceryapp.model.ShoppingListModel;

import java.util.ArrayList;

public class ComparisonViewModel extends AppCompatActivity {
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
        adapter = new ComparisonAdapter(this, db, itemsList);
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
    }
}
