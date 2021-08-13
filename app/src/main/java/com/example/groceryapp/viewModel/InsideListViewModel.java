package com.example.groceryapp.viewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.groceryapp.R;
import com.example.groceryapp.adapter.InsideListAdapter;
import com.example.groceryapp.model.ItemModel;

import java.util.ArrayList;
import java.util.List;

public class InsideListViewModel extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<ItemModel> itemsList;
    InsideListAdapter adapter;
    // Spinner unitSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_list_view_model);
        getSupportActionBar().hide();

        /*unitSpinner = findViewById(R.id.unitSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.unit, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(adapter);

        unitSpinner.setOnItemSelectedListener(this);*/

        Intent i = getIntent();
        TextView listNameTextView = findViewById(R.id.listNameTextView);

        String listName = i.getStringExtra("listName");
        listNameTextView.setText(listName);
        
        //initData();
        //initRecyclerView();
    }

    /*private void initData() {
        itemsList = new ArrayList<>();
        itemsList.add(new ItemModel("Apples"));
        itemsList.add(new ItemModel("Apples"));
        itemsList.add(new ItemModel("Apples"));
        itemsList.add(new ItemModel("Apples"));
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.insideListRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new InsideListAdapter(itemsList);
        adapter.notifyDataSetChanged();
    }*/

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String choice = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}