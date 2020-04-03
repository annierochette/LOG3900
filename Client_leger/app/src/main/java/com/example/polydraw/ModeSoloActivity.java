package com.example.polydraw;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
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
    public GuessingCanvas guessingCanvas;
    ImageButton sendAnswer;
    TextView hints;
    EditText answer;
    private ImageView chatButton;
    private CountDownTimer timer;
    private long remainingTime = 60000; //1 minute
    private TextView  chrono;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        guessingCanvas = new GuessingCanvas(this,null);
        setContentView(R.layout.activity_mode_solo);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        initializeObject();
        eventListeners();
        startTimer();

    }

    private void initializeObject(){
        guessingCanvas = (GuessingCanvas) findViewById(R.id.guessing);
        sendAnswer = (ImageButton) findViewById(R.id.sendAnswer);
        hints = (TextView) findViewById(R.id.hints);
        answer = (EditText) findViewById(R.id.answer);
        chatButton = (ImageView) findViewById(R.id.chatButton);
        chrono = (TextView) findViewById(R.id.chronometer);
        button = (Button) findViewById(R.id.button);

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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTime();
            }
        });

    }

    public void openChat(){
        Intent intent = new Intent(this, ChatBoxActivity.class);
        startActivity(intent);
    }
    //https://www.youtube.com/watch?v=zmjfAcnosS0
    public void startTimer(){
        timer = new CountDownTimer(remainingTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;
                updateTimer();

            }

            @Override
            public void onFinish() {
                openDialog();

            }
        }.start();

    }

    public void updateTimer(){
        int minutes = (int) remainingTime / 60000;
        int seconds = (int) remainingTime %60000/1000;
        String timeLeft;
        timeLeft = "" + minutes;
        timeLeft += ":";
        if(seconds < 10){
            timeLeft += "0";
        }
        timeLeft += seconds;

        chrono.setText(timeLeft);
    }

    public void addTime(){
        timer.cancel();
        remainingTime += 5000;

        timer = new CountDownTimer(remainingTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;
                updateTimer();

            }

            @Override
            public void onFinish() {
                openDialog();

            }
        }.start();

    }

    public void openDialog() {
        postSoloGameDialog postSoloGameDialogDialog = new postSoloGameDialog();
        postSoloGameDialogDialog.show(getSupportFragmentManager(), "postSoloGameDialogDialog");
    }
}
