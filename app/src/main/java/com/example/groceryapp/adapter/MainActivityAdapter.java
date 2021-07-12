package com.example.groceryapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.MainActivity;
import com.example.groceryapp.R;
import com.example.groceryapp.model.ShoppingListModel;

import java.util.List;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {

    // Takes items from the shopping list and displays it in the RecyclerView

    private List<ShoppingListModel> listOfShoppingLists;
    private MainActivity mainActivity;

    public MainActivityAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    // Only knows about one object at a time
    // Takes an individual layout from the RecyclerView
    // and populates it with an object from the list
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView listNameTextView;

        ViewHolder(View view) {
            super(view);
            listNameTextView = view.findViewById(R.id.listNameTextView);
        }
    }

    @NonNull
    @Override
    // Responsible for creating the individual view
    public MainActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list, parent, false);
        return new ViewHolder(itemView);
    }

    // Responsible for mapping data from the original list to the RecyclerView
    @Override
    public void onBindViewHolder(@NonNull MainActivityAdapter.ViewHolder holder, int position) {
        ShoppingListModel list = listOfShoppingLists.get(position);
        holder.listNameTextView.setText(list.getName());
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
