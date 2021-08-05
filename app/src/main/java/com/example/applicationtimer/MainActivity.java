package com.example.applicationtimer;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements Updating {

    public static final String STATUS_NAME= "status";

    private SecondsLimiter secondsLimiter;
    private TextView timeView;
    private Handler handler;
    private boolean isStatus;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        secondsLimiter = savedInstanceState == null ? new SecondsLimiter() :
                (SecondsLimiter) savedInstanceState.getSerializable(SecondsLimiter.CLASS_NAME);

        secondsLimiter.setMain(this);

        isStatus = savedInstanceState != null && savedInstanceState.getBoolean(STATUS_NAME);
        timeView = findViewById(R.id.text_time);

        handler = new Handler();
        handler.post(secondsLimiter);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SecondsLimiter.CLASS_NAME, secondsLimiter);
        outState.putBoolean(STATUS_NAME, isStatus);
    }

    @Override
    public void update(String time) {

        timeView.setText(time);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (secondsLimiter.isStatus()) {
            isStatus = true;
            secondsLimiter.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isStatus) {
            isStatus = false;
            secondsLimiter.start();
        }
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    public void onClickStart(View view) {

        secondsLimiter.start();
    }

    public void onClickStop(View view) {

        secondsLimiter.stop();
    }


    public void onClickReset(View view) {

        secondsLimiter.reset();
    }
}