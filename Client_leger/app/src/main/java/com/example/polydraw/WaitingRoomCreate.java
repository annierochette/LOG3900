package com.example.polydraw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.polydraw.Socket.SocketIO;
import com.github.nkzawa.emitter.Emitter;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class WaitingRoomCreate extends AppCompatActivity {

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
    private String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room_create);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        Map<String, String> postData = new HashMap<>();
        postData.put("type", "FreeForAll");
        HttpPostNewGame task = new HttpPostNewGame(postData);
        System.out.println(SocketIO.HTTP_URL+"match/");
        task.execute(SocketIO.HTTP_URL+"match/");

        Intent intent = getIntent();
        player = intent.getStringExtra("player");
        token = intent.getStringExtra("token");
        username = intent.getStringExtra("username");
        firstName = intent.getStringExtra("firstName");
        lastName = intent.getStringExtra("lastName");
        _id = intent.getStringExtra("_id");

/*        if(token == null)
            socket.emitDisconnectionStatus("disconnection");*/
        System.out.println("WAITING ROOM CREATE TOKEN :"+ token);

        if(intent.getStringExtra("matchId") != null)
            channelName = intent.getStringExtra("matchId");
        System.out.println("je suis connecté à: "+channelName);

        /*socket.getSocket().emit("joinChannel", channelName, username);
        socket.getSocket().emit("joinGame", channelName, username);

        socket.getSocket().on("joinGame", onJoinMatch);*/
        /*socket.getSocket().on("startMatch", startMatch);*/
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
//            System.out.println((String) args[0]);
            String data = args[0].toString();
            Gson gson = new Gson();
            System.out.println(data);
        }
    };

    public class HttpPostNewGame extends AsyncTask<String, Void, String> {
        JSONObject postData;
        public String status;
        public String token;
        public JSONObject player;
        public HttpPostNewGame(Map<String, String> postData) {
            if (postData != null) {
                this.postData = new JSONObject(postData);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println(result);

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);


                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("POST");

                if (this.postData != null) {
                    OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                    writer.write(postData.toString());
                    writer.flush();
                }

                final int statusCode = urlConnection.getResponseCode();

                if(statusCode == 200||statusCode == 201){
                    System.out.println(statusCode + ": Successful request");
                    if (statusCode < 299) { // success
                        BufferedReader in = new BufferedReader(new InputStreamReader(
                                urlConnection.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        String data = response.toString();
                        JSONObject reader = new JSONObject(data);
                        //player  = reader.getJSONObject("name");
                        JSONObject name = reader.getJSONObject("name");
                        System.out.print(name.toString());
                        status = data;

                    }
                }

                else{
                    System.out.println(statusCode + ": Something went wrong...");
                    status = null;
                }


            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return status;
        }

    }

}
