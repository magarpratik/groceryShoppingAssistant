package com.example.groceryapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.R;
import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.model.ItemModel;
import com.example.groceryapp.viewModel.InsideListViewModel;
import com.example.groceryapp.viewModel.MainActivityViewModel;

import org.w3c.dom.Text;

import java.util.List;

// RecyclerView adapter for the items inside the list
public class InsideListAdapter extends RecyclerView.Adapter<InsideListAdapter.ViewHolder> {

    private InsideListViewModel insideListViewModel;
    private DatabaseHandler db;
    private List<ItemModel> itemsList;

    // Constructor for the adapter
    public InsideListAdapter(InsideListViewModel insideListViewModel, DatabaseHandler db) {
        this.insideListViewModel = insideListViewModel;
        this.db = db;
    }

    @NonNull
    @Override
    // inflate the item layout into the RecyclerView
    public InsideListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inside_list_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    // populates the RecyclerView
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (itemsList.get(position).getQuantity() == null) {
            String itemName = itemsList.get(position).getName();
            holder.setData(itemName);
        }
        else {
            String itemName = itemsList.get(position).getName();
            String itemQuantity = itemsList.get(position).getQuantity();
            holder.setData(itemName, itemQuantity);
        }
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView itemNameTextView;
        private TextView itemQtyTextView;

        ViewHolder(View view) {
            super(view);

            itemNameTextView = view.findViewById(R.id.itemNameTextView);
            itemQtyTextView = view.findViewById(R.id.itemQtyTextView);
        }

        public void setData(String itemName) {
            itemNameTextView.setText(itemName);
        }

        public void setData(String itemName, String itemQuantity) {
            itemNameTextView.setText(itemName);
            itemQtyTextView.setText(itemQuantity);
        }
    }
}
