package com.example.polydraw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private EditText username;
    public static final String USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.enterchat);
        username = (EditText) findViewById(R.id.username);

        button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

               if(!username.getText().toString().isEmpty()){
                   Intent i = new Intent(MainActivity.this, ChatBoxActivity.class);
                   i.putExtra(USERNAME, username.getText().toString());

                   startActivity(i);
               }
            }
        } );


    }


}
