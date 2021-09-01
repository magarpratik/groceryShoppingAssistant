package com.example.groceryapp.viewModel;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.DialogCloseListener;
import com.example.groceryapp.R;
import com.example.groceryapp.adapter.SavedListsAdapter;
import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.model.ShoppingListModel;
import com.example.groceryapp.touchHelper.SavedListsTouchHelper;

import java.util.ArrayList;
import java.util.List;

public class SavedListsViewModel extends AppCompatActivity implements DialogCloseListener {
    private List<ShoppingListModel> list;
    private RecyclerView recyclerView;
    private SavedListsAdapter adapter;

    private DatabaseHandler db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saved_lists);
        getSupportActionBar().hide();

        // return to main home screen
        ConstraintLayout homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SavedListsViewModel.this, MainActivityViewModel.class);
                SavedListsViewModel.this.startActivity(i);
            }
        });

        db = new DatabaseHandler(SavedListsViewModel.this);
        db.openDatabase();

        list = new ArrayList<>();

        recyclerView = findViewById(R.id.listRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SavedListsAdapter(this, db);
        recyclerView.setAdapter(adapter);

        list = db.getAllLists(1);
        adapter.setList(list);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SavedListsTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        list = db.getAllLists(1);
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }
}
