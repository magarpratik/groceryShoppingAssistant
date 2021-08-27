package com.example.groceryapp.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.R;
import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.model.ItemModel;
import com.example.groceryapp.model.ShoppingListModel;
import com.example.groceryapp.viewModel.ComparisonViewModel;

import java.util.ArrayList;

public class ComparisonAdapter extends RecyclerView.Adapter<ComparisonAdapter.ViewHolder> {
    private ArrayList<ShoppingListModel> list = new ArrayList<>();
    private ComparisonViewModel comparisonViewModel;
    private DatabaseHandler db;
    private ArrayList<ItemModel> itemsList;
    private ArrayList<String> prices = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ComparisonAdapter(ComparisonViewModel comparisonViewModel, DatabaseHandler db, ArrayList<ItemModel> itemsList) {
        this.comparisonViewModel = comparisonViewModel;
        this.db = db;
        this.itemsList = itemsList;
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView logoImageView;
        private TextView comparisonTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            logoImageView = itemView.findViewById(R.id.logoImageView);
            comparisonTextView = itemView.findViewById(R.id.comparisonItemTextView);
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
