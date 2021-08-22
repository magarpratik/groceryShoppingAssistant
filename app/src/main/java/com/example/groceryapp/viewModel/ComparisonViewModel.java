package com.example.groceryapp.viewModel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.R;
import com.example.groceryapp.adapter.ComparisonAdapter;
import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.model.ShoppingListModel;

import java.util.ArrayList;

public class ComparisonViewModel extends AppCompatActivity {
    private String listName;
    private int listId;
    private RecyclerView recyclerView;
    private ComparisonAdapter adapter;
    private DatabaseHandler db;
    private ArrayList<ShoppingListModel> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the layout
        setContentView(R.layout.activity_comparison);
        getSupportActionBar().hide();

        // unpack the intent and set up the activity
        Intent i = getIntent();
        listName = i.getStringExtra("listName");
        listId = i.getIntExtra("listId", 0);

        // List name at the top of the screen
        TextView listNameTextView = findViewById(R.id.comparisonListNameTextView);
        listNameTextView.setText(listName);

        // Database
        db = new DatabaseHandler(this);
        db.openDatabase();

        // RecyclerView
        recyclerView = findViewById(R.id.comparisonRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Adapter
        adapter = new ComparisonAdapter(this, db);
        recyclerView.setAdapter(adapter);

        // Arraylist
        list = new ArrayList<>();
        for (int j = 0; j < 3; j++) {
            ShoppingListModel shoppingListModel = new ShoppingListModel("Testing");
            list.add(shoppingListModel);
        }
        adapter.setList(list);
    }
}
