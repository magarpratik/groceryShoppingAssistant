package com.example.groceryapp.adapter;

import android.icu.math.BigDecimal;
import android.os.Build;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.R;
import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.model.ItemModel;
import com.example.groceryapp.viewModel.StoreViewModel;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {
    private StoreViewModel storeViewModel;
    private DatabaseHandler db;
    private List<ItemModel> finalList;
    private List<ItemModel> itemsList;
    private boolean isOnTextChanged = false;
    private BigDecimal totalPrice = new BigDecimal("0");

    public StoreAdapter(StoreViewModel storeViewModel, DatabaseHandler db) {
        this.storeViewModel = storeViewModel;
        this.db = db;
    }

    public void setFinalList(List<ItemModel> finalList) {
        this.finalList = finalList;
        notifyDataSetChanged();
    }

    public void setItemsList(List<ItemModel> itemsList) {
        this.itemsList = itemsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreAdapter.ViewHolder holder, int position) {
        holder.numberTextView.setText(String.valueOf(position + 1));
        holder.itemNameTextView.setText(finalList.get(position).getName());
        holder.weightTextView.setText(finalList.get(position).getWeight());
        holder.pricePerUnitTextView.setText(finalList.get(position).getPricePerUnit());
        holder.itemPriceTextView.setText("£" + finalList.get(position).getPrice());

        // Sum total of the price

    }

    @Override
    public int getItemCount() {
        return finalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView numberTextView;
        private TextView itemNameTextView;
        private TextView weightTextView;
        private TextView pricePerUnitTextView;
        private TextView itemPriceTextView;
        private TextView totalPriceTextView;

        public ViewHolder(@NonNull View view) {
            super(view);
            numberTextView = view.findViewById(R.id.numberTextView);
            itemNameTextView = view.findViewById(R.id.itemNameTextView1);
            weightTextView = view.findViewById(R.id.weightTextView);
            pricePerUnitTextView = view.findViewById(R.id.pricePerUnitTextView);
            itemPriceTextView = view.findViewById(R.id.itemPriceTextView);
            totalPriceTextView = view.findViewById(R.id.totalPriceTextView);
        }
    }
}
