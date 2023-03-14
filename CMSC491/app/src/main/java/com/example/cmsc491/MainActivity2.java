package com.example.cmsc491;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    private TextView  mytextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mytextview = (TextView) findViewById(R.id.textView);
        String text = getIntent().getStringExtra("TextView");
        mytextview.setText(text);
    }
}