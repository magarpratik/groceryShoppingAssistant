package com.example.groceryapp.viewModel;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.R;
import com.example.groceryapp.adapter.SavedListsAdapter;
import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.model.ShoppingListModel;

import java.util.List;

public class SavedListsViewModel extends AppCompatActivity {
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
    }
}
