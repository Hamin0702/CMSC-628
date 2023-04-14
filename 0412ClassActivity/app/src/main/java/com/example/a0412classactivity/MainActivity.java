package com.example.a0412classactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DBHelper dbHelper = new DBHelper(this);

    private EditText myfirstname;
    private EditText mylastname;
    private Button myButton;
    private TextView myCount;

    private int count = 0;

    @Override
    public void onClick(View v) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        String firstname = myfirstname.getText().toString();
        String lastname = mylastname.getText().toString();
        values.put(DatabaseSchema.TableDB.COLUMN_FIRSTNAME, firstname);
        values.put(DatabaseSchema.TableDB.COLUMN_LASTNAME, lastname);

        //Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Username", null);
        //cursor.moveToFirst();
        //String count = cursor.getString(0);

        count++;
        String countDisplay = "" + count;
        myCount.setText(countDisplay);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myfirstname = (EditText) findViewById(R.id.editTextFirstName);
        mylastname = (EditText) findViewById(R.id.editTextLastName);
        myButton = (Button) findViewById(R.id.button);
        myCount = (TextView) findViewById(R.id.ediTextCount);

        myButton.setOnClickListener(this);


    }

}