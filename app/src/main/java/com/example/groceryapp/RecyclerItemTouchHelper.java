package com.example.groceryapp;


import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.adapter.MainActivityAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private MainActivityAdapter adapter;

    public RecyclerItemTouchHelper(MainActivityAdapter adapter) {
        // enable both right and left swipe functionality
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
        // position of the item that is being worked on
        final int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.LEFT) {
            // Confirm if you really want to delete the item
            AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
            builder.setTitle("Delete List");
            builder.setMessage("Are you sure you want to delete this list?");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.deleteItem(position);
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else {
            adapter.editItem(position);
        }
    }

}
