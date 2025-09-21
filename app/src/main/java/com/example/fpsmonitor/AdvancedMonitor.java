package com.example.fpsmonitor;

import android.util.Log;
import java.io.*;

public class AdvancedMonitor {
    private static final String TAG = "AdvancedMonitor";
    
    // گرفتن اطلاعات حافظه RAM
    public static String getRamUsage() {
        try {
            Process process = Runtime.getRuntime().exec("free");
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Mem:")) {
                    String[] parts = line.split("\\s+");
                    if (parts.length >= 4) {
                        long total = Long.parseLong(parts[1]);
                        long used = Long.parseLong(parts[2]);
                        return String.format("%.1fG/%.1fG", 
                            used / 1024.0 / 1024.0, 
                            total / 1024.0 / 1024.0);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "خطا در خواندن RAM: " + e.getMessage());
        }
        return "N/A";
    }
    
    // گرفتن دمای دستگاه
    public static String getTemperature() {
        String tempPath = FpsPathProvider.detectTemperaturePath();
        if (tempPath != null) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(tempPath));
                String temp = reader.readLine();
                reader.close();
                if (temp != null) {
                    float tempC = Float.parseFloat(temp) / 1000.0f;
                    return String.format("%.1f°C", tempC);
                }
            } catch (Exception e) {
                Log.e(TAG, "خطا در خواندن دما: " + e.getMessage());
            }
        }
        return "N/A";
    }
    
    // گرفتن فرکانس CPU
    public static String getCpuFrequency(int core) {
        String freqPath = FpsPathProvider.detectCpuFreqPath(core);
        if (freqPath != null) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(freqPath));
                String freq = reader.readLine();
                reader.close();
                if (freq != null) {
                    int freqMhz = Integer.parseInt(freq) / 1000;
                    return freqMhz + "MHz";
                }
            } catch (Exception e) {
                Log.e(TAG, "خطا در خواندن فرکانس CPU: " + e.getMessage());
            }
        }
        return "N/A";
    }
    
    // گرفتن اطلاعات باتری
    public static String getBatteryInfo() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/sys/class/power_supply/battery/capacity"));
            String capacity = reader.readLine();
            reader.close();
            return capacity + "%";
        } catch (Exception e) {
            return "N/A";
        }
    }
}
