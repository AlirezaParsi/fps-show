package com.example.fpsmonitor;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.fpsmonitor.RefreshingDateThread.fps;

public class FloatingWindow extends Service {
    static String TAG = "FloatingWindow";
    public static boolean doExit = true;
    static WindowManager.LayoutParams params;
    static WindowManager windowManager;
    static int statusBarHeight = -1;
    LinearLayout main;
    static TextView line[];  // فقط یک خط برای FPS
    static int linen = 1;  // فقط یک خط
    static Handler uiRefresher;
    static float sizeMultipleNow;

    static boolean showFpsNow;

    @SuppressLint("ClickableViewAccessibility")
    void init() {
        sizeMultipleNow = SharedPreferencesUtil.sharedPreferences.getFloat(SharedPreferencesUtil.SIZE_MULTIPLE, SharedPreferencesUtil.SIZE_MULTIPLE_DEFAULT);
        showFpsNow = SharedPreferencesUtil.sharedPreferences.getBoolean(SharedPreferencesUtil.SHOW_FPS, SharedPreferencesUtil.SHOW_FPS_DEFAULT);
        params = new WindowManager.LayoutParams();
        windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }

        params.format = PixelFormat.RGBA_8888;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 0;
        params.y = 0;
        params.width = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 120, getResources().getDisplayMetrics()) * sizeMultipleNow);
        params.height = 300;
        main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setBackgroundColor(getResources().getColor(R.color.floating_window_backgrouns));
        main.setPadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 5, getResources().getDisplayMetrics()), 0, 0, 0);
        TextView close = new TextView(this);
        close.setText(R.string.close);
        close.setTextSize(TypedValue.COMPLEX_UNIT_PX, close.getTextSize() * sizeMultipleNow);
        close.setTextColor(getResources().getColor(R.color.white));
        main.addView(close);
        close.setOnClickListener(view -> stopSelf());
        close.setOnLongClickListener(view -> {
            SharedPreferencesUtil.sharedPreferences.edit().putBoolean(SharedPreferencesUtil.SKIP_FIRST_SCREEN, false).commit();
            Toast.makeText(FloatingWindow.this, R.string.skip_first_screen_str_disabled, Toast.LENGTH_LONG).show();
            return false;
        });
        main.setOnTouchListener(new View.OnTouchListener() {
            private int x, y;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = (int) event.getRawX();
                        y = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int nowX = (int) event.getRawX();
                        int nowY = (int) event.getRawY();
                        int movedX = nowX - x;
                        int movedY = nowY - y;
                        x = nowX;
                        y = nowY;
                        params.x = params.x + movedX;
                        params.y = params.y + movedY;
                        windowManager.updateViewLayout(main, params);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        windowManager.addView(main, params);

        main.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
    }

    void monitorInit() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        line = new TextView[linen];

        params.height = (linen + 1) * (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics()) * sizeMultipleNow);

        windowManager.updateViewLayout(main, params);
        uiRefresher = new Handler(message -> {
            if (showFpsNow) {
                line[0].setText("fps " + fps);
            }
            return false;
        });

        for (int i = 0; i < linen; i++) {
            line[i] = new TextView(this);
            line[i].setTextColor(getResources().getColor(R.color.white));
            line[i].setLayoutParams(layoutParams);
            line[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, line[i].getTextSize() * sizeMultipleNow);
            main.addView(line[i]);
        }
        windowManager.updateViewLayout(main, params);
        new RefreshingDateThread().start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        doExit = false;
        init();
        monitorInit();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        doExit = true;
        try {
            windowManager.removeView(main);
        } catch (Exception e) {
        }
        super.onDestroy();
    }
}
