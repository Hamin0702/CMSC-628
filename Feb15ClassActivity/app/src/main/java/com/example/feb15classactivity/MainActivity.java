package com.example.feb15classactivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int requestCodeS1 = 1;
    private static final int requestCodeS2 = 2;
    private EditText myEditText;
    private Button myButton;
    private TextView myTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myEditText = (EditText) findViewById(R.id.mainEditText);
        myButton = (Button) findViewById(R.id.mainButton);
        myTextView = (TextView) findViewById(R.id.mainTextView);

        myButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.mainButton:
                String inputText = myEditText.getText().toString();
                if(inputText.equals("Start: 1")){
                    Intent myIntent = new Intent(MainActivity.this, Start1.class);
                    myIntent.putExtra("TextView", inputText);
                    startActivityForResult(myIntent, requestCodeS1);
                } else if (inputText.equals("Start: 2")) {
                    Intent myIntent = new Intent(MainActivity.this, Start2.class);
                    myIntent.putExtra("TextView", inputText);
                    startActivityForResult(myIntent, requestCodeS2);
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == requestCodeS1){
            //if(resultCode == RESULT_OK){
                myTextView.setText("Start 1 Ended");
            //}
        }
        if(requestCode == requestCodeS2){
            //if(resultCode == RESULT_OK){
                myTextView.setText("Start 2 Ended");
        }
    }
}