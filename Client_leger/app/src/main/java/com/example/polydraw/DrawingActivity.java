package com.example.polydraw;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DrawingActivity extends AppCompatActivity {
    DrawingCanvas drawingCanvas;
    Button eraseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        drawingCanvas = new DrawingCanvas(this,null);
        setContentView(R.layout.activity_canvas);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        initializeObject();
        eventListeners();
    }

    private void initializeObject(){
        drawingCanvas = (DrawingCanvas) findViewById(R.id.drawing);
        eraseButton = (Button) findViewById(R.id.eraser);
    }

    private void eventListeners() {
        eraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingCanvas.setErase(true);
                eraseButton.setBackgroundColor(Color.BLACK);

            }
        });
    }
}

