package com.example.polydraw;

public class pointsCounter {

    int points;

    public pointsCounter(){
        this.resetCount();

    }


    public int increment() {
        return this.points++;
    }

    public int getScore() {
        return this.points;
    }

    public void resetCount(){
        this.points = 0;
    }

}
