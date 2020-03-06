package com.example.polydraw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PlayMenu extends AppCompatActivity {

    private Button backButton;
    private Button multiplayerButton;
    private Button soloButton;
    private Button freeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        backButton = (Button) findViewById(R.id.backButton);
        multiplayerButton = (Button) findViewById(R.id.multiplayerButton);
        soloButton = (Button) findViewById(R.id.soloButton);
        freeButton = (Button) findViewById(R.id.freeButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });


        multiplayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFreeGame();
            }
        });

        soloButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFreeGame();
            }
        });

        freeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFreeGame();
            }
        });

        }

    public void backToMenu() {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }

    public void goToMultiplayerGameMenu(){
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }

    public void goToSoloGameMenu(){
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }

    public void openFreeGame(){
        Intent intent = new Intent(this, DrawingActivity.class);
        startActivity(intent);
    }
}
