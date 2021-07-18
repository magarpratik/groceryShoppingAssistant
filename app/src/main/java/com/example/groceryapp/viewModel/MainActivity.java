package com.example.groceryapp.viewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.groceryapp.R;
import com.example.groceryapp.adapter.MainActivityAdapter;
import com.example.groceryapp.model.ShoppingListModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView listRecyclerView;
    private MainActivityAdapter mainActivityAdapter;
    private List<ShoppingListModel> listOfShoppingLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        listOfShoppingLists = new ArrayList<>();

        // RecyclerView
        listRecyclerView = findViewById(R.id.listRecyclerView);
        listRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Adapter
        mainActivityAdapter = new MainActivityAdapter(this);
        listRecyclerView.setAdapter(mainActivityAdapter);

        ShoppingListModel list = new ShoppingListModel("Grocery");

        listOfShoppingLists.add(list);
        listOfShoppingLists.add(list);
        listOfShoppingLists.add(list);
        listOfShoppingLists.add(list);
        listOfShoppingLists.add(list);

        mainActivityAdapter.setListOfShoppingLists(listOfShoppingLists);
    }
}