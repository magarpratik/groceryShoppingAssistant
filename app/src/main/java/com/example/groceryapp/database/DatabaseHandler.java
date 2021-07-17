package com.example.groceryapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String LIST_TABLE = "LIST_TABLE";
    public static final String COLUMN_LIST_NAME = "LIST_NAME";
    public static final String COLUMN_STORE = "STORE";
    public static final String COLUMN_ID = "ID";

    public DatabaseHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
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

    }
}
