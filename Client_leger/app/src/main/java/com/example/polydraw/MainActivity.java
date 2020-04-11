package com.example.polydraw;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.polydraw.Socket.SocketIO;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.net.*;
import java.io.*;
import java.util.*;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class MainActivity extends AppCompatActivity {

    private Button enterAppButton;
    private EditText username;
    private Button signupButton;
    private EditText password;
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public String resultHttp;

    public Application app;

    String query_url = "players/login";

    public static String editTextString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        enterAppButton = (Button) findViewById(R.id.enterapp);
        password = (EditText) findViewById(R.id.password);
        username = (EditText) findViewById(R.id.username);
        signupButton = (Button) findViewById(R.id.signup);

        enterAppButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
               if(!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty()){
                   sendForm();

               }
            }
        } );

       signupButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);

            }
        });

    }

    public void sendForm() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Map<String, String> postData = new HashMap<>();
                    postData.put("username", username.getText().toString());
                    postData.put("password", password.getText().toString());
                    HttpPostLogin task = new HttpPostLogin(postData);
                    System.out.println(SocketIO.HTTP_URL+query_url);
                    task.execute(SocketIO.HTTP_URL+query_url);

                    String str = username.getText().toString();
                    editTextString=str;

                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        thread.start();
    }

    @Override
    public void onBackPressed() { }

    public class HttpPostLogin extends AsyncTask<String, Void, String> {
        JSONObject postData;
        public int status;
        public String token;
        public JSONObject player;

        public HttpPostLogin(Map<String, String> postData) {
            if (postData != null) {
                this.postData = new JSONObject(postData);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(token!=null){
                Intent intent = new Intent(MainActivity.this, Menu.class);
                startActivity(intent);
            }else{
                LoginErrorDialog loginErrorDialog = new LoginErrorDialog();
                loginErrorDialog.show(getSupportFragmentManager(), "loginErrorDialog");

            }

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);


                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("POST");

                if (this.postData != null) {
                    OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                    writer.write(postData.toString());
                    writer.flush();
                }

                final int statusCode = urlConnection.getResponseCode();

                if (statusCode == 200 || statusCode == 201) {
                    System.out.println(statusCode + ": Successful request");
//                    status = statusCode;
                    if (statusCode < 299) { // success
                        BufferedReader in = new BufferedReader(new InputStreamReader(
                                urlConnection.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        String data = response.toString();
                        Gson gson = new Gson();
                        JSONObject reader = new JSONObject(data);
                        player = reader.getJSONObject("player");
                        String result = player.toString();
                        System.out.println(result);
//                        JSONPlayer(player);
                        token = player.get("token").toString();
//                        getStatus(statusCode);


                    }
                } else {
                    System.out.println(statusCode + ": Something went wrong...");
//                    status = statusCode;
                    token = null;
                }


            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return token;
        }
    }

}
