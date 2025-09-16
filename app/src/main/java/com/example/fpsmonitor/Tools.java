package com.example.fpsmonitor;

import android.content.Context;

class Tools {
    static String bool2Text(boolean bool, Context context) {
        if (bool) {
            return context.getResources().getString(R.string.yes);
        }
        return context.getResources().getString(R.string.no);
    }
}