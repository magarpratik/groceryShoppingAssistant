package com.example.groceryapp.adapter;

import android.icu.math.BigDecimal;
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
import com.example.groceryapp.model.ShoppingListModel;
import com.example.groceryapp.viewModel.SavedListsViewModel;

import java.util.ArrayList;
import java.util.List;

public class SavedListsAdapter extends RecyclerView.Adapter<SavedListsAdapter.ViewHolder> {
    private List<ShoppingListModel> list = new ArrayList<>();
    private SavedListsViewModel savedListsViewModel;
    private DatabaseHandler db;

    public SavedListsAdapter(SavedListsViewModel savedListsViewModel, DatabaseHandler db) {
        this.savedListsViewModel = savedListsViewModel;
        this.db = db;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.listNameTextView.setText(list.get(position).getName());
        holder.listNumberTextView.setText(String.valueOf(position + 1));
        switch (Integer.valueOf(list.get(position).getStore().trim())) {
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
}
