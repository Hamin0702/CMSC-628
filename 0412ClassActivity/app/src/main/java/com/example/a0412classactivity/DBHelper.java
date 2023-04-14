package com.example.a0412classactivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static String CREATE_TABLE = "CREATE TABLE " + DatabaseSchema.TableDB.TABLE_NAME
            + " (" + DatabaseSchema.TableDB._ID + " INTEGER PRIMARY KEY, " +
            DatabaseSchema.TableDB.COLUMN_FIRSTNAME + " TEXT, " + DatabaseSchema.TableDB.COLUMN_LASTNAME
            + " TEXT)";

    private static String DELETE_ENTRIES = "DROP TABLE IF EXISTS "+DatabaseSchema.TableDB.TABLE_NAME;
    private static final int Databaseversion = 1;
    private static final String DATABASE_NAME = "Usernames2.db";


    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, Databaseversion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_ENTRIES);
        onCreate(db);
    }
}
