package com.example.feb15classactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Start2 extends AppCompatActivity {

    private TextView myTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start2);

        myTextView = (TextView) findViewById(R.id.textViewS2);
        String text = getIntent().getStringExtra("TextView");
        myTextView.setText(text);
    }
}