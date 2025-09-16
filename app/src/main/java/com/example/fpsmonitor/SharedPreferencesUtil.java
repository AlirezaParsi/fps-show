package com.example.fpsmonitor;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    static SharedPreferences sharedPreferences;

    static final String SKIP_FIRST_SCREEN = "skip_first_screen";
    static final boolean DEFAULT_SKIP_FIRST_SCREEN = false;

    static final String REFRESHING_DELAY = "refreshing_delay";
    static final int DEFAULT_DELAY = 1000;

    static final String SIZE_MULTIPLE = "size_multiple";
    static final float SIZE_MULTIPLE_DEFAULT = (float) 0.8;

    static final String SHOW_FPS = "show_fps";
    static final boolean SHOW_FPS_DEFAULT = true;

    static void init(Context context) {
        sharedPreferences = context.getSharedPreferences("main", 0);
    }
}