package com.example.polydraw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button enterAppButton;
    private EditText username;
    private Button signupButton;
    private EditText password;
    public static final String USERNAME = "username";
    public static final String IP_ADDRESS = "ipAddress";
    public static final String PASSWORD = "password";

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
                String query_url = "https://192.168.2.132:5050/players/login";

               if(!username.getText().toString().isEmpty()){

                   System.out.println(username);

                   Map<String, String> postData = new HashMap<>();
                   postData.put("username", username.getText().toString());
                   postData.put("password", password.getText().toString());
                   HttpPost task = new HttpPost(postData);
                   System.out.println(task);
                   task.execute(query_url);

                   Intent i = new Intent(MainActivity.this, Menu.class);

                   /*Bundle extras = new Bundle();
                   extras.putString(USERNAME, username.getText().toString());
                   extras.putString(PASSWORD, password.getText().toString());

                   i.putExtras(extras);*/

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

}
