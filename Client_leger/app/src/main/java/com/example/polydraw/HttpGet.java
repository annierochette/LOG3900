package com.example.polydraw;

import android.os.AsyncTask;
import android.util.Log;

import com.example.polydraw.Socket.SocketIO;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.polydraw.Socket.SocketIO.HTTP_URL;

public class HttpGet extends AsyncTask<String, Void, Void> {
    public HttpGet() {

    }

    @Override
    protected Void doInBackground(String... params) {

        try {
            URL url = new URL(params[0]);

            // Create the urlConnection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
//            urlConnection.setHeader("token", "Token value goes here");
            urlConnection.setRequestProperty("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InNub29weSIsImlhdCI6MTU4NjU3NDUwNH0.hBXym3o793NljYhJO6_ef3aBLI5SeMvRCcDICyTpnGk");
            urlConnection.setRequestMethod("GET");


            int statusCode = urlConnection.getResponseCode();
            System.out.println(statusCode);

            if (statusCode < 299) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
            }


        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
        return null;
    }

}
