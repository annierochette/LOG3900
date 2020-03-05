package com.example.polydraw;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

//source: https://medium.com/@lewisjkl/android-httpurlconnection-with-asynctask-tutorial-7ce5bf0245cd

public class HttpPost extends AsyncTask<String, Void, Void> {
    // This is the JSON body of the post
    JSONObject postData;// This is a constructor that allows you to pass in the JSON body
    public HttpPost(Map<String, String> postData) {
        if (postData != null) {
            this.postData = new JSONObject(postData);
        }
    }

    // This is a function that we are overriding from AsyncTask. It takes Strings as parameters because that is what we defined for the parameters of our async task
    @Override
    protected Void doInBackground(String... params) {

        try {
            // This is getting the url from the string we passed in
            URL url = new URL(params[0]);

            // Create the urlConnection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            System.out.println("post req");
            System.out.println(this.postData);

            // Send the post body
            if (this.postData != null) {
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(postData.toString());
                writer.flush();
                System.out.println("send req");
            }

            int statusCode = urlConnection.getResponseCode();
            System.out.println(statusCode);

            if (statusCode ==  200) {

                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

                String response = inputStream.toString();
                System.out.println(response);

                // From here you can convert the string to JSON with whatever JSON parser you like to use               // After converting the string to JSON, I call my custom callback. You can follow this process too, or you can implement the onPostExecute(Result) method            } else {
                // Status code is not 200
                // Do something to handle the error
            }


        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
        return null;
    }
}
