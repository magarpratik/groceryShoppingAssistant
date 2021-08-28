package com.example.groceryapp.viewModel;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.R;
import com.example.groceryapp.adapter.StoreAdapter;
import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.model.ItemModel;

import java.util.ArrayList;
import java.util.List;

public class StoreViewModel extends AppCompatActivity {
    private List<ItemModel> itemsList;
    private RecyclerView recyclerView;
    private StoreAdapter adapter;

    private Button chooseStore;
    private DatabaseHandler db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the layout
        setContentView(R.layout.activity_store);
        getSupportActionBar().hide();

        // Database
        db = new DatabaseHandler(StoreViewModel.this);
        db.openDatabase();

        itemsList = new ArrayList<>();

        recyclerView = findViewById(R.id.storeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //adapter = new StoreAdapter(this, db);
    }
}
