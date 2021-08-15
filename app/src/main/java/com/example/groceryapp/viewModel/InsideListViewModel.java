package com.example.groceryapp.viewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.groceryapp.DialogCloseListener;
import com.example.groceryapp.R;
import com.example.groceryapp.adapter.InsideListAdapter;
import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.model.ItemModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class InsideListViewModel extends AppCompatActivity implements DialogCloseListener {
    private List<ItemModel> itemsList;
    private RecyclerView insideListRecyclerView;
    private InsideListAdapter insideListAdapter;

    private FloatingActionButton addItemFloatingButton;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the layout
        setContentView(R.layout.activity_inside_list_view_model);
        getSupportActionBar().hide();

        // unpack the intent and set up the activity
        Intent i = getIntent();
        TextView listNameTextView = findViewById(R.id.listNameTextView);

        String listName = i.getStringExtra("listName");
        int listId = i.getIntExtra("listId", 0);

        listNameTextView.setText(listName);

        // add a new item button
        addItemFloatingButton = findViewById(R.id.addItemFloatingButton);
        addItemFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemFloatingButton.hide();
                Bundle bundle = new Bundle();
                bundle.putInt("LIST_ID", listId);
                AddNewItemViewModel fragment = new AddNewItemViewModel();
                fragment.setArguments(bundle);
                fragment.show(getSupportFragmentManager(), AddNewItemViewModel.TAG);
            }
        });

    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        addItemFloatingButton.show();
    }
}