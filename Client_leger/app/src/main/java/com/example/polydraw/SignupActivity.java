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

    public String IpAddress;
    public String url = "https://192.168.2.108:5050"; //"https://fais-moi-un-dessin.herokuapp.com/"

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
                        sendForm();

                    }
                }
            }
        });

    }

    public void sendForm() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String query_url = "https://fais-moi-un-dessin.herokuapp.com/players";
                try {
                    /*JSONObject data = new JSONObject();
                    data.put("firstName", name.getText().toString());
                    data.put("lastName", surname.getText().toString());
                    data.put("username", username.getText().toString());
                    data.put("password", password.getText().toString());
                    String json = data.toString();
                    System.out.println(json);

                    sendPOST(json);*/

                    Map<String, String> postData = new HashMap<>();
                    postData.put("firstName", name.getText().toString());
                    postData.put("lastName", surname.getText().toString());
                    postData.put("username", username.getText().toString());
                    postData.put("password", password.getText().toString());
                    HttpPost task = new HttpPost(postData);
                    task.execute(query_url);

                    /*URL url = new URL(query_url);
                    System.out.println(url);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestMethod("POST");

                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(data.toString());
                    os.flush();
                    os.close();
                    conn.disconnect();*/
                    /*Intent intent = new Intent(SignupActivity.this, Menu.class);
                    startActivity(intent);*/
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        thread.start();
    }

    private static void sendPOST(String string) throws IOException {
        URL obj = new URL("https://fais-moi-un-dessin.herokuapp.com/players");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        System.out.println(string);

        // For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(string.getBytes());
        System.out.println("allo");
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("POST request not worked");
        }
    }


}
