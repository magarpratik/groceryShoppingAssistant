package com.example.groceryapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.R;
import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.model.ItemModel;
import com.example.groceryapp.model.ShoppingListModel;
import com.example.groceryapp.viewModel.ComparisonViewModel;
import com.example.groceryapp.viewModel.StoreViewModel;

import java.io.Serializable;
import java.util.ArrayList;

public class ComparisonAdapter extends RecyclerView.Adapter<ComparisonAdapter.ViewHolder> {
    private ArrayList<ShoppingListModel> list = new ArrayList<>();
    private ComparisonViewModel comparisonViewModel;
    private DatabaseHandler db;
    private ArrayList<ItemModel> itemsList;
    private ArrayList<String> prices = new ArrayList<>();
    private int listId;
    private String listName;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ComparisonAdapter(ComparisonViewModel comparisonViewModel, DatabaseHandler db, ArrayList<ItemModel> itemsList, int listId, String listName) {
        this.comparisonViewModel = comparisonViewModel;
        this.db = db;
        this.itemsList = itemsList;
        this.listId = listId;
        this.listName = listName;
        prices.add("0");
        prices.add("0");
        prices.add("0");
    }

    public Context getContext() { return comparisonViewModel; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comparison_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Set logos and total prices for each store in the holders
        holder.comparisonTextView.setText("£" + prices.get(position));
        switch (position) {
            case 0:
                holder.logoImageView.setImageResource(R.drawable.asda_logo);
                break;
            case 1:
                holder.logoImageView.setImageResource(R.drawable.sainsburys_logo);
                break;
            case 2:
                holder.logoImageView.setImageResource(R.drawable.tesco_logo);
                break;
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the items list when you click the store
                // start a new activity when you click on the item
                Intent intent = new Intent(comparisonViewModel, StoreViewModel.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("itemsList", (Serializable) itemsList);
                bundle.putString("listName", listName);
                bundle.putInt("listId", listId);
                bundle.putInt("storeId", holder.getAdapterPosition());
                ArrayList<ItemModel> comparisonList = new ArrayList<>();
                comparisonList = db.getComparisonList(itemsList, listId, holder.getAdapterPosition());
                bundle.putSerializable("comparisonList", (Serializable) comparisonList);


                intent.putExtras(bundle);
                comparisonViewModel.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // declare UI elements
        private ImageView logoImageView;
        private TextView comparisonTextView;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            logoImageView = itemView.findViewById(R.id.logoImageView);
            comparisonTextView = itemView.findViewById(R.id.comparisonItemTextView);
            cardView = itemView.findViewById(R.id.storeCardView);
        }
    }

    public void setList(ArrayList<ShoppingListModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setPrices(ArrayList<String> prices) {
        this.prices = prices;
        notifyDataSetChanged();
    }
}
