package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText username, password;
    private Button login;
    private static final String API_SIGNIN = "..../signin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.editTextTextUsername);
        password = (EditText) findViewById(R.id.editTextTextPassword);
        login = (Button) findViewById(R.id.button);

        login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                String usernamep = username.getText().toString();
                String passwordp = password.getText().toString();
                String[] parameters = new String[3];
                parameters[0] = usernamep;
                parameters[1] = passwordp;
                parameters[2] = API_SIGNIN;
                WebService temp = new WebService();
                temp.execute(parameters);
                break;
        }
    }

    private class WebService extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... inputs) {

            try{
                String username = inputs[0];
                String password = inputs[1];
                String apiurl = inputs[2];
                //String apiurl = "....../signin";
                JSONObject json = new JSONObject();
                json.put("username", "haminh1");
                json.put("password", "Hamin7273");
                String jsonstring = json.toString();
                URL url = new URL(apiurl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "utf-8"));
                writer.write(jsonstring);
                writer.flush();
                writer.close();
                if (urlConnection.getResponseCode() == 200){
                    BufferedReader bread = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String temp, responseString = "";
                    while ((temp = bread.readLine()) != null){
                        responseString += temp;
                    }

                    JSONObject readobj = new JSONObject();
                    readobj.put("Content", responseString);

                }

            }
            catch (Exception e){
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}