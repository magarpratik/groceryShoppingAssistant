package com.example.groceryapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.groceryapp.model.ItemModel;
import com.example.groceryapp.model.ShoppingListModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    // database details
    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "GroceryApp.db";

    // LIST table details
    public static final String LIST_TABLE = "LIST_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_LIST_NAME = "LIST_NAME";
    public static final String COLUMN_STORE = "STORE";

    // ITEM table details
    public static final String ITEM_TABLE = "ITEM_TABLE";
    public static final String ITEM_ID = "ITEM_ID";
    public static final String ITEM_NAME = "ITEM_NAME";
    public static final String ITEM_QUANTITY = "ITEM_QUANTITY";
    public static final String ITEM_UNIT = "ITEM_UNIT";
    public static final String LIST_ID = "LIST_ID";

    // RESULTS table details
    public static final String RESULTS_TABLE = "RESULTS_TABLE";
    public static final String RESULTS_ITEM_ID = "RESULTS_ITEM_ID";
    public static final String RESULTS_NAME = "RESULTS_NAME";
    public static final String RESULTS_QTY = "RESULTS_QTY";
    public static final String RESULTS_LIST_ID = "RESULTS_LIST_ID";
    public static final String RESULTS_STORE_ID = "RESULTS_STORE_ID";
    public static final String RESULTS_PRICE = "RESULTS_PRICE";
    public static final String RESULTS_PPU = "RESULTS_PPU";
    public static final String RESULTS_PPU_EXTRACTED = "RESULTS_PPU_EXTRACTED";

    private SQLiteDatabase db;

    // Constructor
    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    // Open the database so you can write on it
    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    // This is called when a database is accessed for the first time
    // There should be code here to create a new database
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        String createListTableStatement = "CREATE TABLE " + LIST_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_LIST_NAME + " TEXT, " + COLUMN_STORE + " TEXT)";
        String createItemTableStatement = "CREATE TABLE " + ITEM_TABLE + " (" + ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LIST_ID + " INTEGER, " + ITEM_NAME + " TEXT, " + ITEM_QUANTITY + " TEXT, " + ITEM_UNIT + " TEXT)";
        String createResultsTableStatement = "CREATE TABLE " + RESULTS_TABLE + " (" + RESULTS_LIST_ID + " INTEGER, " + RESULTS_ITEM_ID
                + " INTEGER, " + RESULTS_STORE_ID + " INTEGER, " + RESULTS_NAME + " TEXT, " + RESULTS_PRICE + " TEXT, "
                + RESULTS_QTY + " TEXT, " + RESULTS_PPU + " TEXT, " + RESULTS_PPU_EXTRACTED + " TEXT)";

        db.execSQL(createListTableStatement);
        db.execSQL(createItemTableStatement);
        db.execSQL(createResultsTableStatement);
    }

    // called when the database version number changes
    // prevents previous users' apps from breaking down when the database design is changed
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the older tables
        db.execSQL("DROP TABLE IF EXISTS " + LIST_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + RESULTS_TABLE);

        // Create tables again
        onCreate(db);
    }

    // Add the webscraping results to the database
    public void addResults(ItemModel itemModel) {
        ContentValues cv = new ContentValues();
        cv.put(RESULTS_LIST_ID, itemModel.getListId());
        cv.put(RESULTS_ITEM_ID, itemModel.getId());
        cv.put(RESULTS_NAME, itemModel.getName());
        cv.put(RESULTS_QTY, itemModel.getQuantity());
        cv.put(RESULTS_STORE_ID, itemModel.getStoreId());
        cv.put(RESULTS_PRICE, itemModel.getPrice());
        cv.put(RESULTS_PPU, itemModel.getPricePerUnit());
        cv.put(RESULTS_PPU_EXTRACTED, itemModel.getExtractedPPU());

        db.insert(RESULTS_TABLE, null, cv);
    }

    public void clearResults() {
        db.execSQL("DELETE FROM " + RESULTS_TABLE);
    }





    // METHODS FOR THE ADDING/DELETING/EDITING LISTS

    public void addNewList(ShoppingListModel shoppingList) {
        // add the new list details to a cv data structure
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LIST_NAME, shoppingList.getName());
        cv.put(COLUMN_STORE, shoppingList.getStore());

        // insert the cv to the database
        db.insert(LIST_TABLE, null, cv);
    }

    // Change the name of a list
    public void updateList(int id, String list_name) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LIST_NAME, list_name);
        db.update(LIST_TABLE, cv, COLUMN_ID + "=?", new String[] {String.valueOf(id)});
    }

    // delete a list
    public void deleteList(int id) {
        db.delete(LIST_TABLE, COLUMN_ID + "=?", new String[] {String.valueOf(id)});
        db.delete(ITEM_TABLE, LIST_ID + "=?", new String[] {String.valueOf(id)});
    }

    // get the list table from the database
    public List<ShoppingListModel> getAllLists() {
        List<ShoppingListModel> listOfLists= new ArrayList<>();

        // Cursor is resultset
        Cursor cursor = null;

        // Atomic operation starts
        db.beginTransaction();
        try {
            // returns the whole table
            cursor = db.query(LIST_TABLE, null, null, null, null, null,null, null);

            if(cursor != null) {
                // move to the first row of the table
                if(cursor.moveToFirst()) {
                    do{
                        // Get the details about the lists from the resultset (cursor)
                        // Go through the lists from the resultset
                        // Add the lists from the resultset to the listOfLists ArrayList
                        ShoppingListModel shoppingList = new ShoppingListModel(cursor.getString(cursor.getColumnIndex(COLUMN_LIST_NAME)));
                        shoppingList.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                        shoppingList.setStore(cursor.getString(cursor.getColumnIndex(COLUMN_STORE)));
                        listOfLists.add(shoppingList);
                    }while(cursor.moveToNext());
                }
            }
        }
        finally {
            // Atomic operation ends
            db.endTransaction();
            cursor.close();
        }
        return listOfLists;
    }





    // METHODS FOR ADDING/DELETING/UPDATING ITEMS

    public void addNewItem(ItemModel itemModel) {
        // add the new ITEM details to a cv data structure
        ContentValues cv = new ContentValues();
        cv.put(LIST_ID, itemModel.getListId());
        cv.put(ITEM_NAME, itemModel.getName());
        cv.put(ITEM_QUANTITY, itemModel.getQuantity());
        cv.put(ITEM_UNIT, itemModel.getUnit());

        // insert the cv to the table
        db.insert(ITEM_TABLE, null, cv);
    }

    // Change the name of an item
    public void updateItem(int id, String item_name) {
        ContentValues cv = new ContentValues();
        cv.put(ITEM_NAME, item_name);
        db.update(ITEM_TABLE, cv, ITEM_ID + "=?", new String[] {String.valueOf(id)});
    }

    public void updateItem(int id, String item_name, String item_qty, String item_unit) {
        ContentValues cv = new ContentValues();
        cv.put(ITEM_NAME, item_name);
        cv.put(ITEM_QUANTITY, item_qty);
        cv.put(ITEM_UNIT, item_unit);
        db.update(ITEM_TABLE, cv, ITEM_ID + "=?", new String[] {String.valueOf(id)});
    }

    // delete a list
    public void deleteItem(int id) {
        db.delete(ITEM_TABLE, ITEM_ID + "=?", new String[] {String.valueOf(id)});
    }

    // get the list table from the database
    public List<ItemModel> getListOfItems(int listId) {
        List<ItemModel> listOfItems= new ArrayList<>();

        // Cursor is resultset
        Cursor cursor = null;

        // Atomic operation starts
        db.beginTransaction();
        try {
            // returns the whole table
            cursor = db.query(ITEM_TABLE, null, LIST_ID + " = " + listId, null, null, null,null, null);

            if(cursor != null) {
                // move to the first row of the table
                if(cursor.moveToFirst()) {
                    do{
                        // Get the details about the lists from the resultset (cursor)
                        // Go through the lists from the resultset
                        // Add the lists from the resultset to the listOfLists ArrayList
                        ItemModel item = new ItemModel(cursor.getInt(cursor.getColumnIndex(LIST_ID)), cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
                        item.setId(cursor.getInt(cursor.getColumnIndex(ITEM_ID)));
                        item.setQuantity(cursor.getString(cursor.getColumnIndex(ITEM_QUANTITY)));
                        item.setUnit(cursor.getString(cursor.getColumnIndex(ITEM_UNIT)));
                        listOfItems.add(item);
                    }while(cursor.moveToNext());
                }
            }
        }
        finally {
            // Atomic operation ends
            db.endTransaction();
            cursor.close();
        }
        return listOfItems;
    }




    // Get the list of items of a particular list
    public ArrayList<ItemModel> getItemsList(int listId) {
        ArrayList<ItemModel> result = new ArrayList<>();
        Cursor cursor = null;

        db.beginTransaction();
        try {
            cursor = db.query(ITEM_TABLE, null, LIST_ID + " = " + listId, null, null, null, null, null);

            if(cursor != null) {
                if(cursor.moveToFirst()) {
                    do{
                        ItemModel itemModel = new ItemModel(cursor.getInt(cursor.getColumnIndex(LIST_ID)), cursor.getString(cursor.getColumnIndex(ITEM_NAME)),
                                cursor.getString(cursor.getColumnIndex(ITEM_QUANTITY)), cursor.getString(cursor.getColumnIndex(ITEM_UNIT)));
                        itemModel.setId(cursor.getInt(cursor.getColumnIndex(ITEM_ID)));
                        result.add(itemModel);
                    }while(cursor.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            cursor.close();
        }

        return result;
    }




    // get comparison price
    public ArrayList<BigDecimal> getComparisonPrices(ArrayList<ItemModel> itemsList) {
        ArrayList<BigDecimal> prices = null;
        return prices;
    }





    // reset the database
    public void resetDatabase() {
        db.execSQL("DROP TABLE IF EXISTS " + LIST_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + RESULTS_TABLE);
    }

    // EXAMPLE
    // add a new list
    /*public boolean addList(ShoppingListModel shoppingListModel) {
        openDatabase();
        // Dictionary-like data structure
        // Key-value pairs
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_LIST_NAME, shoppingListModel.getName());
        cv.put(COLUMN_STORE, shoppingListModel.getStore());

        // -1 for fail
        // +1 for success
        long insert = db.insert(LIST_TABLE, null, cv);
        if (insert == -1) {
            return false;
        }
        else {
            return true;
        }
    }*/
}
