package com.code.game;

import android.app.Activity;
import android.widget.TextView;

public class Timer {
    private TextView textViewTime;
    private Activity activity;
    private Thread thread;
    private int secand = 0;
    private boolean flagGo = false;
    private String tourn = "";

    public Timer() {
    }

    public Timer(TextView textViewTime, Activity activity) {
        this.textViewTime = textViewTime;
        this.activity = activity;

        thread = new Thread() {
            public void run() {
                while (flagGo) {
                    try {
                        activity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                secand++;
                                time(secand);
                            }
                        });
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

    private void time(int secand) {
        int h = (int) secand / 3600;
        int mi = (int) ((secand % 3600) / 60);
        int s = (int) ((secand % 3600) % 60);
        if (h > 0)
            textViewTime.setText( tourn+" זמן: "   + String.format("%02d:%02d:%02d", (h), (mi), (s)));
        else
            textViewTime.setText(tourn+" זמן: " + String.format("%02d:%02d", (mi), (s)));


    }

    public void start(String tourn) {
        this.tourn = tourn;
        secand = 0;
        flagGo = true;

    }

    public void stop() {
        flagGo = false;
    }

    public TextView getTextViewTime() {
        return textViewTime;
    }

    public void setTextViewTime(TextView textViewTime) {
        this.textViewTime = textViewTime;
    }
}
