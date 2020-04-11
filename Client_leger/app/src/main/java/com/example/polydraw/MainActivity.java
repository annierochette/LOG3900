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

import com.example.polydraw.Socket.SocketIO;

import java.net.*;
import java.io.*;
import java.util.*;



public class MainActivity extends AppCompatActivity {

    private Button enterAppButton;
    private EditText username;
    private Button signupButton;
    private EditText password;
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

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
                    HttpPost task = new HttpPost(postData);
                    System.out.println(SocketIO.HTTP_URL+query_url);
                    task.execute(SocketIO.HTTP_URL+query_url);

                    String str = username.getText().toString();
                    editTextString=str;

                    Intent intent = new Intent(MainActivity.this, Menu.class);
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
