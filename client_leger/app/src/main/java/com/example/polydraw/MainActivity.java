package com.example.polydraw;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity  {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.EditText);
    }

    public void onMessage(com.example.polydraw.Message receivedMessage){

    }

    public void sendMessage(View view){
        String message = editText.getText().toString();
        if(message.length() > 0){

        }
    }
}
