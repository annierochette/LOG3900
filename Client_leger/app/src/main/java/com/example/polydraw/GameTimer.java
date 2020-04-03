package com.example.polydraw;

import android.os.CountDownTimer;

import java.util.Timer;

public class GameTimer {
    private CountDownTimer timer;
    private long remainingTime = 60000; //1 minute



    public void start(){
        timer = new CountDownTimer(remainingTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;

            }

            @Override
            public void onFinish() {

            }
        }.start();

    }

    public void updateTimer(){
        int minutes = (int) remainingTime / 60000;
        int seconds = (int) remainingTime %60000/1000;
        String timeLeft;
        timeLeft = "" + minutes;
        timeLeft += ":";
        if(seconds < 10){
            timeLeft += "0";
        }
        timeLeft += seconds;
    }

    public void addTime(){

    }


}
