package com.example.groceryapp.viewModel;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

    private String listName;
    private int listId;
    private int storeId;
    private TextView storeTextView;
    private ImageView storeImageView;

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

        // unpack the intent and set up the activity
        Bundle bundle = getIntent().getExtras();
        itemsList = (ArrayList<ItemModel>) bundle.getSerializable("itemsList");
        listName = bundle.getString("listName");
        listId = bundle.getInt("listId");
        storeId = bundle.getInt("storeId");

        storeTextView = findViewById(R.id.storeTextView);
        storeTextView.setText(listName);

        storeImageView = findViewById(R.id.storeImageView);

        switch (storeId) {
            case 0:
                storeImageView.setImageResource(R.drawable.asda_logo);
                break;
            case 1:
                storeImageView.setImageResource(R.drawable.sainsburys_logo);
                break;
            case 2:
                storeImageView.setImageResource(R.drawable.tesco_logo);
                break;
        }
    }
}
