/**
 * @Author Pratik Magar 2241293
 **/

package com.example.groceryapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.R;
import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.models.ItemModel;
import com.example.groceryapp.activities.AddNewItemActivity;
import com.example.groceryapp.activities.InsideListActivity;

import java.util.ArrayList;
import java.util.List;

// RecyclerView adapter for the items inside the list
public class InsideListAdapter extends RecyclerView.Adapter<InsideListAdapter.ViewHolder> {
    private List<ItemModel> itemsList = new ArrayList<>();
    private InsideListActivity insideListActivity;
    private DatabaseHandler db;

    // Constructor for the adapter
    public InsideListAdapter(InsideListActivity insideListActivity, DatabaseHandler db) {
        this.insideListActivity = insideListActivity;
        this.db = db;
    }

    public Context getContext() { return insideListActivity; }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView numberTextView;
        private TextView itemNameTextView;
        private TextView itemQtyTextView;

        ViewHolder(View view) {
            super(view);
            itemNameTextView = view.findViewById(R.id.itemNameTextView);
            itemQtyTextView = view.findViewById(R.id.itemQtyTextView);
            numberTextView = view.findViewById(R.id.numberTextView);
        }
    }

    @NonNull
    @Override
    // inflate the item layout into the RecyclerView
    public InsideListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inside_list_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    // populates the RecyclerView
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.numberTextView.setText(String.valueOf(position + 1));
        holder.itemNameTextView.setText(itemsList.get(position).getName());
        if (itemsList.get(position).getQuantity() != null) {
            holder.itemQtyTextView.setText(itemsList.get(position).getQuantity()
                    + " " + itemsList.get(position).getUnit());
        }


    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    // Update the RecyclerView
    public void setItemsList(List<ItemModel> itemsList) {
        this.itemsList = itemsList;
        // update the RecyclerView
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        ItemModel itemModel = itemsList.get(position);
        db.deleteItem(itemModel.getId());
        itemsList.remove(position);
        notifyItemRemoved(position);
        if (itemsList.size() == 0) {
            insideListActivity.hideUI();
        }
    }

    public void editItem(int position) {
        ItemModel itemModel = itemsList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("LIST_ID", itemModel.getListId());
        bundle.putInt("ITEM_ID", itemModel.getId());
        bundle.putString("ITEM_NAME", itemModel.getName());
        bundle.putString("ITEM_QTY", itemModel.getQuantity());
        bundle.putString("ITEM_UNIT", itemModel.getUnit());

        AddNewItemActivity fragment = new AddNewItemActivity();
        fragment.setArguments(bundle);
        fragment.show(insideListActivity.getSupportFragmentManager(), AddNewItemActivity.TAG);
    }
}
