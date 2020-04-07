package com.example.polydraw;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.*;
import java.io.*;
import java.util.*;



public class MainActivity extends AppCompatActivity {

    private Button enterAppButton;
    private EditText username;
    private Button signupButton;
    private EditText password;
    public static final String USERNAME = "username";
    public static final String IP_ADDRESS = "ipAddress";
    public static final String PASSWORD = "password";

    public Application app;

    String query_url = "https://fais-moi-un-dessin.herokuapp.com/players/login";

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
                String query_url = "http://192.168.2.243:5050";
                try {

                    Map<String, String> postData = new HashMap<>();
                    postData.put("username", username.getText().toString());
                    postData.put("password", password.getText().toString());
                    HttpPost task = new HttpPost(postData);
                    task.execute(query_url);

                    String str = username.getText().toString();
                    editTextString=str;

                    Intent intent = new Intent(MainActivity.this, Menu.class);
//                    intent.putExtra("username", username.getText().toString());
                    startActivity(intent);

                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        thread.start();
    }

    @Override
    public void onBackPressed() { }

}
