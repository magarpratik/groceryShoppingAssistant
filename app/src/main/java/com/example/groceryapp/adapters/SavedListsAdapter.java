/**
 * @Author Pratik Magar 2241293
 **/

package com.example.groceryapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.R;
import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.models.ShoppingListModel;
import com.example.groceryapp.activities.AddNewListActivity;
import com.example.groceryapp.activities.ShoppingActivity;
import com.example.groceryapp.activities.SavedListsActivity;

import java.util.ArrayList;
import java.util.List;

public class SavedListsAdapter extends RecyclerView.Adapter<SavedListsAdapter.ViewHolder> {
    private List<ShoppingListModel> list = new ArrayList<>();
    private SavedListsActivity savedListsActivity;
    private DatabaseHandler db;

    public SavedListsAdapter(SavedListsActivity savedListsActivity, DatabaseHandler db) {
        this.savedListsActivity = savedListsActivity;
        this.db = db;
    }

    public Context getContext() {return savedListsActivity;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String listName = list.get(position).getName();
        holder.listNameTextView.setText(listName);
        holder.listNumberTextView.setText(String.valueOf(position + 1));

        int storeId = Integer.valueOf(list.get(position).getStore().trim());
        switch (storeId) {
            case 0:
                holder.logoImageView1.setImageResource(R.drawable.asda_logo);
                break;
            case 1:
                holder.logoImageView1.setImageResource(R.drawable.sainsburys_logo);
                break;
            case 2:
                holder.logoImageView1.setImageResource(R.drawable.tesco_logo);
                break;
        }

        holder.listNameCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(savedListsActivity, ShoppingActivity.class);
                i.putExtra("storeId", storeId);
                i.putExtra("listName", listName);
                i.putExtra("listId", list.get(position).getId());
                savedListsActivity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView listNameTextView;
        private CardView listNameCardView;
        private TextView listNumberTextView;
        private ImageView logoImageView1;

        public ViewHolder(@NonNull View view) {
            super(view);
            listNameTextView = view.findViewById(R.id.listNameTextView);
            listNameCardView = view.findViewById(R.id.listNameCardView);
            listNumberTextView = view.findViewById(R.id.listNumberTextView);
            logoImageView1 = view.findViewById(R.id.logoImageView1);
        }
    }

    public void setList(List<ShoppingListModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        ShoppingListModel item = list.get(position);
        db.deleteList(item.getId());
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        ShoppingListModel item = list.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("ID", item.getId());
        bundle.putString("LIST_NAME", item.getName());
        AddNewListActivity fragment = new AddNewListActivity();
        fragment.setArguments(bundle);
        fragment.show(savedListsActivity.getSupportFragmentManager(), AddNewListActivity.TAG);
    }
}
