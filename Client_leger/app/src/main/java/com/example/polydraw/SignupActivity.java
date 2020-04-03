package com.example.polydraw;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.net.*;
import java.io.*;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private Button signup;
    private EditText name;
    private EditText surname;
    private EditText username;
    private EditText password;
    private EditText passwordConfirmation;
    private Button backButton;


    public String IpAddress;
    public String url = "https://fais-moi-un-dessin.herokuapp.com"; //"https://fais-moi-un-dessin.herokuapp.com/"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        passwordConfirmation = (EditText) findViewById(R.id.passwordConfirmation);
        signup = (Button) findViewById(R.id.signup);
        backButton = (Button) findViewById(R.id.backButton);


        NewUser newUser = new NewUser(name.getText().toString(),surname.getText().toString(),username.getText().toString(), password.getText().toString());

        signup.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(!name.getText().toString().trim().isEmpty() && !surname.getText().toString().trim().isEmpty() && !username.getText().toString().trim().isEmpty() && !password.getText().toString().trim().isEmpty() && !passwordConfirmation.getText().toString().trim().isEmpty()) {
                    if(password.getText().toString().equals(passwordConfirmation.getText().toString())) {
                        sendForm();

                    }
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToLogin();
            }
        });

    }

    public void sendForm() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String query_url = url;
                try {

                    Map<String, String> postData = new HashMap<>();
                    postData.put("firstName", name.getText().toString());
                    postData.put("lastName", surname.getText().toString());
                    postData.put("username", username.getText().toString());
                    postData.put("password", password.getText().toString());
                    HttpPost task = new HttpPost(postData);
                    task.execute(query_url);

                    Intent intent = new Intent(SignupActivity.this, Menu.class);
                    startActivity(intent);

                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        thread.start();
    }

    public void backToLogin(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() { }

}
