package com.example.polydraw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.polydraw.Socket.SocketIO;
import com.github.nkzawa.emitter.Emitter;

public class WaitingRoom extends AppCompatActivity {

    private Button backButton;
    private Button playButton;
    private ImageButton disconnectButton;
    private ImageView chatButton;
    private SocketIO socket;
    private int nbPlayers = 2; //nb d'utilisateurs qui ont joint la partie

    private String channelName = "General";

    private String player;
    private String token;
    private String username;
    private String firstName;
    private String lastName;
    private String playersList;

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

        if(token == null)
            socket.emitDisconnectionStatus("disconnection");
        System.out.println("WAITING ROOM TOKEN :"+ token);

        if(intent.getStringExtra("matchId") != null)
            channelName = intent.getStringExtra("matchId");
        System.out.println("je suis connecté à: "+channelName);

        /*socket.getSocket().emit("joinChannel", channelName, username);
        socket.getSocket().emit("joinGame", channelName, username);*/

//        socket.getSocket().on("joinGame", onJoinMatch);
        System.out.println();


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
        intent.putExtra("matchId", channelName);
        startActivity(intent);
    }

    public void openChat(){
        Intent intent = new Intent(this, ChatBoxActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("username", username);
        intent.putExtra("firstName", firstName);
        intent.putExtra("lastName", lastName);
        intent.putExtra("matchId", channelName);
        startActivity(intent);
    }
    //A DECOMMENTER QUAND PARTIE SERA FONCTIONNELLE
    /*@Override
    public void onBackPressed() { }*/

    private Emitter.Listener onJoinMatch = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            playersList = (String) args[0];
        }
    };
}
