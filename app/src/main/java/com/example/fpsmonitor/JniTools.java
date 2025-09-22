package com.example.fpsmonitor;

public class JniTools {
    static {
        try {
            System.loadLibrary("tools");
        } catch (UnsatisfiedLinkError e) {
            // کتابخانه وجود نداره، ولی برنامه ادامه پیدا کنه
        }
    }
    
    public static String getFps() {
        try {
            return getFpsNative();
        } catch (UnsatisfiedLinkError e) {
            return "N/A (No Native)";
        }
    }
    
    public static int getCpuNum() {
        try {
            return getCpuNumNative();
        } catch (UnsatisfiedLinkError e) {
            return Runtime.getRuntime().availableProcessors();
        }
    }
    
    private static native String getFpsNative();
    private static native int getCpuNumNative();
}
