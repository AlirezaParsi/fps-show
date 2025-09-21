package com.example.fpsmonitor;

import android.util.Log;
import java.io.*;

public class PermissionManager {
    private static final String TAG = "PermissionManager";
    
    // بررسی دسترسی روت
    public static boolean hasRootAccess() {
        return executeCommand("echo root test").success;
    }
    
    // اجرای دستور با دسترسی روت
    public static CommandResult executeCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            OutputStream os = process.getOutputStream();
            InputStream is = process.getInputStream();
            
            os.write((command + "\n").getBytes());
            os.write("exit\n".getBytes());
            os.flush();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            
            process.waitFor();
            return new CommandResult(process.exitValue() == 0, output.toString());
            
        } catch (Exception e) {
            Log.e(TAG, "خطا در اجرای دستور: " + e.getMessage());
            return new CommandResult(false, e.getMessage());
        }
    }
    
    // خواندن محتوای یک فایل با دسترسی روت
    public static String readFileWithRoot(String filePath) {
        CommandResult result = executeCommand("cat " + filePath);
        if (result.success) {
            return result.output.trim();
        }
        return null;
    }
    
    // کلاس برای ذخیره نتیجه دستور
    public static class CommandResult {
        public final boolean success;
        public final String output;
        
        public CommandResult(boolean success, String output) {
            this.success = success;
            this.output = output;
        }
    }
}
