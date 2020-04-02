package com.example.polydraw;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import yuku.ambilwarna.AmbilWarnaDialog; //https://codinginflow.com/tutorials/android/ambilwarna-color-picker-dialog

public class ModeSoloActivity extends AppCompatActivity {
    DrawingCanvas drawingCanvas;
    public GuessingCanvas guessingCanvas;
    ImageButton sendAnswer;
    TextView hints;
    EditText answer;
    private ImageView chatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        guessingCanvas = new GuessingCanvas(this,null);
        setContentView(R.layout.activity_mode_solo);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        initializeObject();
        eventListeners();

    }

    private void initializeObject(){
        guessingCanvas = (GuessingCanvas) findViewById(R.id.guessing);
        sendAnswer = (ImageButton) findViewById(R.id.sendAnswer);
        hints = (TextView) findViewById(R.id.hints);
        answer = (EditText) findViewById(R.id.answer);
        chatButton = (ImageView) findViewById(R.id.chatButton);

    }

    private void eventListeners() {
        sendAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChat();
            }
        });

    }

    public void openChat(){
        Intent intent = new Intent(this, ChatBoxActivity.class);
        startActivity(intent);
    }
}
