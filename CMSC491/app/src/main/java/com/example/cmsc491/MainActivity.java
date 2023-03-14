package com.example.cmsc491;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int requestCodeActivity = 2;
    private EditText myedittext;
    private Button myButton, myButton2;
    private TextView myTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myedittext = (EditText) findViewById(R.id.editTextInput);
        myButton = (Button) findViewById(R.id.submitButton);
        myTextview = (TextView) findViewById(R.id.displayText);
        myButton2 = (Button) findViewById(R.id.button2);

        myButton.setOnClickListener(this);
        myButton2.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.button2:
                // myTextview.setText("Hello World!");
                String text1 = myedittext.getText().toString();
                Intent myIntent = new Intent(MainActivity.this, MainActivity2.class);
                myIntent.putExtra("TextView", text1);
                // Intent myIntent = new Intent(Intent.ACTION_VIEW);
                //myIntent.setData(Uri.parse("https://www.umbc.edu"));
                // startActivity(myIntent);
                startActivityForResult(myIntent, requestCodeActivity);
                break;
            case R.id.submitButton:
                String text = myedittext.getText().toString();
                myTextview.setText(text);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == requestCodeActivity){
            if(resultCode == RESULT_OK){

            }
        }
    }
}