package com.example.groceryapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.viewControllers.AddNewListViewController;
import com.example.groceryapp.viewControllers.InsideListViewController;
import com.example.groceryapp.viewControllers.MainActivityViewController;
import com.example.groceryapp.R;
import com.example.groceryapp.models.ShoppingListModel;

import java.util.ArrayList;
import java.util.List;

// RecyclerView Adapter for the list of lists
// Takes items from the shopping list and displays it in the RecyclerView
public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {
    private List<ShoppingListModel> listOfShoppingLists = new ArrayList<>();
    private MainActivityViewController mainActivity;
    private DatabaseHandler db;

    // Constructor
    public MainActivityAdapter(MainActivityViewController mainActivity, DatabaseHandler db) {
        this.mainActivity = mainActivity;
        this.db = db;
    }

    public Context getContext() { return mainActivity; }

    // Only knows about one object at a time
    // Takes an individual layout from the RecyclerView
    // and populates it with an object from the list
    // Responsible for generating view objects
    public class ViewHolder extends RecyclerView.ViewHolder {
        // declare view elements from the list_item.xml
        private TextView listNameTextView;
        private CardView listNameCardView;
        private TextView listNumberTextView;

        ViewHolder(View view) {
            super(view);
            listNameTextView = view.findViewById(R.id.listNameTextView);
            listNameCardView = view.findViewById(R.id.listNameCardView);
            listNumberTextView = view.findViewById(R.id.listNumberTextView);
        }
    }

    @NonNull
    @Override
    // Responsible for creating the individual view
    // This is the place to instantiate the view holder class
    public MainActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(itemView);
    }

    // MOST IMPORTANT
    // Responsible for mapping data from the original list to the RecyclerView
    @Override
    public void onBindViewHolder(@NonNull MainActivityAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.listNameTextView.setText(listOfShoppingLists.get(position).getName());
        holder.listNumberTextView.setText(String.valueOf(position + 1));

        // YOU CAN SET OnClickListeners FOR EVERY UI ELEMENT

        // EXAMPLE
        holder.listNameCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start a new activity when you click on an item
                Intent i = new Intent(getContext(), InsideListViewController.class);
                i.putExtra("listName", listOfShoppingLists.get(holder.getAdapterPosition()).getName());
                i.putExtra("listId", listOfShoppingLists.get(holder.getAdapterPosition()).getId());
                mainActivity.startActivity(i);

                // Toast.makeText(mainActivity, listOfShoppingLists.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfShoppingLists.size();
    }

    // Update the RecyclerView
    public void setListOfShoppingLists(List<ShoppingListModel> listOfShoppingLists) {
        this.listOfShoppingLists = listOfShoppingLists;
        // update the RecyclerView
        notifyDataSetChanged();
    }

    // delete an item and update the recyclerView
    public void deleteItem(int position) {
        ShoppingListModel item = listOfShoppingLists.get(position);
        db.deleteList(item.getId());
        listOfShoppingLists.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        ShoppingListModel item = listOfShoppingLists.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("ID", item.getId());
        bundle.putString("LIST_NAME", item.getName());
        AddNewListViewController fragment = new AddNewListViewController();
        fragment.setArguments(bundle);
        fragment.show(mainActivity.getSupportFragmentManager(), AddNewListViewController.TAG);
    }
}
