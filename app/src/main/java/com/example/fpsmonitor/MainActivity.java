package com.example.fpsmonitor;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!FloatingWindow.doExit) {
            Toast.makeText(this, "لطفاً ابتدا پنجره شناور را ببندید", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // نمایش اطلاعات ساده
        TextView tvDeviceInfo = findViewById(R.id.tvDeviceInfo);
        TextView tvFpsSupport = findViewById(R.id.tvFpsSupport);
        
        tvDeviceInfo.setText("Device: " + Build.MANUFACTURER + " " + Build.MODEL);
        tvFpsSupport.setText("FPS Support: Checking...");

        Button btnShowFps = findViewById(R.id.btnShowFps);
        btnShowFps.setOnClickListener(view -> {
            permissionCheck();
        });

        Button btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(view -> {
            Settings.createDialog(MainActivity.this);
        });
    }

    void permissionCheck() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(this)) {
                startService(new Intent(this, FloatingWindow.class));
                finish();
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
            finish();
        }
    }
}
