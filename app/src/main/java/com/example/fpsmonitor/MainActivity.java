package com.example.fpsmonitor;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // استفاده از layout جدید

        if (!FloatingWindow.doExit) {
            Toast.makeText(this, "لطفاً ابتدا پنجره شناور را ببندید", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // بررسی دسترسی روت
        if (!PermissionManager.hasRootAccess()) {
            Toast.makeText(this, "دسترسی روت لازم است!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // تشخیص خودکار مسیرها
        String fpsPath = FpsPathProvider.detectFpsPath();
        if (fpsPath == null) {
            Toast.makeText(this, "دستگاه شما پشتیبانی نمی‌شود", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // ادامه کدهای شما...
        permissionCheck();
    }

    void permissionCheck() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(this)) {
                startService(new Intent(this, FloatingWindow.class));
            } else {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, "خطا در دریافت مجوز", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            startService(new Intent(this, FloatingWindow.class));
        }
    }
}
