/**
 * @Author Pratik Magar 2241293
 **/

package com.example.groceryapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.icu.math.BigDecimal;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.R;
import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.models.ItemModel;
import com.example.groceryapp.activities.AddNewSavedItemActivity;
import com.example.groceryapp.activities.ShoppingActivity;

import java.util.List;

public class InsideSavedListAdapter extends RecyclerView.Adapter<InsideSavedListAdapter.ViewHolder> {
    private ShoppingActivity shoppingActivity;
    private DatabaseHandler db;
    private List<ItemModel> itemsList;
    private List<Integer> basketPriceList;
    private View rootview;
    private Context context;

    boolean isOnTextChanged = false;
    int basketPrice;
    private TextView basketPriceTextView;
    private TextView estimatedPriceTextView;

    public InsideSavedListAdapter(ShoppingActivity shoppingActivity, DatabaseHandler db) {
        this.shoppingActivity = shoppingActivity;
        this.db = db;
    }

    public Context getContext() { return shoppingActivity;}

    @NonNull
    @Override
    public InsideSavedListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_item, parent, false);

        context = parent.getContext();
        rootview = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);

        basketPriceTextView = (TextView) rootview.findViewById(R.id.basketPriceTextView);
        estimatedPriceTextView = (TextView) rootview.findViewById(R.id.estimatedPriceTextView);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull InsideSavedListAdapter.ViewHolder holder, int position) {
        holder.numberTextView.setText(String.valueOf(position + 1));
        holder.itemNameTextView.setText(itemsList.get(position).getName());
        holder.weightTextView.setText(itemsList.get(holder.getAdapterPosition()).getWeight());
        holder.pricePerUnitTextView.setText(itemsList.get(position).getPricePerUnit());
        BigDecimal price = new BigDecimal(itemsList.get(position).getPrice());
        BigDecimal multiply = price.multiply(new BigDecimal("100"));
        BigDecimal result = multiply.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_EVEN);
        holder.itemPriceTextView.setText("£" + result);
        //holder.quantityText.setText(itemsList.get(holder.getAdapterPosition()).getFinalQuantity());

        // if item is crossed
        if (itemsList.get(holder.getAdapterPosition()).isCrossed()) {
            holder.itemNameTextView.setPaintFlags(holder.itemNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.weightTextView.setPaintFlags(holder.weightTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.pricePerUnitTextView.setPaintFlags(holder.pricePerUnitTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.itemPriceTextView.setPaintFlags(holder.itemPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            holder.itemNameTextView.setPaintFlags(holder.itemNameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.weightTextView.setPaintFlags(holder.weightTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.pricePerUnitTextView.setPaintFlags(holder.pricePerUnitTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.itemPriceTextView.setPaintFlags(holder.itemPriceTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        holder.storeItemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.itemNameTextView.getPaint().isStrikeThruText()) {
                    holder.itemNameTextView.setPaintFlags(holder.itemNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.weightTextView.setPaintFlags(holder.weightTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.pricePerUnitTextView.setPaintFlags(holder.pricePerUnitTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.itemPriceTextView.setPaintFlags(holder.itemPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    itemsList.get(holder.getAdapterPosition()).setCrossed(true);
                    db.crossItem(itemsList.get(holder.getAdapterPosition()).getListId(),
                            itemsList.get(holder.getAdapterPosition()).getStoreId(),
                            itemsList.get(holder.getAdapterPosition()).getId(),
                            1);
                }
                else {
                    holder.itemNameTextView.setPaintFlags(holder.itemNameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.weightTextView.setPaintFlags(holder.weightTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.pricePerUnitTextView.setPaintFlags(holder.pricePerUnitTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.itemPriceTextView.setPaintFlags(holder.itemPriceTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    itemsList.get(holder.getAdapterPosition()).setCrossed(false);
                    db.crossItem(itemsList.get(holder.getAdapterPosition()).getListId(),
                            itemsList.get(holder.getAdapterPosition()).getStoreId(),
                            itemsList.get(holder.getAdapterPosition()).getId(),
                            0);
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


                int totalEstimate = 0;
                // add the prices
                for (int j = 0; j < itemsList.size(); j++) {
                    String price = itemsList.get(j).getPrice();
                    java.math.BigDecimal num = new java.math.BigDecimal(price.trim());
                    java.math.BigDecimal multiplier = new java.math.BigDecimal("100");
                    java.math.BigDecimal res = num.multiply(multiplier);
                    totalEstimate = totalEstimate + res.intValue();
                }

                java.math.BigDecimal num = new java.math.BigDecimal(String.valueOf(totalEstimate));
                java.math.BigDecimal divisor = new java.math.BigDecimal("100");
                java.math.BigDecimal result = num.divide(divisor, 2, java.math.BigDecimal.ROUND_HALF_EVEN);
                total = 0;

                String totalPrice = String.valueOf(result);
                estimatedPriceTextView.setText("£" + totalPrice);
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
        private EditText quantityText;

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
            quantityText = view.findViewById(R.id.quantityText);
        }
    }

    public void setSavedItemsList(List<ItemModel> itemsList) {
        this.itemsList = itemsList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        ItemModel itemModel = itemsList.get(position);
        db.deleteSavedItem(itemModel.getId());
        itemsList.remove(position);
        notifyItemRemoved(position);
    }

    // TODO
    public void editItem(int position) {
        ItemModel itemModel = itemsList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("LIST_ID", itemModel.getListId());
        bundle.putInt("ITEM_ID", itemModel.getId());
        bundle.putString("ITEM_NAME", itemModel.getName());
        bundle.putString("ITEM_WEIGHT", itemModel.getWeight());
        bundle.putInt("STORE_ID", itemModel.getStoreId());
        bundle.putString("ITEM_PRICE", itemModel.getPrice());


        AddNewSavedItemActivity fragment = new AddNewSavedItemActivity();
        fragment.setArguments(bundle);
        fragment.show(shoppingActivity.getSupportFragmentManager(), AddNewSavedItemActivity.TAG);
    }
}
