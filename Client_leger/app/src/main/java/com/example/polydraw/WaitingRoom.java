package com.example.polydraw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.ArrayLinkedVariables;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WaitingRoom extends AppCompatActivity {

    private Button backButton;
    private Button playButton;
    private ImageButton disconnectButton;
    private ImageView chatButton;
    private SocketIO socket;
    private int nbPlayers; //nb d'utilisateurs qui ont joint la partie

    private String channelName = "General";

    private String player;
    private String token;
    private String username;
    private String firstName;
    private String lastName;
    private String _id;

    JSONArray playersList;

    WaitingListAdapter adapter;
    List<String> playersWaiting;
    public RecyclerView myRecyclerView;

    List<String>myList;

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
        socket.getSocket().on("startMatch", onStartMatch);

        playButton = (Button) findViewById(R.id.playButton);
        chatButton = (ImageView) findViewById(R.id.chatButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.getSocket().emit("startMatch", channelName, username);
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

    @Override
    public void onBackPressed() {
        socket.getSocket().emit("stopWaiting", channelName, username);
        Intent intent = new Intent(this, meleegeneraleActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("username", username);
        intent.putExtra("firstName", firstName);
        intent.putExtra("lastName", lastName);
        intent.putExtra("matchId", channelName);
        intent.putExtra("_id", _id);
        startActivity(intent);
    }

    /*public void setAdapter(List<String> array){
        myList = new ArrayList<String>(array);
        System.out.println("SIZE DE LA LISTE "+myList.size());

        playButton.setVisibility(View.VISIBLE);

        playersWaiting = (ListView) findViewById(R.id.playersWaiting);
        adapter = new ArrayAdapter<String>(WaitingRoom.this, android.R.layout.simple_list_item_1, myList);
        playersWaiting.setAdapter(adapter);
    }*/

    private Emitter.Listener onStartMatch = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
//            playersList = (String) args[0];
            String data = args[0].toString();
            System.out.println(data);
            System.out.println("START MATCH");

            /*Intent intent = new Intent(WaitingRoom.this, meleegeneraleActivity.class);
            intent.putExtra("token", token);
            intent.putExtra("username", username);
            intent.putExtra("firstName", firstName);
            intent.putExtra("lastName", lastName);
            intent.putExtra("matchId", channelName);
            intent.putExtra("_id", _id);
            startActivity(intent);*/
        }
    };

    private Emitter.Listener onJoinMatch = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
//            playersList = (String) args[0];
            String data = args[0].toString();
            System.out.println(data);
            System.out.println("SOCKET JOIN ON");
            try{
                playersList = new JSONArray(data);
                setNbPlayers(playersList.length());
                System.out.println("Nb de joueurs presents: "+nbPlayers);

                myList = (Arrays.asList(data.split(",")));

                adapter = new WaitingListAdapter(playersWaiting);
                myRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                /*updateList updateList = new updateList();
                updateList.execute(myList);*/

                /*playersWaiting = (ListView) findViewById(R.id.playersWaiting);
                adapter = new ArrayAdapter<String>(WaitingRoom.this, android.R.layout.simple_list_item_1, new ArrayList<String>());
                playersWaiting.setAdapter(adapter);*/


            } catch(Exception e){

            }

        }
    };

    public void setNbPlayers(int num){
        this.nbPlayers = num;
    }

/*    public class updateList extends AsyncTask<List<String>, String, List<String>> {
        public updateList() {

        }

        @Override
        protected void onPreExecute() {
            adapter = new WaitingListAdapter(new ArrayList<String>());
        }

        @Override
        protected void onProgressUpdate(String... values) {
            playersWaiting.add(values[0]);
            adapter = new WaitingListAdapter(playersWaiting);
            myRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        @SuppressLint("WrongThread")
        @Override
        protected void onPostExecute(List<String> s) {
            super.onPostExecute(s);
            try{
                JSONArray jsonArray = new JSONArray(s);
                for(int i = 0; i<jsonArray.length(); i++){
                    try {
                        JSONObject oneObject = jsonArray.getJSONObject(i);
                        // Pulling items from the array
                        String name = oneObject.toString();
                        publishProgress(name);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch(Exception e){

            }

        }

        @Override
        protected List<String> doInBackground(List<String>... params) {

            socket.getSocket().on("joinGame", onJoinMatch);



            return myList;
        }

    }*/

}
