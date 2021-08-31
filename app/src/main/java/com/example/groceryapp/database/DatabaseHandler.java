package com.example.groceryapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.math.BigDecimal;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.groceryapp.model.ItemModel;
import com.example.groceryapp.model.ShoppingListModel;

import java.math.RoundingMode;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseHandler extends SQLiteOpenHelper {
    // database details
    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "GroceryApp.db";

    // LIST table details
    public static final String LIST_TABLE = "LIST_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_LIST_NAME = "LIST_NAME";
    public static final String COLUMN_STORE = "STORE";
    public static final String COLUMN_IS_SAVED = "COLUMN_IS_SAVED";

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

    // FINAL table details
    public static final String FINAL_TABLE = "FINAL_TABLE";
    public static final String FINAL_ITEM_ID = "FINAL_ITEM_ID";
    public static final String FINAL_NAME = "FINAL_NAME";
    public static final String FINAL_QTY = "FINAL_QTY";
    public static final String FINAL_LIST_ID = "FINAL_LIST_ID";
    public static final String FINAL_STORE_ID = "FINAL_STORE_ID";
    public static final String FINAL_PRICE = "FINAL_PRICE";
    public static final String FINAL_PPU = "FINAL_PPU";

    private SQLiteDatabase db;

    // Constructor
    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    // Open the database so you can write on it
    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void getReadableDB() { db = this.getReadableDatabase(); }

    // This is called when a database is accessed for the first time
    // There should be code here to create a new database
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        String createListTableStatement = "CREATE TABLE " + LIST_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_LIST_NAME + " TEXT, " + COLUMN_IS_SAVED + " INTEGER, " + COLUMN_STORE + " INTEGER)";
        String createItemTableStatement = "CREATE TABLE " + ITEM_TABLE + " (" + ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LIST_ID + " INTEGER, " + ITEM_NAME + " TEXT, " + ITEM_QUANTITY + " TEXT, " + ITEM_UNIT + " TEXT)";
        String createResultsTableStatement = "CREATE TABLE " + RESULTS_TABLE + " (" + RESULTS_LIST_ID + " INTEGER, " + RESULTS_ITEM_ID
                + " INTEGER, " + RESULTS_STORE_ID + " INTEGER, " + RESULTS_NAME + " TEXT, " + RESULTS_PRICE + " TEXT, "
                + RESULTS_QTY + " TEXT, " + RESULTS_PPU + " TEXT, " + RESULTS_PPU_EXTRACTED + " TEXT)";
        String createFinalTableStatement = "CREATE TABLE " + FINAL_TABLE + " (" + FINAL_LIST_ID + " INTEGER, " + FINAL_ITEM_ID
                + " INTEGER, " + FINAL_STORE_ID + " INTEGER, " + FINAL_NAME + " TEXT, " + FINAL_PRICE + " TEXT, "
                + FINAL_QTY + " TEXT, " + FINAL_PPU + " TEXT)";

        db.execSQL(createListTableStatement);
        db.execSQL(createItemTableStatement);
        db.execSQL(createResultsTableStatement);
        db.execSQL(createFinalTableStatement);
    }

    // called when the database version number changes
    // prevents previous users' apps from breaking down when the database design is changed
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the older tables
        db.execSQL("DROP TABLE IF EXISTS " + LIST_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + RESULTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FINAL_TABLE);

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

    public void clearResults(int listId) {
        db.execSQL("DELETE FROM " + RESULTS_TABLE + " WHERE " + RESULTS_LIST_ID + " = " + listId);
    }



    // METHODS FOR THE ADDING/DELETING/EDITING LISTS

    public void addNewList(ShoppingListModel shoppingList) {
        // add the new list details to a cv data structure
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LIST_NAME, shoppingList.getName());
        cv.put(COLUMN_STORE, shoppingList.getStore());
        cv.put(COLUMN_IS_SAVED, 0);

        // insert the cv to the database
        db.insert(LIST_TABLE, null, cv);
    }

    // Change the name of a list
    public void updateList(int id, String list_name) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LIST_NAME, list_name);
        db.update(LIST_TABLE, cv, COLUMN_ID + "=?", new String[] {String.valueOf(id)});
    }

    public void saveList(int listId, int storeId) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_IS_SAVED, 1);
        cv.put(COLUMN_STORE, storeId);
        db.update(LIST_TABLE, cv, COLUMN_ID + "=?", new String[] {String.valueOf(listId)});
    }

    // delete a list
    public void deleteList(int id) {
        db.delete(LIST_TABLE, COLUMN_ID + "=?", new String[] {String.valueOf(id)});
        db.delete(ITEM_TABLE, LIST_ID + "=?", new String[] {String.valueOf(id)});
    }

    // get the list table from the database
    public List<ShoppingListModel> getAllLists(int isSaved) {
        List<ShoppingListModel> listOfLists= new ArrayList<>();

        // Cursor is resultset
        Cursor cursor = null;

        // Atomic operation starts
        db.beginTransaction();
        try {
            // returns the whole table
            cursor = db.query(LIST_TABLE, null, COLUMN_IS_SAVED + " = " + isSaved, null, null, null,null, null);

            if(cursor != null) {
                // move to the first row of the table
                if(cursor.moveToFirst()) {
                    do{
                        // Get the details about the lists from the resultset (cursor)
                        // Go through the lists from the resultset
                        // Add the lists from the resultset to the listOfLists ArrayList
                        ShoppingListModel shoppingList = new ShoppingListModel(cursor.getString(cursor.getColumnIndex(COLUMN_LIST_NAME)));
                        shoppingList.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                        shoppingList.setStore(String.valueOf(cursor.getInt(cursor.getColumnIndex(COLUMN_STORE))));
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

    public void deleteOldItems(int listId) {
        db.delete(ITEM_TABLE, LIST_ID + "=?", new String[] {String.valueOf(listId)});
    }

    // get the list table from the database
    public List<ItemModel> getListOfItems(int listId) {
        List<ItemModel> listOfItems = new ArrayList<>();

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
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<String> getComparisonPrices(ArrayList<ItemModel> itemsList) {
        ArrayList<String> prices = new ArrayList<>();
        int count = 0;

        db.beginTransaction();
        try {
            // Loop through stores
            for (int i = 0; i < 3; i++) {
                String storeId = String.valueOf(i);
                int result = 0;

                // loop through items
                for (int j = 0; j < itemsList.size(); j++) {
                    String itemId = String.valueOf(itemsList.get(j).getId());
                    String res = "";

                    // Cursor is resultset
                    Cursor cursor = null;

                    String[] columns = {RESULTS_PRICE, RESULTS_PPU_EXTRACTED};
                    String selection = RESULTS_STORE_ID + " = " + storeId +
                            " AND " + RESULTS_ITEM_ID + " = " + itemId;
                    String orderBy = RESULTS_PPU_EXTRACTED + ", " + RESULTS_PRICE;

                    cursor = db.query(RESULTS_TABLE,
                            columns,
                            selection,
                            null,
                            null,
                            null,
                            orderBy);
                    //String[] args = {storeId, itemId};
                    /*cursor = db.rawQuery("SELECT " + RESULTS_PRICE + ", " + "MIN(" + RESULTS_PPU_EXTRACTED + ") FROM " +
                        "(SELECT " + RESULTS_PRICE + ", " + RESULTS_PPU_EXTRACTED + " FROM " + RESULTS_TABLE + " WHERE " +
                        RESULTS_STORE_ID + " =? AND " + RESULTS_ITEM_ID + " =?)", args);*/

                    if (cursor != null && cursor.moveToFirst()) {
                        res = cursor.getString(cursor.getColumnIndex(RESULTS_PRICE));
                    }
                    cursor.close();

                    BigDecimal bigDecimal = new BigDecimal(res);
                    BigDecimal multiplier = new BigDecimal("100");
                    BigDecimal answer = bigDecimal.multiply(multiplier);
                    result = result + answer.intValue();
                }
                BigDecimal finalResult = new BigDecimal(String.valueOf(result));
                BigDecimal divisor = new BigDecimal("100");
                BigDecimal finalFinalResult = finalResult.divide(divisor, 2, BigDecimal.ROUND_HALF_EVEN);
                prices.add(String.valueOf(finalFinalResult));
            }
        }
        finally {
                count = 0;
                db.endTransaction();
        }
        return prices;
    }




    // get the cheapest items
    public ArrayList<ItemModel> getComparisonList(List<ItemModel> itemsList, int listId, int storeId) {
        ArrayList<ItemModel> result = new ArrayList<>();

        // Cursor is resultset
        Cursor cursor = null;

        String[] columns = {RESULTS_ITEM_ID, RESULTS_STORE_ID, RESULTS_NAME, RESULTS_PRICE, RESULTS_QTY, RESULTS_PPU, "MIN(" + RESULTS_PPU_EXTRACTED + ")"};

        // Atomic operation starts
        db.beginTransaction();
        try {
            // returns the whole table
            cursor = db.query(RESULTS_TABLE,
                    columns,
                    RESULTS_LIST_ID + " = " + listId + " AND " +
                    RESULTS_STORE_ID + " = " + storeId,
                    null,
                    RESULTS_ITEM_ID,
                    null,
                    null);

            if(cursor != null) {
                // move to the first row of the table
                if(cursor.moveToFirst()) {
                    do{
                        // Get the details about the lists from the resultset (cursor)
                        // Go through the lists from the resultset
                        // Add the lists from the resultset to the listOfLists ArrayList
                        ItemModel item = new ItemModel(listId, cursor.getString(cursor.getColumnIndex(RESULTS_NAME)));
                        item.setId(cursor.getInt(cursor.getColumnIndex(RESULTS_ITEM_ID)));
                        item.setWeight(cursor.getString(cursor.getColumnIndex(RESULTS_QTY)));
                        item.setStoreId(cursor.getInt(cursor.getColumnIndex(RESULTS_STORE_ID)));
                        item.setPrice(cursor.getString(cursor.getColumnIndex(RESULTS_PRICE)));
                        item.setPricePerUnit(cursor.getString(cursor.getColumnIndex(RESULTS_PPU)));

                        result.add(item);
                    }while(cursor.moveToNext());
                }
            }
        }
        finally {
            // Atomic operation ends
            db.endTransaction();
            cursor.close();
        }
        return result;
    }




    // get the cheapest items
    public void addCheapestItems(int listId, int storeId) {
        //ArrayList<ItemModel> result = new ArrayList<>();

        // Cursor is resultset
        Cursor cursor = null;

        String[] columns = {RESULTS_LIST_ID, RESULTS_ITEM_ID, RESULTS_STORE_ID, RESULTS_NAME, RESULTS_PRICE, RESULTS_QTY, RESULTS_PPU, "MIN(" + RESULTS_PPU_EXTRACTED + ")"};

        // Atomic operation starts
        db.beginTransaction();
        try {
            // returns the whole table
            cursor = db.query(RESULTS_TABLE,
                    columns,
                    RESULTS_LIST_ID + " = " + listId + " AND " +
                            RESULTS_STORE_ID + " = " + storeId,
                    null,
                    RESULTS_ITEM_ID,
                    null,
                    null);

            if(cursor != null) {
                // move to the first row of the table
                if(cursor.moveToFirst()) {
                    do{
                        // Get the details about the lists from the resultset (cursor)
                        // Go through the lists from the resultset
                        // Add the lists from the resultset to the listOfLists ArrayList
                        ItemModel item = new ItemModel(listId, cursor.getString(cursor.getColumnIndex(RESULTS_NAME)));
                        item.setId(cursor.getInt(cursor.getColumnIndex(RESULTS_ITEM_ID)));
                        item.setWeight(cursor.getString(cursor.getColumnIndex(RESULTS_QTY)));
                        item.setStoreId(cursor.getInt(cursor.getColumnIndex(RESULTS_STORE_ID)));
                        item.setPrice(cursor.getString(cursor.getColumnIndex(RESULTS_PRICE)));
                        item.setPricePerUnit(cursor.getString(cursor.getColumnIndex(RESULTS_PPU)));

                        addFinalItem(item);
                    }while(cursor.moveToNext());
                }
            }
        }
        finally {
            // Atomic operation ends
            db.endTransaction();
            cursor.close();
        }
    }

    public void addFinalItem(ItemModel itemModel) {
        ContentValues cv = new ContentValues();
        cv.put(FINAL_LIST_ID, itemModel.getListId());
        cv.put(FINAL_ITEM_ID, itemModel.getId());
        cv.put(FINAL_NAME, itemModel.getName());
        cv.put(FINAL_QTY, itemModel.getQuantity());
        cv.put(FINAL_STORE_ID, itemModel.getStoreId());
        cv.put(FINAL_PRICE, itemModel.getPrice());
        cv.put(FINAL_PPU, itemModel.getPricePerUnit());

        db.insert(FINAL_TABLE, null, cv);
    }


    // get items to choose from
    public ArrayList<ItemModel> getOptionsList(int itemId, int listId, int storeId) {
        ArrayList<ItemModel> result = new ArrayList<>();

        // Cursor is resultset
        Cursor cursor = null;

        String[] columns = {RESULTS_ITEM_ID, RESULTS_STORE_ID, RESULTS_NAME, RESULTS_PRICE, RESULTS_QTY, RESULTS_PPU};

        // Atomic operation starts
        db.beginTransaction();
        try {
            // returns the whole table
            cursor = db.query(RESULTS_TABLE,
                    columns,
                    RESULTS_LIST_ID + " = " + listId + " AND " +
                            RESULTS_STORE_ID + " = " + storeId + " AND " +
                           RESULTS_ITEM_ID + " = " + itemId,
                    null,
                    null,
                    null,
                    null);

            if(cursor != null) {
                // move to the first row of the table
                if(cursor.moveToFirst()) {
                    do{
                        // Get the details about the lists from the resultset (cursor)
                        // Go through the lists from the resultset
                        // Add the lists from the resultset to the listOfLists ArrayList
                        ItemModel item = new ItemModel(listId, cursor.getString(cursor.getColumnIndex(RESULTS_NAME)));
                        item.setId(cursor.getInt(cursor.getColumnIndex(RESULTS_ITEM_ID)));
                        item.setWeight(cursor.getString(cursor.getColumnIndex(RESULTS_QTY)));
                        item.setStoreId(cursor.getInt(cursor.getColumnIndex(RESULTS_STORE_ID)));
                        item.setPrice(cursor.getString(cursor.getColumnIndex(RESULTS_PRICE)));
                        item.setPricePerUnit(cursor.getString(cursor.getColumnIndex(RESULTS_PPU)));

                        result.add(item);
                    }while(cursor.moveToNext());
                }
            }
        }
        finally {
            // Atomic operation ends
            db.endTransaction();
            cursor.close();
        }
        return result;
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

