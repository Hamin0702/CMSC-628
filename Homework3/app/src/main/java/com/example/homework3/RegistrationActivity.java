package com.example.homework3;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegistrationActivity extends AppCompatActivity {

    private EditText username, password, email, confirmation;
    private Button register, confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        username = (EditText) findViewById(R.id.editTextTextID);
        password = (EditText) findViewById(R.id.editTextTextPassword);
        email = (EditText) findViewById(R.id.editTextEmail);
        confirmation = (EditText) findViewById(R.id.editTextConfirmation);
        register = (Button) findViewById(R.id.registrationButton);
        confirm = (Button) findViewById(R.id.confirmationButton);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] parameters = new String[3];
                parameters[0] = username.getText().toString();
                parameters[1] = password.getText().toString();
                parameters[2] = email.getText().toString();
                Registration temp = new Registration();
                temp.execute(parameters);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] parameters = new String[2];
                parameters[0] = username.getText().toString();
                parameters[1] = confirmation.getText().toString();
                Confirmation temp = new Confirmation();
                temp.execute(parameters);
            }
        });

    }

    private class Registration extends AsyncTask<String, Void, Integer>{

        @Override
        protected Integer doInBackground(String... strings) {
            String apiurl = "https://wn9q183vgd.execute-api.us-east-1.amazonaws.com/prod/user/register";

            try{

                // Passing in the inputs as a json
                JSONObject json = new JSONObject();
                json.put("username", strings[0]);
                json.put("password", strings[1]);
                json.put("email", strings[2]);
                String jsonstring = json.toString();

                //HTTP Connection to the api
                URL url = new URL(apiurl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "utf-8"));
                writer.write(jsonstring);
                writer.flush();
                writer.close();

                // Returns the response code
                return urlConnection.getResponseCode();

            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    private class Confirmation extends AsyncTask<String, Void, Integer>{

        @Override
        protected Integer doInBackground(String... strings) {
            String apiurl = "https://wn9q183vgd.execute-api.us-east-1.amazonaws.com/prod/user/confirmation";

            try{

                // Passing in the inputs as a json
                JSONObject json = new JSONObject();
                json.put("username", strings[0]);
                json.put("confirmation", strings[1]);
                String jsonstring = json.toString();

                //HTTP Connection to the api
                URL url = new URL(apiurl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "utf-8"));
                writer.write(jsonstring);
                writer.flush();
                writer.close();

                // Returns the response code
                return urlConnection.getResponseCode();

            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
