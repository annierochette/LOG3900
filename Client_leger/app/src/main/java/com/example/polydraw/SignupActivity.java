package com.example.polydraw;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        passwordConfirmation = (EditText) findViewById(R.id.passwordConfirmation);
        signup = (Button) findViewById(R.id.signup);


        NewUser newUser = new NewUser(name.getText().toString(),surname.getText().toString(),username.getText().toString(), password.getText().toString(), passwordConfirmation.getText().toString());

        signup.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(!name.getText().toString().trim().isEmpty() && !surname.getText().toString().trim().isEmpty() && !username.getText().toString().trim().isEmpty() && !password.getText().toString().trim().isEmpty() && !passwordConfirmation.getText().toString().trim().isEmpty()) {
                    if(password.getText().toString().equals(passwordConfirmation.getText().toString())) {

                        JSONObject data = new JSONObject();

                        try{

                            data.put("name", name.getText().toString());
                            data.put("surname", surname.getText().toString());
                            data.put("username", username.getText().toString());
                            data.put("password", password.getText().toString());
                            data.put("passwordConfirmation", passwordConfirmation.getText().toString());

                            try{
                                URL url = new URL("http://" + IpAddress + ":5050");
                                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                                httpCon.setRequestMethod("POST");

                                httpCon.setRequestProperty("Content-Type", "application/json; utf-8");
                                httpCon.setRequestProperty("Accept", "application/json");
                                httpCon.setDoOutput(true);
                                String jsonInputString = data.toString();

                                try(OutputStream os = httpCon.getOutputStream()) {
                                    byte[] input = jsonInputString.getBytes("utf-8");
                                    os.write(input, 0, input.length);
                                }

                                try(BufferedReader br = new BufferedReader(
                                        new InputStreamReader(httpCon.getInputStream(), "utf-8"))) {
                                    StringBuilder response = new StringBuilder();
                                    String responseLine = null;
                                    while ((responseLine = br.readLine()) != null) {
                                        response.append(responseLine.trim());
                                    }
                                    System.out.println(response.toString());
                                }

                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }
                        }

                        catch(JSONException e){
                            e.printStackTrace();
                        }
                        /*Intent intent = new Intent(SignupActivity.this, Menu.class);
                        startActivity(intent);*/
                    }
                }
            }
        });


    }

    public void sendForm(String url, JSONObject userInfo){

        try{


        }

        catch(Exception e){
            e.printStackTrace();
        }


    }

}
