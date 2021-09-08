package com.example.groceryapp.viewControllers;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.R;
import com.example.groceryapp.adapters.SelectItemAdapter;
import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.models.ItemModel;

import java.util.ArrayList;
import java.util.List;

public class SelectItemViewController extends AppCompatActivity {
    private DatabaseHandler db;
    private String listName;
    private int listId;
    private int storeId;
    private int itemId;
    private List<ItemModel> itemsList;
    private List<ItemModel> finalList;
    private int position;

    private RecyclerView recyclerView;
    private SelectItemAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the layout
        setContentView(R.layout.activity_select_item);
        getSupportActionBar().hide();

        // Database
        db = new DatabaseHandler(SelectItemViewController.this);
        db.openDatabase();

        // unpack the bundle
        // unpack the intent and set up the activity
        Bundle bundle = getIntent().getExtras();
        listName = bundle.getString("listName");
        listId = bundle.getInt("listId");
        storeId = bundle.getInt("storeId");
        itemId = bundle.getInt("itemId");
        itemsList = (List<ItemModel>) bundle.getSerializable("itemsList");
        // list being displayed
        finalList = (List<ItemModel>) bundle.getSerializable("comparisonList");
        position = bundle.getInt("position");


        TextView selectItemTextView = findViewById(R.id.selectItemTextView);
        selectItemTextView.setText(listName);

        // RecyclerView
        recyclerView = findViewById(R.id.selectItemRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Adapter
        adapter = new SelectItemAdapter(this, db, listName, storeId);
        recyclerView.setAdapter(adapter);

        // get the items from the database
        ArrayList<ItemModel> optionsList = new ArrayList<>();
        optionsList = db.getOptionsList(itemId, listId, storeId);


        adapter.setListOfItems(optionsList);
        adapter.setFinalList((ArrayList<ItemModel>) finalList);
        adapter.setItemPosition(position);
        adapter.setItemsList(itemsList);
    }
}
