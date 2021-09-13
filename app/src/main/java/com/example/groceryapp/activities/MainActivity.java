/**
 * @Author Pratik Magar 2241293
 **/

package com.example.groceryapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.groceryapp.DialogCloseListener;
import com.example.groceryapp.R;
import com.example.groceryapp.touchHelpers.MainActivityTouchHelper;
import com.example.groceryapp.adapters.MainActivityAdapter;
import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.models.ShoppingListModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {
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
        db = new DatabaseHandler(MainActivity.this);
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

        listOfShoppingLists = db.getAllLists(0);
        // reverse the order so the latest ones are at the top
        // Collections.reverse(listOfShoppingLists);
        mainActivityAdapter.setListOfShoppingLists(listOfShoppingLists);

        // adding a new list
        addFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFloatingButton.hide();
                AddNewListActivity.newInstance().show(getSupportFragmentManager(), AddNewListActivity.TAG);
            }
        });

        ConstraintLayout shoppingListButton = findViewById(R.id.shoppingListButton);
        shoppingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SavedListsActivity.class);
                MainActivity.this.startActivity(i);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        listOfShoppingLists = db.getAllLists(0);
        // reverse the order so the latest ones are at the top
        // Collections.reverse(listOfShoppingLists);
        mainActivityAdapter.setListOfShoppingLists(listOfShoppingLists);
        mainActivityAdapter.notifyDataSetChanged();
        addFloatingButton.show();
    }
}