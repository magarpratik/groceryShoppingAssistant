package com.example.groceryapp.viewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.example.groceryapp.DialogCloseListener;
import com.example.groceryapp.R;
import com.example.groceryapp.adapter.MainActivityAdapter;
import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.model.ShoppingListModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivityViewModel extends AppCompatActivity implements DialogCloseListener {
    private RecyclerView listRecyclerView;
    private MainActivityAdapter mainActivityAdapter;
    private List<ShoppingListModel> listOfShoppingLists;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        // Database
        db = new DatabaseHandler(this);
        db.openDatabase();

        listOfShoppingLists = new ArrayList<>();

        // RecyclerView
        listRecyclerView = findViewById(R.id.listRecyclerView);
        listRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Adapter
        mainActivityAdapter = new MainActivityAdapter(this);
        listRecyclerView.setAdapter(mainActivityAdapter);

        listOfShoppingLists = db.getAllLists();
        // reverse the order so the latest ones are at the top
        Collections.reverse(listOfShoppingLists);
        mainActivityAdapter.setListOfShoppingLists(listOfShoppingLists);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        listOfShoppingLists = db.getAllLists();
        // reverse the order so the latest ones are at the top
        Collections.reverse(listOfShoppingLists);
        mainActivityAdapter.setListOfShoppingLists(listOfShoppingLists);
        mainActivityAdapter.notifyDataSetChanged();

    }
}