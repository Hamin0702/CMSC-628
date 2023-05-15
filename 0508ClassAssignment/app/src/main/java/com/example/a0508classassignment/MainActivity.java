package com.example.a0508classassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.Request;
import com.amazonaws.Response;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText filename;
    private EditText user;
    private EditText filetype;
    private Button submit;

    private String apiurl = "https://epct80tttc.execute-api.us-east-1.amazonaws.com/dev/upload";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filename = (EditText) findViewById(R.id.editTextFileName);
        user = (EditText) findViewById(R.id.editTextUser);
        filetype = (EditText) findViewById(R.id.editTextFileType);
        submit = (Button) findViewById(R.id.submitbutton);

        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String myFilename = filename.getText().toString();
        String myUser = user.getText().toString();
        String myFiletype = filetype.getText().toString();

        uploadJSON task = new uploadJSON(this);
        task.execute(apiurl, myUser, myFilename);
    }

}