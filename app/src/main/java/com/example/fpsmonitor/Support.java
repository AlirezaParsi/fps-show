package com.example.fpsmonitor;

class Support {
    static boolean support_fps;

    static final int UNSUPPORTED = -1;

    static int CheckSupport() {
        int linen = 0;

        if (!JniTools.getFps().equals("")) {
            linen++;
            support_fps = true;
        } else support_fps = false;

        return linen;
    }
}