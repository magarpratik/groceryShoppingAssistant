package com.example.groceryapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.MainActivity;
import com.example.groceryapp.R;
import com.example.groceryapp.model.ShoppingListModel;

import java.util.ArrayList;
import java.util.List;


// Takes items from the shopping list and displays it in the RecyclerView
public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {
    private List<ShoppingListModel> listOfShoppingLists = new ArrayList<>();
    private MainActivity mainActivity;

    // Constructor
    public MainActivityAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    // Only knows about one object at a time
    // Takes an individual layout from the RecyclerView
    // and populates it with an object from the list
    // Responsible for generating view objects
    public class ViewHolder extends RecyclerView.ViewHolder {
        // declare view elements from the list_item.xml
        private TextView listNameTextView;
        private CardView listNameCardView;

        ViewHolder(View view) {
            super(view);
            listNameTextView = view.findViewById(R.id.listNameTextView);
            listNameCardView = view.findViewById(R.id.listNameCardView);
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
    public void onBindViewHolder(@NonNull MainActivityAdapter.ViewHolder holder, int position) {
        holder.listNameTextView.setText(listOfShoppingLists.get(position).getName());

        // YOU CAN SET OnClickListeners FOR EVERY UI ELEMENT

        // EXAMPLE
        /*holder.listNameCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mainActivity, listOfShoppingLists.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return listOfShoppingLists.size();
    }

    // Update the RecyclerView
    public void setListOfShoppingLists(List<ShoppingListModel> listOfShoppingLists) {
        this.listOfShoppingLists = listOfShoppingLists;
        notifyDataSetChanged();
    }
}
