package com.example.storageapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static String filename = "Preferencefile.txt";
    public static String sharedprefname = "Preferences.txt";
    private EditText username, password;
    private TextView usernamed, passwordd;
    private Button submit, retrieve;

    private String temppassword, tempusername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        usernamed = (TextView) findViewById(R.id.textviewusername);
        passwordd = (TextView) findViewById(R.id.textviewpassword);
        submit = (Button) findViewById(R.id.submit);
        retrieve = (Button) findViewById(R.id.retrieve);
        submit.setOnClickListener(this);
        retrieve.setOnClickListener(this);
    }

    private String readFromSharedPreferences(String key){
        SharedPreferences sharedpref = getSharedPreferences(sharedprefname, MODE_PRIVATE);
        return sharedpref.getString(key, "Default");
    }

    private void writeToSharedPreferences(String key, String value){
        SharedPreferences.Editor sharedpref = getSharedPreferences(sharedprefname, MODE_PRIVATE).edit();
        sharedpref.putString(key, value);
        sharedpref.commit();
    }

    private void writeToInternalFile(String data){
        try {
            FileOutputStream fos = openFileOutput(filename, MODE_APPEND);
            PrintWriter print = new PrintWriter(fos);
            print.println(data);
            print.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void readFromInternalFile(){
        try {
            FileInputStream fin = openFileInput(filename);
            InputStreamReader is = new InputStreamReader(fin);
            BufferedReader bread = new BufferedReader(is);
            this.tempusername = bread.readLine();
            this.temppassword = bread.readLine();
            bread.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                //writeToSharedPreferences("username", username.getText().toString());
                //writeToSharedPreferences("password", password.getText().toString());

                writeToInternalFile(username.getText().toString());
                writeToInternalFile(password.getText().toString());
                break;
            case R.id.retrieve:
                //usernamed.setText(readFromSharedPreferences("username"));
                //passwordd.setText(readFromSharedPreferences("password"));

                readFromInternalFile();
                usernamed.setText(this.tempusername);
                passwordd.setText(this.temppassword);
                break;
        }
    }
}