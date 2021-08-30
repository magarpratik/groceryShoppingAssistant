package com.example.groceryapp.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.math.BigDecimal;
import android.os.Build;
import android.os.Bundle;
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

public class SelectItemAdapter extends RecyclerView.Adapter<SelectItemAdapter.ViewHolder> {
    private SelectItemViewModel selectItemViewModel;
    private DatabaseHandler db;
    private List<ItemModel> listOfItems; // optionsList that is displayed
    private String listName;
    private List<ItemModel> finalList;
    private int itemPosition;
    private List<ItemModel> itemsList;
    private int storeId;

    public SelectItemAdapter(SelectItemViewModel selectItemViewModel, DatabaseHandler db, String listName, int storeId) {
        this.selectItemViewModel = selectItemViewModel;
        this.db = db;
        this.listName = listName;
        this.storeId = storeId;
    }

    @NonNull
    @Override
    public SelectItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_item, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull SelectItemAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.numberTextView.setText(String.valueOf(position + 1));
        holder.itemNameTextView.setText(listOfItems.get(position).getName());
        holder.weightTextView.setText(listOfItems.get(position).getWeight());
        holder.pricePerUnitTextView.setText(listOfItems.get(position).getPricePerUnit());
        holder.itemPriceTextView.setText("£" + listOfItems.get(position).getPrice());
        //BigDecimal price = new BigDecimal(listOfItems.get(position).getPrice());
        //BigDecimal multiply = price.multiply(new BigDecimal("100"));
        //BigDecimal result = multiply.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_EVEN);
        //holder.itemPriceTextView.setText("£" + result);

        // choose the item
        // update the finalList
        holder.storeItemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(selectItemViewModel, StoreViewModel.class);
                Bundle bundle = new Bundle();
                //bundle.putSerializable("itemsList", (Serializable) itemsList);
                //bundle.putString("listName", listName);
                bundle.putInt("listId", listOfItems.get(position).getListId());
                bundle.putInt("storeId", listOfItems.get(position).getStoreId());
                //ArrayList<ItemModel> comparisonList = new ArrayList<>();
                //comparisonList = db.getComparisonList(itemsList, listId, holder.getAdapterPosition());
                //bundle.putSerializable("comparisonList", (Serializable) comparisonList);
                //bundle.putInt("itemId", listOfItems.get(position).getId());
                bundle.putString("listName", listName);
                bundle.putSerializable("itemsList", (Serializable) itemsList);
                //finalList.remove(itemPosition);
                //finalList.add(itemPosition, listOfItems.get(position));
                bundle.putSerializable("comparisonList", (Serializable) finalList);

                intent.putExtras(bundle);
                selectItemViewModel.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView numberTextView;
        private TextView itemNameTextView;
        private TextView weightTextView;
        private TextView pricePerUnitTextView;
        private TextView itemPriceTextView;
        private CardView storeItemCardView;

        public ViewHolder(@NonNull View view) {
            super(view);
            numberTextView = view.findViewById(R.id.numberTextView);
            itemNameTextView = view.findViewById(R.id.itemNameTextView1);
            weightTextView = view.findViewById(R.id.weightTextView);
            pricePerUnitTextView = view.findViewById(R.id.pricePerUnitTextView);
            itemPriceTextView = view.findViewById(R.id.itemPriceTextView);
            storeItemCardView = view.findViewById(R.id.storeItemCardView);
        }
    }

    public void setListOfItems(ArrayList<ItemModel> optionsList) {
        this.listOfItems = optionsList;
    }

    public void setFinalList(ArrayList<ItemModel> finalList) {
        this.finalList = finalList;
        notifyDataSetChanged();
    }

    public void setItemPosition(int position) {
        this.itemPosition = position;
        notifyDataSetChanged();
    }

    public void setItemsList(List<ItemModel> itemsList) {
        this.itemsList = itemsList;
        notifyDataSetChanged();
    }
}
