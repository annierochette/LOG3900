package com.example.polydraw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.polydraw.Socket.SocketIO;

public class WaitingRoom extends AppCompatActivity {

    private Button backButton;
    private Button playButton;
    private ImageButton disconnectButton;
    private ImageView chatButton;
    private SocketIO socket;
    private int nbPlayers = 2; //nb d'utilisateurs qui ont joint la partie

    private String channelName = "test12345"; //mettre nom de la partie

    private String player;
    private String token;
    private String username;
    private String firstName;
    private String lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        Intent intent = getIntent();
        player = intent.getStringExtra("player");
        token = intent.getStringExtra("token");
        username = intent.getStringExtra("username");
        firstName = intent.getStringExtra("firstName");
        lastName = intent.getStringExtra("lastName");

        socket.getSocket().emit("joinChannel", channelName);


        playButton = (Button) findViewById(R.id.playButton);
        chatButton = (ImageView) findViewById(R.id.chatButton);

        if(nbPlayers > 1)
            playButton.setVisibility(View.VISIBLE);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMultiplayerGame();
            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChat();
            }
        });

    }

    public void playMultiplayerGame(){
        Intent intent = new Intent(this, meleegeneraleActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("username", username);
        intent.putExtra("firstName", firstName);
        intent.putExtra("lastName", lastName);
        startActivity(intent);
    }

    public void openChat(){
        Intent intent = new Intent(this, ChatBoxActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("username", username);
        intent.putExtra("firstName", firstName);
        intent.putExtra("lastName", lastName);
        startActivity(intent);
    }
    //A DECOMMENTER QUAND PARTIE SERA FONCTIONNELLE
    /*@Override
    public void onBackPressed() { }*/
}
