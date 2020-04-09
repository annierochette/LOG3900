package com.example.polydraw;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.polydraw.Socket.SocketIO.HTTP_URL;

public class HttpGet extends AsyncTask<String, Void, Void> {
    JSONObject getData;
    public HttpGet(Map<String, String> getData) {
        if (getData != null) {
            this.getData = new JSONObject(getData);
        }
    }

    @Override
    protected Void doInBackground(String... params) {

        try {
            URL url = new URL(params[0]);

            // Create the urlConnection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("GET");


            int statusCode = urlConnection.getResponseCode();
            System.out.println(statusCode);

            if(statusCode == 201){
                System.out.println("Successful post request");
            }

            else{
                System.out.println("Something went wrong...");
            }


        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
        return null;
    }

}
