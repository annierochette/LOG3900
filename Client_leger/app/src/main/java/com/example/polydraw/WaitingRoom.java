package com.example.polydraw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.ArrayLinkedVariables;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.polydraw.Socket.SocketIO;
import com.github.nkzawa.emitter.Emitter;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private String _id;

    ListView playersWaiting;
    ArrayAdapter<String> adapter;
    JSONArray playersList;

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
        _id = intent.getStringExtra("_id");

/*        if(token == null)
            socket.emitDisconnectionStatus("disconnection");*/
        System.out.println("WAITING ROOM TOKEN :"+ token);

        if(intent.getStringExtra("matchId") != null)
            channelName = intent.getStringExtra("matchId");
        System.out.println("je suis connecté à: "+channelName);

        //socket.getSocket().emit("joinChannel", channelName, username);
        /*System.out.print("PRINT USERNAME POUR EMIT JOIN WAITINGROOM: "+ username);
        socket.getSocket().emit("joinGame", channelName, username);*/

        socket.getSocket().on("joinGame", onJoinMatch);
        /*socket.getSocket().on("startMatch", startMatch);*/
        System.out.println();


        playButton = (Button) findViewById(R.id.playButton);
        chatButton = (ImageView) findViewById(R.id.chatButton);

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
        socket.getSocket().emit("startMatch", channelName, username);
        intent.putExtra("token", token);
        intent.putExtra("username", username);
        intent.putExtra("firstName", firstName);
        intent.putExtra("lastName", lastName);
        intent.putExtra("matchId", channelName);
        intent.putExtra("_id", _id);
        startActivity(intent);
    }

    public void openChat(){
        Intent intent = new Intent(this, ChatBoxActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("username", username);
        intent.putExtra("firstName", firstName);
        intent.putExtra("lastName", lastName);
        intent.putExtra("matchId", channelName);
        intent.putExtra("_id", _id);
        startActivity(intent);
    }
    //A DECOMMENTER QUAND PARTIE SERA FONCTIONNELLE
    /*@Override
    public void onBackPressed() { }*/

    private Emitter.Listener onJoinMatch = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
//            playersList = (String) args[0];
            String data = args[0].toString();
            System.out.println(data);
            System.out.println("SOCKET JOIN ON");
            try{
                playersList = new JSONArray(data);
                nbPlayers = playersList.length();
               System.out.println("Nb de joueurs presents: "+nbPlayers);

                if(nbPlayers>1)
                    playButton.setVisibility(View.VISIBLE);

                /*playersWaiting = (ListView) findViewById(R.id.playersWaiting);
                adapter = new ArrayAdapter<String>(WaitingRoom.this, android.R.layout.simple_list_item_1, new ArrayList<String>());
                playersWaiting.setAdapter(adapter);*/


            } catch(Exception e){

            }
            List<String> myList = new ArrayList<String>(Arrays.asList(data.split(",")));
            System.out.println("SIZE DE LA LISTE "+myList.size());

            playButton.setVisibility(View.VISIBLE);

            playersWaiting = (ListView) findViewById(R.id.playersWaiting);
            adapter = new ArrayAdapter<String>(WaitingRoom.this, android.R.layout.simple_list_item_1, myList);
            playersWaiting.setAdapter(adapter);

        }
    };

}
