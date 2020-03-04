package com.example.polydraw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PlayMenu extends FragmentActivity {
    private Button drawingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        drawingButton = (Button) findViewById(R.id.drawing);

        drawingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawingView();
            }
        });

        if (findViewById(R.id.chat) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

        }

    }

    public void openDrawingView(){
        Intent intent = new Intent(this, DrawingActivity.class);
        startActivity(intent);
    }
}
