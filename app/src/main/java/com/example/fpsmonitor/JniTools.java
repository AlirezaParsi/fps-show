package com.example.fpsmonitor;

public class JniTools {
    static {
        System.loadLibrary("tools");
    }
    
    public static native String getFps();
    public static native int getCpuNum();
    
    // متدهای جدید برای مانیتورینگ پیشرفته
    public static native String getGpuUsage();
    public static native String getCpuTemperature();
    public static native String getDeviceModel();
}
