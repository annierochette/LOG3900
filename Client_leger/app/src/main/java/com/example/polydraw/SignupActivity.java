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

public class SignupActivity extends AppCompatActivity {

    private Button signup;
    private EditText name;
    private EditText surname;
    private EditText username;
    private EditText password;
    private EditText passwordConfirmation;

    public String IpAddress;
    public String url = "http://" + IpAddress + ":5050";

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


        NewUser newUser = new NewUser(name.getText().toString(),surname.getText().toString(),username.getText().toString(), password.getText().toString());

        signup.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(!name.getText().toString().trim().isEmpty() && !surname.getText().toString().trim().isEmpty() && !username.getText().toString().trim().isEmpty() && !password.getText().toString().trim().isEmpty() && !passwordConfirmation.getText().toString().trim().isEmpty()) {
                    if(password.getText().toString().equals(passwordConfirmation.getText().toString())) {
                        String query_url = "http://localhost:5050/players";
                        try {
                            JSONObject data = new JSONObject();
                            data.put("name", name.getText().toString());
                            data.put("surname", surname.getText().toString());
                            data.put("username", username.getText().toString());
                            data.put("password", password.getText().toString());
                            String json = data.toString();
                            URL url = new URL(query_url);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setConnectTimeout(5000);
                            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                            conn.setDoOutput(true);
                            conn.setDoInput(true);
                            conn.setRequestMethod("POST");
                            OutputStream os = conn.getOutputStream();
                            os.write(json.getBytes("UTF-8"));
                            os.close();


                            conn.disconnect();
                        } catch (Exception e) {
                            System.out.println(e);
                        }

                        /*Intent intent = new Intent(SignupActivity.this, Menu.class);
                        startActivity(intent);*/
                    }
                }
            }
        });

    }

    public void sendForm(String url, @Nullable JSONObject userInfo){


    }

}
