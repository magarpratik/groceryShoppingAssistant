package com.example.groceryapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.groceryapp.model.ShoppingListModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    // database details
    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "list.db";

    // table details
    public static final String LIST_TABLE = "LIST_TABLE";
    public static final String COLUMN_LIST_NAME = "LIST_NAME";
    public static final String COLUMN_STORE = "STORE";
    public static final String COLUMN_ID = "ID";

    private SQLiteDatabase db;

    // Constructor
    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    // This is called when a database is accessed for the first time
    // There should be code here to create a new database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + LIST_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_LIST_NAME + " TEXT, " + COLUMN_STORE + " TEXT)";
        db.execSQL(createTableStatement);
    }

    // called when the database version number changes
    // prevents previous users' apps from breaking down when the database design is changed
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the older tables
        db.execSQL("DROP TABLE IF EXISTS " + LIST_TABLE);

        // Create tables again
        onCreate(db);
    }

    // Open the database so you can write on it
    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void addNewList(ShoppingListModel shoppingList) {
        // add the new list details to a cv data structure
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LIST_NAME, shoppingList.getName());
        cv.put(COLUMN_STORE, shoppingList.getStore());

        // insert the cv to the database
        db.insert(LIST_TABLE, null, cv);
    }

    public List<ShoppingListModel> getAllLists() {
        List<ShoppingListModel> listOfLists= new ArrayList<>();

        // Cursor is resultset
        Cursor cursor = null;

        // Atomic operation
        db.beginTransaction();
        try {
            // returns the whole table
            cursor = db.query(LIST_TABLE, null, null, null, null, null,null);

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

    // Change the name of a list
    public void updateList(int id, String list_name) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LIST_NAME, list_name);
        db.update(LIST_TABLE, cv, COLUMN_ID + "=?", new String[] {String.valueOf(id)});
    }

    // delete a list
    public void deleteList(int id, String list_name) {
        db.delete(LIST_TABLE, COLUMN_ID + "=?", new String[] {String.valueOf(id)});
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
