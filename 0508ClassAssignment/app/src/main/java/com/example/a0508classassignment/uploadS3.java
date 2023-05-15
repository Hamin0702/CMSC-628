package com.example.a0508classassignment;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class uploadS3 extends AsyncTask<String, Void, Void> {

    private File textfile;
    private Context mContext;

    public uploadS3(Context context) {
        mContext = context;

        File fileDir = mContext.getFilesDir();
        textfile = new File(fileDir, "text.txt");

        String text = "05/08 In Class Activity Reagan Armstrong & Hamin Han";
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(textfile);
            fileWriter.write(text);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Void doInBackground(String... strings) {

        String presigneduri = strings[0];

        try {
            URL url = new URL(presigneduri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("Content-Length", String.valueOf(textfile.length()));

            OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            InputStream in = new FileInputStream(textfile);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.flush();
            out.close();
            in.close();
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
       return null;
    }
}
