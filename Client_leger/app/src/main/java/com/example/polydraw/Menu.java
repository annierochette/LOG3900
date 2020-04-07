package com.example.polydraw;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.polydraw.Socket.SocketIO;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class Menu extends AppCompatActivity {
    private Button playButton;
    private ImageButton profileButton;
    private Button tutorialButton;
    private Button settingsButton;
    private ImageButton disconnectButton;
    private ImageView chatButton;
//    public String username = getIntent().getStringExtra("username");

    private SocketIO socket;
    private String http = "http://192.168.2.109:5050";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        playButton = (Button) findViewById(R.id.play);
        profileButton = (ImageButton) findViewById(R.id.profile);
        tutorialButton = (Button)findViewById(R.id.tutorial);
        settingsButton = (Button) findViewById(R.id.settings);
        disconnectButton = (ImageButton) findViewById(R.id.logoutButton);
        chatButton = (ImageView) findViewById(R.id.chatButton);

        socket = new SocketIO();
        socket.init();

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlayMenuView();
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfileView();
            }
        });
        tutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTutorialView();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsView();
            }
        });

        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToLogin();
            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChat();
            }
        });
    }

    public void openPlayMenuView(){
        Intent intent = new Intent(this, PlayMenu.class);
        startActivity(intent);
    }

    public void openProfileView(){
        Intent intent = new Intent(this, Profile.class);
//        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void openTutorialView(){
        Intent intent = new Intent(this, Tutorial.class);
        startActivity(intent);
    }

    public void openSettingsView(){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void backToLogin(){
        socket.disconnect();
        socket.emit("disconnection", "General", null);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openChat(){
        Intent intent = new Intent(this, ChatBoxActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        backToLogin();
    }

}
