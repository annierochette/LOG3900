package com.example.polydraw;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity {
    private Button playButton;
    private Button profileButton;
    private Button tutorialButton;
    private Button settingsButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);

        playButton = (Button)findViewById(R.id.play);
        profileButton = (Button) findViewById(R.id.profile);
        tutorialButton = (Button)findViewById(R.id.tutorial);
        settingsButton = (Button) findViewById(R.id.settings);
    }

}
