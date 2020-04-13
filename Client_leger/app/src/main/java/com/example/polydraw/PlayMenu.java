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

public class PlayMenu extends AppCompatActivity {

    private Button backButton;
    private Button multiplayerButton;
    private Button soloButton;
    private Button freeButton;
    private ImageButton disconnectButton;
    private ImageView chatButton;
    private SocketIO socket;

    private String player;
    private String token;
    private String username;
    private String firstName;
    private String lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        backButton = (Button) findViewById(R.id.backButton);
        multiplayerButton = (Button) findViewById(R.id.multiplayerButton);
        soloButton = (Button) findViewById(R.id.soloButton);
        freeButton = (Button) findViewById(R.id.freeButton);
        disconnectButton = (ImageButton) findViewById(R.id.logoutButton);
        chatButton = (ImageView) findViewById(R.id.chatButton);

        Intent intent = getIntent();
        player = intent.getStringExtra("player");
        token = intent.getStringExtra("token");
        username = intent.getStringExtra("username");
        firstName = intent.getStringExtra("firstName");
        lastName = intent.getStringExtra("lastName");

        System.out.println("PLAY MENU TOKEN :"+ token);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        multiplayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMultiplayerGameMenu();
            }
        });

        soloButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSoloGame();
            }
        });

        freeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFreeGame();
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



    public void backToMenu() {
        Intent intent = new Intent(this, Menu.class);
        intent.putExtra("token", token);
        intent.putExtra("username", username);
        intent.putExtra("firstName", firstName);
        intent.putExtra("lastName", lastName);
        startActivity(intent);
    }

    public void goToMultiplayerGameMenu(){
        Intent intent = new Intent(this, MeleeGeneraleMenuActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("username", username);
        intent.putExtra("firstName", firstName);
        intent.putExtra("lastName", lastName);
        startActivity(intent);
    }

    public void playSoloGame(){
        FunctionalityNotAvailable functionalityNotAvailable = new FunctionalityNotAvailable();
        functionalityNotAvailable.show(getSupportFragmentManager(), "functionalityNotAvailable");
        /*Intent intent = new Intent(this, ModeSoloActivity.class);
        startActivity(intent);*/
    }

    public void openFreeGame(){
        Intent intent = new Intent(this, DrawingActivity.class);
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
}
