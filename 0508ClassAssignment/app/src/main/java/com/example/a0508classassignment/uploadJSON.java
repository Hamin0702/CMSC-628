package com.example.a0508classassignment;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class uploadJSON extends AsyncTask<String, Void, String> {
    private Context mContext;

    public uploadJSON(Context context) {
        mContext = context;
    }
    @Override
    protected String doInBackground(String... strings) {
        String apiurl = strings[0];
        String username = strings[1];
        String filename = strings[2];

        String returnedlink = "";
        try{
            URL url = new URL(apiurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userName", username);
            jsonObject.put("fileName", filename);
            String inputJSON = jsonObject.toString();

            OutputStream os = conn.getOutputStream();
            os.write(inputJSON.getBytes());
            os.flush();
            os.close();

            // Get the returned link
            InputStream inputStream = conn.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            String result = stringBuilder.toString();
            inputStream.close();
            conn.disconnect();

            JSONObject resultJSON = new JSONObject(result);
            returnedlink = resultJSON.getString("body");


        } catch (Exception e){
            e.printStackTrace();
        }
        return returnedlink;
    }

    @Override
    protected void onPostExecute(String returnLink) {
        super.onPostExecute(returnLink);

        try{
            uploadS3 task = new uploadS3(mContext);
            task.execute(returnLink);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
