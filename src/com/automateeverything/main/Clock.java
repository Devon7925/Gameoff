package com.automateeverything.main;

/**
 * Clock
 */
public class Clock {
    // Remember the last system time.
    long lastTime = System.nanoTime();
    float dt;

    public void update(){
        long thisTime = System.nanoTime();
        dt = (thisTime - lastTime) / 1E9f;
        lastTime = thisTime;
    }

    float getDelta(){
        return dt;
    }
}