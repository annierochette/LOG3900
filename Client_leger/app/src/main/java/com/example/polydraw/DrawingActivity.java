package com.example.polydraw;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

//import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

public class DrawingActivity extends AppCompatActivity {
    private int defaultColorR = 0;
    private int defaultColorG = 0;
    private int defaultColorB = 0;
    public  int selectedColorR;
    public  int selectedColorG;
    public  int selectedColorB;
    DrawingCanvas drawingCanvas;
    Button eraseButton;
    Button drawButton;
    Button color;
    Button okColor;
//    final ColorPicker cp = new ColorPicker(DrawingActivity.this, defaultColorR, defaultColorG, defaultColorB); //https://github.com/Pes8/android-material-color-picker-dialog; https://stackoverflow.com/questions/6980906/android-color-picker

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
        drawButton = (Button) findViewById(R.id.paint);
        color = (Button) findViewById(R.id.colorButton);
//        okColor = (Button)cp.findViewById(R.id.okColorButton);

    }

    private void eventListeners() {
        eraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingCanvas.setErase(true);
                color.setBackground(getDrawable(R.drawable.round_button));
                eraseButton.setBackground(getDrawable(R.drawable.selected_button));
                drawButton.setBackground(getDrawable(R.drawable.round_button));

            }
        });
        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingCanvas.setErase(false);
                color.setBackground(getDrawable(R.drawable.round_button));
                eraseButton.setBackground(getDrawable(R.drawable.round_button));
                drawButton.setBackground(getDrawable(R.drawable.selected_button));
            }
        });

        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color.setBackground(getDrawable(R.drawable.selected_button));
                eraseButton.setBackground(getDrawable(R.drawable.round_button));
                drawButton.setBackground(getDrawable(R.drawable.round_button));

                /*color.setBackgroundColor(getColor(R.drawable.round_green_button));
                eraseButton.setBackgroundColor(getColor(R.color.colorOrange));
                drawButton.setBackgroundColor(getColor(R.color.colorOrange));*/

//                cp.show();
            }
        });

        /*okColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                *//* You can get single channel (value 0-255) *//*
                selectedColorR = cp.getRed();
                selectedColorG = cp.getGreen();
                selectedColorB = cp.getBlue();

                cp.dismiss();
            }
        });*/


    }
}

