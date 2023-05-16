package com.example.homework3;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class PasswordResetActivity extends AppCompatActivity {

    private EditText username, verification, newpassword;
    private Button verify, reset;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.passwordreset);

        username = (EditText) findViewById(R.id.editTextUsername);
        verification = (EditText) findViewById(R.id.editTextVerificationCode);
        newpassword = (EditText) findViewById(R.id.editTextNewPassword);
        verify = (Button) findViewById(R.id.verificationButton);
        reset = (Button) findViewById(R.id.passwordresetButton);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] parameters = new String[1];
                parameters[0] = username.getText().toString();
                Verification temp = new Verification();
                temp.execute(parameters);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] parameters = new String[3];
                parameters[0] = username.getText().toString();
                parameters[1] = verification.getText().toString();
                parameters[2] = newpassword.getText().toString();
                PasswordReset temp = new PasswordReset();
                temp.execute(parameters);
            }
        });

    }

    private class Verification extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            String apiurl = "https://wn9q183vgd.execute-api.us-east-1.amazonaws.com/prod/user/forgotpassword";

            try{

                // Passing in the inputs as a json
                JSONObject json = new JSONObject();
                json.put("username", strings[0]);
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

    private class PasswordReset extends AsyncTask<String, Void, Integer>{

        @Override
        protected Integer doInBackground(String... strings) {
            String apiurl = "https://wn9q183vgd.execute-api.us-east-1.amazonaws.com/prod/user/resetpassword";

            try{

                // Passing in the inputs as a json
                JSONObject json = new JSONObject();
                json.put("username", strings[0]);
                json.put("verification", strings[1]);
                json.put("newpassword", strings[2]);
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
