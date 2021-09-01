package com.example.groceryapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.icu.math.BigDecimal;
import android.os.Build;
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
import com.example.groceryapp.viewModel.InsideSavedListViewModel;

import java.util.ArrayList;
import java.util.List;

public class InsideSavedListAdapter extends RecyclerView.Adapter<InsideSavedListAdapter.ViewHolder> {
    private InsideSavedListViewModel insideSavedListViewModel;
    private DatabaseHandler db;
    private List<ItemModel> itemsList;
    private List<Integer> basketPriceList;
    private View rootview;
    private Context context;

    boolean isOnTextChanged = false;
    int basketPrice;
    private TextView basketPriceTextView;

    public InsideSavedListAdapter(InsideSavedListViewModel insideSavedListViewModel, DatabaseHandler db) {
        this.insideSavedListViewModel = insideSavedListViewModel;
        this.db = db;
    }

    @NonNull
    @Override
    public InsideSavedListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_item, parent, false);

        context = parent.getContext();
        rootview = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);

        basketPriceTextView = (TextView) rootview.findViewById(R.id.basketPriceTextView);

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

        holder.storeItemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.itemNameTextView.getPaint().isStrikeThruText()) {
                    holder.itemNameTextView.setPaintFlags(holder.itemNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.weightTextView.setPaintFlags(holder.weightTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.pricePerUnitTextView.setPaintFlags(holder.pricePerUnitTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.itemPriceTextView.setPaintFlags(holder.itemPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    itemsList.get(holder.getAdapterPosition()).setCrossed(true);
                }
                else {
                    holder.itemNameTextView.setPaintFlags(holder.itemNameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.weightTextView.setPaintFlags(holder.weightTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.pricePerUnitTextView.setPaintFlags(holder.pricePerUnitTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.itemPriceTextView.setPaintFlags(holder.itemPriceTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    itemsList.get(holder.getAdapterPosition()).setCrossed(false);
                }

                int total = 0;
                for (int i = 0; i < itemsList.size(); i++) {
                    if (itemsList.get(i).isCrossed()) {
                        BigDecimal price = new BigDecimal(itemsList.get(i).getPrice());
                        BigDecimal multiplier = new BigDecimal("100");
                        BigDecimal result = price.multiply(multiplier);
                        int num = result.intValue();
                        total = total + num;
                    }

                    BigDecimal answer = new BigDecimal(String.valueOf(total));
                    BigDecimal divider = new BigDecimal("100");
                    BigDecimal res = answer.divide(divider, 2, BigDecimal.ROUND_HALF_EVEN);

                    basketPriceTextView.setText("£" + String.valueOf(res));
                }
            }
        });
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
