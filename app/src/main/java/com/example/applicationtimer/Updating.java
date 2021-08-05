package com.example.applicationtimer;

import android.os.Handler;

public interface Updating {

    void update(String time);
    Handler getHandler();
}
