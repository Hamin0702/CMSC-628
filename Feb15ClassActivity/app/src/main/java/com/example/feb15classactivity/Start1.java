package com.example.feb15classactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Start1 extends AppCompatActivity {

    private TextView myTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start1);

        myTextView = (TextView) findViewById(R.id.textViewS1);
        String text = getIntent().getStringExtra("TextView");
        myTextView.setText(text);
    }
}