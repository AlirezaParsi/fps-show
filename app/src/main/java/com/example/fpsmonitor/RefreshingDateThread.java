package com.example.fpsmonitor;

public class RefreshingDateThread extends Thread {
    static int cpunum;  // برای سازگاری
    static String fps;

    static int delay;

    public void run() {
        delay = SharedPreferencesUtil.sharedPreferences.getInt(SharedPreferencesUtil.REFRESHING_DELAY, SharedPreferencesUtil.DEFAULT_DELAY);
        while (!FloatingWindow.doExit) {
            if (FloatingWindow.showFpsNow && Support.support_fps)
                fps = JniTools.getFps();
            FloatingWindow.uiRefresher.sendEmptyMessage(0);
            try {
                Thread.sleep(delay);
            } catch (Exception e) {
            }
        }
    }
}