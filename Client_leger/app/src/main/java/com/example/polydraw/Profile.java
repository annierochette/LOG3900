package com.example.polydraw;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.polydraw.Socket.SocketIO;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Array;
import java.util.List;

public class Profile extends AppCompatActivity {

    private Button backButton;
    private ImageButton disconnectButton;
    private ImageView chatButton;
    private Button pictureButton;
    private SocketIO socket;
    private TextView namePlaceholder;
    private TextView usernamePlaceholder;
    private TextView nbPlayedGames;
    private TextView nbGamesWon;
    private TextView gameTime;
    private TextView time;

    private String token;
    private String username;
    private String firstName;
    private String lastName;
    private String _id;

    public JSONObject generalStats;
    String matchWon;
    String matchPlayed;
    String[] statsView;

    private List<JSONObject> games;

    String USERNAME = MainActivity.editTextString;

    public static final int GET_FROM_GALLERY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        username = intent.getStringExtra("username");
        firstName = intent.getStringExtra("firstName");
        lastName = intent.getStringExtra("lastName");
        _id = intent.getStringExtra("_id");

        final String firstNameFinal = intent.getStringExtra("firstName");
        final String lastNameFinal = intent.getStringExtra("lastName");
        final String usernameFinal = intent.getStringExtra("username");

        HttpGetPlayer task = new HttpGetPlayer();
        task.execute(SocketIO.HTTP_URL+"players/"+username+"/general-statistics");

        backButton = (Button) findViewById(R.id.backButton);
        disconnectButton = (ImageButton) findViewById(R.id.logoutButton);
        chatButton = (ImageView) findViewById(R.id.chatButton);
        pictureButton = (Button) findViewById(R.id.changePicture);

        namePlaceholder = (TextView) findViewById(R.id.name);
        namePlaceholder.setText(firstNameFinal+ " "+lastNameFinal);

        usernamePlaceholder = (TextView) findViewById(R.id.username);
        usernamePlaceholder.setText(usernameFinal);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
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

        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });
    }

    public void backToMenu(){
        Intent intent = new Intent(this, Menu.class);
        intent.putExtra("token", token);
        intent.putExtra("username", username);
        intent.putExtra("firstName", firstName);
        intent.putExtra("lastName", lastName);
        intent.putExtra("_id", _id);
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
        intent.putExtra("_id", _id);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() { }

    public class HttpGetPlayer extends AsyncTask<String, Void, String> {
        String result;

        public HttpGetPlayer() {

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println(s);
            statsView = s.split(",");
            Integer var1 = Integer.parseInt(statsView[0]);
            Integer var2 = Integer.parseInt(statsView[1]);
            System.out.println(var1 +" "+ var2);
            setStats(var1, var2);

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);

                // Create the urlConnection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                System.out.println(token);
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

                    // print result
                    String data = response.toString();
                    System.out.println(data);
                    JSONObject reader = new JSONObject(data);
                    generalStats = reader.getJSONObject("generalStats");
                    matchWon = generalStats.get("matchWon").toString();
                    matchPlayed = generalStats.get("matchPlayed").toString();
                    result = generalStats.get("matchWon").toString() + ","+generalStats.get("matchPlayed").toString();

                } else{
                    result = null;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

    }

    public void setStats(Integer matchsGagnes, Integer matchsJoues){
        nbGamesWon = (TextView) findViewById(R.id.nbGamesWon);
        if(matchPlayed!="0"){
            Integer result = ((matchsGagnes*100)/matchsJoues);
            nbGamesWon.setText("Pourcentage de parties gagnées: "+result+"%");
        } else{
            nbGamesWon.setText("Pourcentage de parties gagnées: 0%");

        }

        time = (TextView) findViewById(R.id.time);
        if(matchPlayed!="0"){
            time.setText("Temps total à jouer: "+matchPlayed+" minutes");
        } else{
            time.setText("Temps total à jouer: 0 minute");

        }

        nbPlayedGames = (TextView) findViewById(R.id.nbPlayedGames);
        if(matchPlayed!="0"){
            nbPlayedGames.setText("Nombre de parties jouées: "+ matchPlayed);
        } else{
            nbPlayedGames.setText("Nombre de parties jouées: 0");

        }

        gameTime = (TextView) findViewById(R.id.gameTime);
        if(matchPlayed!="0"){
            gameTime.setText("Temps moyen d'une partie: 1 minute");
        } else{
            gameTime.setText("Temps moyen d'une partie: 0 minute");

        }

    }
}