package com.example.homework3;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username = (EditText) findViewById(R.id.editTextLoginID);
        password = (EditText) findViewById(R.id.editTextLoginPassword);
        login = (Button) findViewById(R.id.loginButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] parameters = new String[2];
                parameters[0] = username.getText().toString();
                parameters[1] = password.getText().toString();
                Login temp = new Login();
                temp.execute(parameters);
            }
        });
    }

    private class Login extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            String apiurl = "https://wn9q183vgd.execute-api.us-east-1.amazonaws.com/prod/user/signin";

            try{

                // Passing in the inputs as a json
                JSONObject json = new JSONObject();
                json.put("username", strings[0]);
                json.put("password", strings[1]);
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
                int code = urlConnection.getResponseCode();
                return urlConnection.getResponseCode();

            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer responseCode) {
            if(responseCode != null){
                if(responseCode == 200){
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                } else if (responseCode == 401) {
                    Toast.makeText(getApplicationContext(), "Login Not Successful: Invalid credentials", Toast.LENGTH_SHORT).show();
                } else if (responseCode == 402) {
                    Toast.makeText(getApplicationContext(), "Login Not Successful: User not found", Toast.LENGTH_SHORT).show();
                } else if (responseCode == 403) {
                    Toast.makeText(getApplicationContext(), "Login Not Successful: User not confirmed", Toast.LENGTH_SHORT).show();
                } else if (responseCode == 500) {
                    Toast.makeText(getApplicationContext(), "Login Not Successful: An error occured", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
