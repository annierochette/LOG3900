package com.example.polydraw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;



public class MainActivity extends AppCompatActivity {

    private Button enterAppButton;
    private EditText username;
    private Button signupButton;
    private EditText password;
    public static final String USERNAME = "username";
    public static final String IP_ADDRESS = "ipAddress";
    public static final String PASSWORD = "password";
    String query_url = "https://192.168.2.132:5050/players/login";

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
                String query_url = "https://fais-moi-un-dessin.herokuapp.com";

               if(!username.getText().toString().isEmpty()){

                   System.out.println(username);

                   Map<String, String> postData = new HashMap<>();
                   postData.put("username", username.getText().toString());
                   postData.put("password", password.getText().toString());
                   HttpPost task = new HttpPost(postData);
                   System.out.println(task);
                   task.execute(query_url);

                   Intent i = new Intent(MainActivity.this, Menu.class);

                   Bundle extras = new Bundle();
                   extras.putString(USERNAME, username.getText().toString());
                   extras.putString(PASSWORD, password.getText().toString());

                   i.putExtras(extras);


                   /*try {
                       JSONObject data = new JSONObject();
                       data.put("username", username.getText().toString());
                       data.put("password", password.getText().toString());
                       String json = data.toString();
                       System.out.println(json);

                       URL url = new URL(query_url + "/players/login");
                       System.out.println(url);

                       HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                       conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                       conn.setDoOutput(true);
                       conn.setDoInput(true);
                       conn.setRequestMethod("POST");

                       DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                       System.out.println("allo4");

                       os.writeBytes(data.toString());
                       System.out.println("allo5");
                       os.flush();
                       os.close();
                       System.out.println("allo6");

                       conn.disconnect();

                       startActivity(i);
                   } catch (Exception e) {
                       System.out.println(e);
                   }

//                   startActivity(i);*/

                   AsyncTask asyncHttpPost = new AsyncTask(extras, i);
                   asyncHttpPost.execute();
                   startActivity(i);
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

    private class AsyncTask extends android.os.AsyncTask <String, Void, Void>{
        private Bundle data;
        private Intent intent;

        public AsyncTask(Bundle mData, Intent mIntent) {
            data = mData;
            intent = mIntent;
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                JSONObject data = new JSONObject();
                data.put("username", username);
                data.put("password", password);
                String json = data.toString();
                System.out.println(json);

                URL url = new URL(query_url + "/players/login");
                System.out.println(url);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                System.out.println("allo4");

                os.writeBytes(data.toString());
                System.out.println("allo5");
                os.flush();
                os.close();
                System.out.println("allo6");

                conn.disconnect();

                startActivity(intent);
            } catch (Exception e) {
                System.out.println(e);
            }

            return null;

        }
    }

}
