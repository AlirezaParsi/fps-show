package com.example.fpsmonitor;

public class JniTools {
    static {
        System.loadLibrary("tools");
    }
    public static native String getFps();
    public static native int getCpuNum();
}