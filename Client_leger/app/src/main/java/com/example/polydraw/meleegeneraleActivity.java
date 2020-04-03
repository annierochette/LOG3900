package com.example.polydraw;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

import yuku.ambilwarna.AmbilWarnaDialog; //https://codinginflow.com/tutorials/android/ambilwarna-color-picker-dialog

public class meleegeneraleActivity extends AppCompatActivity {
    public DrawingCanvas drawingCanvas;
    public GuessingCanvas guessingCanvas;

    public Button eraseButton;
    public Button drawButton;
    public Button color;
    public Button capStyle;
    public SeekBar seekBar;
    public TextView seekBarText;
    public Button toggle;
    public ImageButton sendAnswer;
    public TextView hints;
    public EditText answer;
    public ImageView chatButton;

    public Boolean guessingView = false;

    public LinearLayout layoutDrawingView;
    public LinearLayout layoutGuessingView;

    public ConstraintLayout mLayout;
    public int mDefaultColor;

    private CountDownTimer timer;
    private long remainingTime = 60000; //1 minute
    private TextView  chrono;

    private int score;

    private int nbPlayers = 2;
    public TextView player1;
    public TextView player2;
    public TextView player3;
    public TextView player4;
    public TextView score1;
    public TextView score2;
    public TextView score3;
    public TextView score4;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        drawingCanvas = new DrawingCanvas(this,null);
        setContentView(R.layout.activity_meleegenerale);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        initializeObject();
        eventListeners();
        startTimer();
        showNbPlayers();

    }

    private void initializeObject(){
        drawingCanvas = (DrawingCanvas) findViewById(R.id.drawing);
        guessingCanvas = (GuessingCanvas) findViewById(R.id.guessing);
        eraseButton = (Button) findViewById(R.id.eraser);
        drawButton = (Button) findViewById(R.id.paint);
        color = (Button) findViewById(R.id.colorButton);
        capStyle = (Button) findViewById(R.id.capButton);
        mLayout = (ConstraintLayout) findViewById(R.id.layout);
        mDefaultColor = ContextCompat.getColor(meleegeneraleActivity.this, R.color.colorPrimary);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBarText = (TextView) findViewById(R.id.sbTextView);
        toggle = (Button) findViewById(R.id.toggleButton);
        layoutDrawingView = (LinearLayout) findViewById(R.id.drawingToolbox);
        layoutGuessingView = (LinearLayout) findViewById(R.id.guessingToolbox);
        drawingCanvas = (DrawingCanvas) findViewById(R.id.drawing);
        sendAnswer = (ImageButton) findViewById(R.id.sendAnswer);
        hints = (TextView) findViewById(R.id.hints);
        answer = (EditText) findViewById(R.id.answer);
        chatButton = (ImageView) findViewById(R.id.chatButton);
        chrono = (TextView) findViewById(R.id.chronometer);

        player1 = (TextView) findViewById(R.id.player1);
        player2 = (TextView) findViewById(R.id.player2);
        player3 = (TextView) findViewById(R.id.player3);
        player4 = (TextView) findViewById(R.id.player4);
        score1 = (TextView) findViewById(R.id.points1);
        score2 = (TextView) findViewById(R.id.points2);
        score3 = (TextView) findViewById(R.id.points3);
        score4 = (TextView) findViewById(R.id.points4);

    }

    private void eventListeners() {
        eraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingCanvas.setErase(true);
                color.setBackground(getDrawable(R.drawable.round_button));
                eraseButton.setBackground(getDrawable(R.drawable.selected_button));
                drawButton.setBackground(getDrawable(R.drawable.round_button));
                capStyle.setBackground(getDrawable(R.drawable.round_button));
                openEraserOptions();

            }
        });
        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingCanvas.setErase(false);
                color.setBackground(getDrawable(R.drawable.round_button));
                eraseButton.setBackground(getDrawable(R.drawable.round_button));
                drawButton.setBackground(getDrawable(R.drawable.selected_button));
                capStyle.setBackground(getDrawable(R.drawable.round_button));
            }
        });

        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingCanvas.setErase(false);
                color.setBackground(getDrawable(R.drawable.selected_button));
                eraseButton.setBackground(getDrawable(R.drawable.round_button));
                drawButton.setBackground(getDrawable(R.drawable.round_button));
                capStyle.setBackground(getDrawable(R.drawable.round_button));
                openColorPicker();

            }
        });

        capStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingCanvas.setErase(false);
                color.setBackground(getDrawable(R.drawable.round_button));
                eraseButton.setBackground(getDrawable(R.drawable.round_button));
                drawButton.setBackground(getDrawable(R.drawable.round_button));
                capStyle.setBackground(getDrawable(R.drawable.selected_button));
                openCapOptions();

            }
        });

        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { setToggle();

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int pval = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pval = progress;
                drawingCanvas.setWidth(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //write custom code to on start progress
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarText.setText(pval + "/" + seekBar.getMax());
            }
        });

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

    public void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;
                drawingCanvas.setDrawingColor(mDefaultColor);

            }
        });
        colorPicker.show();
    }

    //https://www.javatpoint.com/android-popup-menu-example
    public void openCapOptions(){
        PopupMenu popup = new PopupMenu(meleegeneraleActivity.this, capStyle);
        popup.getMenuInflater().inflate(R.menu.cap_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(meleegeneraleActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                drawingCanvas.setPencilTip(item.getTitle().toString());
                return true;
            }
        });
        popup.show();

    }

    public void openEraserOptions(){
        PopupMenu popup = new PopupMenu(meleegeneraleActivity.this, eraseButton);
        popup.getMenuInflater().inflate(R.menu.eraser_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(meleegeneraleActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        popup.show();
    }

    public void setToggle(){
        if(!guessingView){
            guessingView = true;
            layoutGuessingView.setVisibility(View.VISIBLE);
            layoutDrawingView.setVisibility(View.INVISIBLE);
            guessingCanvas.setVisibility(View.VISIBLE);
            drawingCanvas.setVisibility(View.INVISIBLE);
        }
        else{
            guessingView = false;
            layoutGuessingView.setVisibility(View.INVISIBLE);
            layoutDrawingView.setVisibility(View.VISIBLE);
            guessingCanvas.setVisibility(View.INVISIBLE);
            drawingCanvas.setVisibility(View.VISIBLE);
        }
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
        int seconds = (int) remainingTime % 60000 / 1000;
        String timeLeft;
        timeLeft = "" + minutes;
        timeLeft += ":";
        if(seconds < 10){
            timeLeft += "0";
        }
        timeLeft += seconds;

        chrono.setText(timeLeft);
    }

    public void openDialog() {
        postMultiplayerGameDialog postMultiplayerGameDialog = new postMultiplayerGameDialog();
        postMultiplayerGameDialog.show(getSupportFragmentManager(), "postMultiplayerGameDialog");
        postMultiplayerGameDialog.setCancelable(false);
    }

    public void updatePoints(){
        score+=1;
        System.out.println(score);
//        points.setText(score +" pts");
    }

    public void setNbPlayers(){
        //selon nb de personnes connectees sur socket du jeu
        // au moins 2 joueurs reels
    }

    public void showNbPlayers(){
        switch (nbPlayers){
            case 1:
                System.out.println(1);
                player1.setVisibility(View.VISIBLE);
                score1.setVisibility(View.VISIBLE);
                break;
            case 2:
                System.out.println(2);
                player1.setVisibility(View.VISIBLE);
                score1.setVisibility(View.VISIBLE);
                player2.setVisibility(View.VISIBLE);
                score2.setVisibility(View.VISIBLE);
                break;
            case 3:
                System.out.println(3);
                player1.setVisibility(View.VISIBLE);
                score1.setVisibility(View.VISIBLE);
                player2.setVisibility(View.VISIBLE);
                score2.setVisibility(View.VISIBLE);
                player3.setVisibility(View.VISIBLE);
                score3.setVisibility(View.VISIBLE);
                break;
            case 4:
                System.out.println(4);
                player1.setVisibility(View.VISIBLE);
                score1.setVisibility(View.VISIBLE);
                player2.setVisibility(View.VISIBLE);
                score2.setVisibility(View.VISIBLE);
                player3.setVisibility(View.VISIBLE);
                score3.setVisibility(View.VISIBLE);
                player4.setVisibility(View.VISIBLE);
                score4.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onBackPressed() { }
}
