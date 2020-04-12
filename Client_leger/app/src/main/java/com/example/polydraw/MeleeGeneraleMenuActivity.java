package com.example.polydraw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import com.example.polydraw.Socket.SocketIO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MeleeGeneraleMenuActivity extends AppCompatActivity {

    private Button backButton;
    private Button createButton;
    private ImageButton disconnectButton;
    private ImageView chatButton;
    private SocketIO socket;

    matchListAdapter adapter;
    ArrayList<String> matchList;
    public RecyclerView myRecyclerView;

    private String player;
    private String token;
    private String username;
    private String firstName;
    private String lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_melee_generale_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        HttpGetMatches task = new HttpGetMatches();
        task.execute(SocketIO.HTTP_URL+"match/");

        Intent intent = getIntent();
        player = intent.getStringExtra("player");
        token = intent.getStringExtra("token");
        username = intent.getStringExtra("username");
        firstName = intent.getStringExtra("firstName");
        lastName = intent.getStringExtra("lastName");

        System.out.println(token);

        backButton = (Button) findViewById(R.id.backButton);
        createButton = (Button) findViewById(R.id.createButton);
        disconnectButton = (ImageButton) findViewById(R.id.logoutButton);
        chatButton = (ImageView) findViewById(R.id.chatButton);

        matchList = new ArrayList<>();
        myRecyclerView = (RecyclerView) findViewById(R.id.matchlist);
        /*adapter = new matchListAdapter(new ArrayList<String>());
        myRecyclerView.setAdapter(adapter);*/
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myRecyclerView.setLayoutManager(mLayoutManager);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToPlayMenu();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMultiplayerGame();
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



    public void backToPlayMenu() {
        Intent intent = new Intent(this, PlayMenu.class);
        intent.putExtra("token", token);
        intent.putExtra("username", username);
        intent.putExtra("firstName", firstName);
        intent.putExtra("lastName", lastName);
        startActivity(intent);
    }

    public void playMultiplayerGame(){
        Intent intent = new Intent(this, WaitingRoom.class);
        intent.putExtra("token", token);
        intent.putExtra("username", username);
        intent.putExtra("firstName", firstName);
        intent.putExtra("lastName", lastName);
        startActivity(intent);
    }

    public void backToLogin(){
        socket.emitDisconnectionStatus("disconnection");
        Intent intent = new Intent(this, MainActivity.class);
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

    @Override
    public void onBackPressed() { }

    // source ajouter item dans listview avec asynctask: https://www.youtube.com/watch?time_continue=602&v=2bNBLiqkKlE&feature=emb_title
    public class HttpGetMatches extends AsyncTask<String, String, String> {
        String result;
        public HttpGetMatches() {

        }

        @Override
        protected void onPreExecute() {
            adapter = new matchListAdapter(new ArrayList<String>());
        }

        @Override
        protected void onProgressUpdate(String... values) {
            matchList.add(values[0]);
            System.out.println(values[0]);
            myRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        @SuppressLint("WrongThread")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                JSONArray jsonArray = new JSONArray(s);
                for(int i = 0; i<jsonArray.length(); i++){
                    try {
                        JSONObject oneObject = jsonArray.getJSONObject(i);
                        // Pulling items from the array
                        String matchName = oneObject.getString("name");
                        publishProgress(matchName);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch(Exception e){

            }

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);

                // Create the urlConnection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(5000);
                urlConnection.setRequestProperty("Authorization", token);
                urlConnection.setRequestMethod("GET");


                int statusCode = urlConnection.getResponseCode();
                System.out.println(statusCode);

                if (statusCode < 299) { // success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    result = response.toString();
                } else {
                    result = null;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

    }

}
