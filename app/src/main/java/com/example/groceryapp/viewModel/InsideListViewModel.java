package com.example.groceryapp.viewModel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.groceryapp.R;

public class InsideListViewModel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_list_view_model);
        getSupportActionBar().hide();

        Intent i = getIntent();
        TextView listNameTextView = findViewById(R.id.listNameTextView);

        String listName = i.getStringExtra("listName");
        listNameTextView.setText(listName);
    }
}