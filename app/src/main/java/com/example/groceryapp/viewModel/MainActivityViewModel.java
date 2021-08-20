package com.example.groceryapp.viewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.groceryapp.DialogCloseListener;
import com.example.groceryapp.R;
import com.example.groceryapp.touchHelper.MainActivityTouchHelper;
import com.example.groceryapp.adapter.MainActivityAdapter;
import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.model.ShoppingListModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivityViewModel extends AppCompatActivity implements DialogCloseListener {
    private List<ShoppingListModel> listOfShoppingLists;
    private RecyclerView listRecyclerView;
    private MainActivityAdapter mainActivityAdapter;

    private FloatingActionButton addFloatingButton;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the layout
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        // Database
        db = new DatabaseHandler(MainActivityViewModel.this);
        db.openDatabase();

        // db.resetDatabase();

        // arraylist to hold the list of listNames
        listOfShoppingLists = new ArrayList<>();

        // RecyclerView
        listRecyclerView = findViewById(R.id.listRecyclerView);
        listRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Adapter
        mainActivityAdapter = new MainActivityAdapter(this, db);
        listRecyclerView.setAdapter(mainActivityAdapter);

        // add a new list button
        addFloatingButton = findViewById(R.id.addFloatingButton);

        // swipe functionality
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new MainActivityTouchHelper(mainActivityAdapter));
        itemTouchHelper.attachToRecyclerView(listRecyclerView);

        listOfShoppingLists = db.getAllLists();
        // reverse the order so the latest ones are at the top
        Collections.reverse(listOfShoppingLists);
        mainActivityAdapter.setListOfShoppingLists(listOfShoppingLists);



        // adding a new list
        addFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFloatingButton.hide();
                AddNewListViewModel.newInstance().show(getSupportFragmentManager(), AddNewListViewModel.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        listOfShoppingLists = db.getAllLists();
        // reverse the order so the latest ones are at the top
        Collections.reverse(listOfShoppingLists);
        mainActivityAdapter.setListOfShoppingLists(listOfShoppingLists);
        mainActivityAdapter.notifyDataSetChanged();
        addFloatingButton.show();
    }
}