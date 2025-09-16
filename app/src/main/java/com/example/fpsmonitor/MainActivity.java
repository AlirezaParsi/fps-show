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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends Activity {
    ScrollView mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainView = new ScrollView(this);
        setContentView(mainView);

        if (!FloatingWindow.doExit) {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.please_close_app_first), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        RefreshingDateThread.cpunum = JniTools.getCpuNum();  // برای سازگاری
        FloatingWindow.linen = Support.CheckSupport();  // فقط FPS
        SharedPreferencesUtil.init(this);

        if (SharedPreferencesUtil.sharedPreferences.getBoolean(SharedPreferencesUtil.SKIP_FIRST_SCREEN, SharedPreferencesUtil.DEFAULT_SKIP_FIRST_SCREEN)) {
            if (checkRoot()) {
                permissionCheck();
            } else {
                Toast.makeText(this, "Root access required!", Toast.LENGTH_LONG).show();
            }
            finish();
            return;
        }

        addView();
    }

    // چک ساده root
    private boolean checkRoot() {
        try {
            Process process = Runtime.getRuntime().exec("su");
            OutputStreamWriter osw = new OutputStreamWriter(process.getOutputStream());
            osw.write("exit\n");
            osw.flush();
            osw.close();
            process.waitFor();
            return process.exitValue() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    void addView() {
        LinearLayout main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        mainView.addView(main);
        {
            TextView textView = new TextView(this);
            textView.setText(getResources().getString(R.string.support_fps_mo) + Tools.bool2Text(Support.support_fps, this));
            main.addView(textView);
        }
        {
            TextView textView = new TextView(this);
            textView.setText(R.string.Unsupport_reason);
            main.addView(textView);
        }
        {
            Button button = new Button(this);
            button.setText(R.string.show_floatingwindow);
            main.addView(button);
            button.setOnClickListener(view -> {
                if (checkRoot()) {
                    permissionCheck();
                    finish();
                } else {
                    Toast.makeText(this, "Root access required! Grant root to the app.", Toast.LENGTH_LONG).show();
                }
            });
        }
        {
            Button button = new Button(this);
            button.setText(R.string.settings);
            main.addView(button);
            button.setOnClickListener(view -> com.example.fpsmonitor.Settings.createDialog(MainActivity.this));
        }
        {
            TextView textView = new TextView(this);
            textView.setText("This app requires root access to read FPS without changing SELinux.");
            main.addView(textView);
        }
        {
            LinearLayout line = new LinearLayout(MainActivity.this);
            main.addView(line);
            {
                TextView textView = new TextView(this);
                textView.setText(R.string.visit_github);
                line.addView(textView);
                textView.setOnClickListener(view -> {
                    Uri uri = Uri.parse("https://github.com/libxzr/PerfMon-Plus");  // لینک اصلی، می‌تونی تغییر بدی
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                });
            }
        }
    }

    void permissionCheck() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(MainActivity.this)) {
                Intent intent = new Intent(MainActivity.this, FloatingWindow.class);
                startService(intent);
            } else {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                } catch (Exception e) {
                }
            }
        } else {
            Intent intent = new Intent(MainActivity.this, FloatingWindow.class);
            startService(intent);
        }
    }
}