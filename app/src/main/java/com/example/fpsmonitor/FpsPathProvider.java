package com.example.fpsmonitor;

import android.util.Log;
import java.io.File;

public class FpsPathProvider {
    private static final String TAG = "FpsPathProvider";

    // لیست تمام مسیرهای ممکن برای FPS در دستگاه‌های مختلف
    public static final String[] KNOWN_FPS_PATHS = {
        // مدیاتک (Mediatek)
        "/sys/kernel/fpsgo/fstb/fstb_fps_list",
        "/sys/kernel/fpsgo/fstb/fpsgo_status",
        "/proc/mtk_fps",
        
        // کوالکام (Snapdragon)
        "/sys/class/kgsl/kgsl-3d0/gpuclk",
        "/sys/devices/soc.0/1c00000.qcom,kgsl-3d0/kgsl/kgsl-3d0/gpuclk",
        "/d/dri/0/gt/cur_freq",
        
        // سامسونگ اگزینوس
        "/sys/devices/platform/17500000.mali/clock",
        "/sys/devices/13900000.mali/clock",
        
        // هواوی کیرین
        "/sys/devices/platform/e82c0000.mali/clock",
        
        // مسیرهای عمومی
        "/sys/class/graphics/fb0/virtual_size",
        "/sys/class/graphics/fb0/mode"
    };

    // پیدا کردن مسیر صحیح FPS
    public static String detectFpsPath() {
        for (String path : KNOWN_FPS_PATHS) {
            File file = new File(path);
            if (file.exists() && file.canRead()) {
                Log.d(TAG, "مسیر FPS پیدا شد: " + path);
                return path;
            }
        }
        Log.w(TAG, "هیچ مسیر FPS شناخته شده‌ای پیدا نشد!");
        return null;
    }

    // پیدا کردن مسیر فرکانس CPU
    public static String detectCpuFreqPath(int cpuCore) {
        String[] possiblePaths = {
            "/sys/devices/system/cpu/cpu" + cpuCore + "/cpufreq/scaling_cur_freq",
            "/sys/devices/system/cpu/cpu" + cpuCore + "/cpufreq/cpuinfo_cur_freq"
        };
        
        for (String path : possiblePaths) {
            if (new File(path).exists()) {
                return path;
            }
        }
        return null;
    }

    // پیدا کردن مسیر دمای دستگاه
    public static String detectTemperaturePath() {
        String[] tempPaths = {
            "/sys/class/thermal/thermal_zone0/temp",
            "/sys/class/thermal/thermal_zone1/temp",
            "/sys/devices/virtual/thermal/thermal_zone0/temp"
        };
        
        for (String path : tempPaths) {
            if (new File(path).exists()) {
                return path;
            }
        }
        return null;
    }
}
