package com.example.polydraw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Tutorial extends AppCompatActivity {

    Button button;
    TextView textView;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        final ViewPager viewPager = findViewById(R.id.viewPager);
        final Slideshow slideshow = new Slideshow(this);
        viewPager.setAdapter(slideshow);

        button = findViewById(R.id.enterapp);
        textView = findViewById(R.id.textView3);
        imageView = findViewById(R.id.logo);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == slideshow.getCount()-1){
                    button.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                } else{
                    button.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void backToMenu() {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() { }
}
