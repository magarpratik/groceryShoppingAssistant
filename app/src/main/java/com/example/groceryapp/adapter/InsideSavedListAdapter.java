package com.example.groceryapp.adapter;

import android.icu.math.BigDecimal;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.R;
import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.model.ItemModel;
import com.example.groceryapp.viewModel.InsideSavedListViewModel;

import java.util.ArrayList;
import java.util.List;

public class InsideSavedListAdapter extends RecyclerView.Adapter<InsideSavedListAdapter.ViewHolder> {
    private InsideSavedListViewModel insideSavedListViewModel;
    private DatabaseHandler db;
    private List<ItemModel> itemsList;

    public InsideSavedListAdapter(InsideSavedListViewModel insideSavedListViewModel, DatabaseHandler db) {
        this.insideSavedListViewModel = insideSavedListViewModel;
        this.db = db;
    }

    @NonNull
    @Override
    public InsideSavedListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_item, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull InsideSavedListAdapter.ViewHolder holder, int position) {
        holder.numberTextView.setText(String.valueOf(position + 1));
        holder.itemNameTextView.setText(itemsList.get(position).getName());
        holder.weightTextView.setText(itemsList.get(position).getWeight());
        holder.pricePerUnitTextView.setText(itemsList.get(position).getPricePerUnit());
        BigDecimal price = new BigDecimal(itemsList.get(position).getPrice());
        BigDecimal multiply = price.multiply(new BigDecimal("100"));
        BigDecimal result = multiply.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_EVEN);
        holder.itemPriceTextView.setText("£" + result);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView numberTextView;
        private TextView itemNameTextView;
        private TextView weightTextView;
        private TextView pricePerUnitTextView;
        private TextView itemPriceTextView;
        private TextView totalPriceTextView;
        private CardView storeItemCardView;
        private TextView storeTextView;
        public ViewHolder(@NonNull View view) {
            super(view);
            numberTextView = view.findViewById(R.id.numberTextView);
            itemNameTextView = view.findViewById(R.id.itemNameTextView1);
            weightTextView = view.findViewById(R.id.weightTextView);
            pricePerUnitTextView = view.findViewById(R.id.pricePerUnitTextView);
            itemPriceTextView = view.findViewById(R.id.itemPriceTextView);
            totalPriceTextView = view.findViewById(R.id.totalPriceTextView);
            storeItemCardView = view.findViewById(R.id.storeItemCardView);
            storeTextView = view.findViewById(R.id.storeTextView);
        }
    }

    public void setSavedItemsList(List<ItemModel> itemsList) {
        this.itemsList = itemsList;
        notifyDataSetChanged();
    }
}
