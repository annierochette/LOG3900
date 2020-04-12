package com.example.polydraw;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.net.*;
import java.io.*;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.polydraw.Socket.SocketIO;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SignupActivity extends AppCompatActivity {

    private Button signup;
    private EditText name;
    private EditText surname;
    private EditText username;
    private EditText password;
    private EditText passwordConfirmation;
    private Button backButton;

    String query_url = "players";

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
                    }  else{
                        SignupConfirmation signupConfirmation = new SignupConfirmation();
                        signupConfirmation.show(getSupportFragmentManager(), "signupConfirmation");

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
                try {

                    Map<String, String> postData = new HashMap<>();
                    postData.put("firstName", name.getText().toString());
                    postData.put("lastName", surname.getText().toString());
                    postData.put("username", username.getText().toString());
                    postData.put("password", password.getText().toString());
                    HttpPostSignup task = new HttpPostSignup(postData);
                    task.execute(SocketIO.HTTP_URL+query_url);


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

    public class HttpPostSignup extends AsyncTask<String, Void, String> {
        JSONObject postData;
        public String status;
        public String token;
        public JSONObject player;
        public HttpPostSignup(Map<String, String> postData) {
            if (postData != null) {
                this.postData = new JSONObject(postData);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result.equals("201")){
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                intent.putExtra("player", result);
                startActivity(intent);
            }else{
                SignupError signupError = new SignupError();
                signupError.show(getSupportFragmentManager(), "signupError");

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

                if(statusCode == 200||statusCode == 201){
                    System.out.println(statusCode + ": Successful request");
                    status = "success";
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
                        player  = reader.getJSONObject("player");
                        token = player.get("token").toString();

                    }
                }

                else{
                    System.out.println(statusCode + ": Something went wrong...");
                    status = "failed";
                }


            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return status;
        }

    }

}
