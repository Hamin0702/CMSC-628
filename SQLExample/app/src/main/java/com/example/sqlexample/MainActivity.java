package com.example.sqlexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper = new DBHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //addTableEntry("Hamin", "Han");
        //readTableEntries();
        //updateTable();
        readTableEntries();
        deleteTable();
    }

    private void deleteTable() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DatabaseSchema.TableDB.COLUMN_LASTNAME + " LIKE ?";
        String[] selectArgs = {"Hamin"};

        int deleteRows = db.delete(DatabaseSchema.TableDB.TABLE_NAME, selection, selectArgs);
        Log.i("NUMBERDELETED", new Integer(deleteRows).toString());
    }
    private void updateTable(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseSchema.TableDB.COLUMN_FIRSTNAME, "John");

        String selection = DatabaseSchema.TableDB.COLUMN_FIRSTNAME + " LIKE ?";
        String[] selectArgs = {"Hamin"};

        int count = db.update(DatabaseSchema.TableDB.TABLE_NAME, values, selection, selectArgs);
        Log.i("COUNT", new Integer(count).toString());
    }

    private void addTableEntry(String firstname, String lastname){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseSchema.TableDB.COLUMN_FIRSTNAME, firstname);
        values.put(DatabaseSchema.TableDB.COLUMN_LASTNAME, lastname);
        long rowID = db.insert(DatabaseSchema.TableDB.TABLE_NAME, null, values);
        Log.i("ROWID", new Long(rowID).toString());
    }

    private void readTableEntries(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection  = {
                BaseColumns._ID,
                DatabaseSchema.TableDB.COLUMN_FIRSTNAME,
                DatabaseSchema.TableDB.COLUMN_LASTNAME
        };
        String selection = DatabaseSchema.TableDB.COLUMN_FIRSTNAME + " = ?";
        String[] selectionArgs = {"Hamin"};

        String sortOrder = DatabaseSchema.TableDB.COLUMN_FIRSTNAME + " DESC";

        Cursor cursor = db.query(DatabaseSchema.TableDB.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

        while(cursor.moveToNext()){
            String[] names = cursor.getColumnNames();
            for(int i=0;i<names.length;i++){
                int index = cursor.getColumnIndex(names[i]);
                String value = cursor.getString(index);
                Log.i("Values", value);
            }
        }
        cursor.close();

    }
}