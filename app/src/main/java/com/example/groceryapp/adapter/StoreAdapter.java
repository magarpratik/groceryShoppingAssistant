package com.example.groceryapp.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.math.BigDecimal;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.groceryapp.viewModel.SelectItemViewModel;
import com.example.groceryapp.viewModel.StoreViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {
    private StoreViewModel storeViewModel;
    private DatabaseHandler db;
    private List<ItemModel> finalList; // list being displayed
    private List<ItemModel> itemsList;
    private boolean isOnTextChanged = false;
    private BigDecimal totalPrice = new BigDecimal("0");
    private String listName;

    public StoreAdapter(StoreViewModel storeViewModel, DatabaseHandler db, String listName) {
        this.storeViewModel = storeViewModel;
        this.db = db;
        this.listName = listName;
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
    public void onBindViewHolder(@NonNull StoreAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.numberTextView.setText(String.valueOf(position + 1));
        holder.itemNameTextView.setText(finalList.get(position).getName());
        holder.weightTextView.setText(finalList.get(position).getWeight());
        holder.pricePerUnitTextView.setText(finalList.get(position).getPricePerUnit());
        BigDecimal price = new BigDecimal(finalList.get(position).getPrice());
        BigDecimal multiply = price.multiply(new BigDecimal("100"));
        BigDecimal result = multiply.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_EVEN);
        holder.itemPriceTextView.setText("£" + result);

        // click on an item to look at all the options
        // start an activity to show all the other options for the same item
        holder.storeItemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(storeViewModel, SelectItemViewModel.class);
                Bundle bundle = new Bundle();
                //bundle.putSerializable("itemsList", (Serializable) itemsList);
                //bundle.putString("listName", listName);
                bundle.putInt("listId", itemsList.get(position).getListId());
                bundle.putInt("storeId", itemsList.get(position).getStoreId());
                //ArrayList<ItemModel> comparisonList = new ArrayList<>();
                //comparisonList = db.getComparisonList(itemsList, listId, holder.getAdapterPosition());
                //bundle.putSerializable("comparisonList", (Serializable) comparisonList);
                bundle.putInt("itemId", itemsList.get(position).getId());
                bundle.putString("listName", listName);
                bundle.putSerializable("itemsList", (Serializable) itemsList);
                bundle.putSerializable("comparisonList", (Serializable) finalList);
                bundle.putInt("position", position);

                intent.putExtras(bundle);
                storeViewModel.startActivity(intent);
            }
        });
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
}
