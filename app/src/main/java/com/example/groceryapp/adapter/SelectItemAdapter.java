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
import com.example.groceryapp.viewModel.SelectItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class SelectItemAdapter extends RecyclerView.Adapter<SelectItemAdapter.ViewHolder> {
    private SelectItemViewModel selectItemViewModel;
    private DatabaseHandler db;
    private List<ItemModel> listOfItems;


    public SelectItemAdapter(SelectItemViewModel selectItemViewModel, DatabaseHandler db) {
        this.selectItemViewModel = selectItemViewModel;
        this.db = db;
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
    public void onBindViewHolder(@NonNull SelectItemAdapter.ViewHolder holder, int position) {
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
}
