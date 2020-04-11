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
        public String token;
        public JSONObject player;
        public String playerString;
        public String playerName;
        public String playerLastName;
        public String playerUsername;
        private String result;

        public HttpPostLogin(Map<String, String> postData) {
            if (postData != null) {
                this.postData = new JSONObject(postData);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result!=null){
                Intent intent = new Intent(MainActivity.this, Menu.class);

                try{
                    JSONObject reader = new JSONObject(result);

                    String token1 = reader.get("token").toString();;
                    String username1 = reader.get("username").toString();
                    String lastName1 = reader.get("lastName").toString();
                    String firstName1 = reader.get("firstName").toString();

                    System.out.println("token try catch pour intent main "+token1);

                    intent.putExtra("token", token1);
                    intent.putExtra("username", username1);
                    intent.putExtra("lastName", lastName1);
                    intent.putExtra("firstName", firstName1);
                } catch(Exception e){
                    e.printStackTrace();
                }

                intent.putExtra("player", result);
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
                        JSONObject reader = new JSONObject(data);
                        player = reader.getJSONObject("player");
                        playerString = player.toString();
                        result = playerString;

                        /*token = player.get("token").toString();
                        playerUsername = player.get("username").toString();
                        playerLastName = player.get("lastName").toString();
                        playerName = player.get("firstName").toString();*/

                    }
                } else {
                    System.out.println(statusCode + ": Something went wrong...");
                    result = null;
                }


            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result;
        }
    }

}
