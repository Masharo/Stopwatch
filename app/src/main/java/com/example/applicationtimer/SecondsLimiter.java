package com.example.applicationtimer;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

//Класс который подсчитывает время

@RequiresApi(api = Build.VERSION_CODES.O)
public class SecondsLimiter implements Runnable, Serializable {

    public static final String CLASS_NAME = "CLASS_SECOND_LIMITER";

    private LocalDateTime localDateTime;
    private long time;
    private long timeStop;
    private boolean status;
    private transient Updating main;

    public SecondsLimiter(LocalDateTime localDateTime, long time, long timeStop, boolean status) {
        this.localDateTime = localDateTime;
        this.time = time;
        this.timeStop = timeStop;
        this.status = status;
    }

    public SecondsLimiter() {

        this.localDateTime = null;
        this.time = 0;
        this.status = false;
        this.timeStop = 0;
    }

    public void setMain(Updating main) {
        this.main = main;
    }

    private String getString() {

        long second = time % 60;
        long minute = time / 60 % 60;
        long hour = time / 3600;

        String result = String.format("%02d:%02d:%02d", hour, minute, second);

        return result;
    }

    public boolean isStatus() {
        return status;
    }

    private void update() {

        if (this.status) {
            this.time = Duration.between(localDateTime, LocalDateTime.now()).getSeconds() + timeStop;
            main.update(getString());
        }
    }

    public void stop() {

        if (this.status) {
            this.timeStop = this.time;
            this.status = false;
        }
    }

    public void start() {

        if (!this.status) {
            this.localDateTime = LocalDateTime.now();
            this.status = true;
        }
    }

    public void reset() {

        this.time = 0;
        this.timeStop = 0;
        this.localDateTime = LocalDateTime.now();
        main.update(getString());
    }

    @Override
    public void run() {

        update();
        main.getHandler().postDelayed(this, 100);
    }
}