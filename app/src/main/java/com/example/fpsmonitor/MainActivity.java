package com.example.fpsmonitor;

import android.app.Activity;
import android.os.Bundle;
import android.view.Choreographer;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView fpsText;
    private JniTools jniTools = new JniTools(); // فرض می‌کنیم JniTools هست

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fpsText = findViewById(R.id.fps_text);

        // فراخوانی JNI هر 1 ثانیه
        updateFpsFromJNI();

        // Choreographer برای FPS تخمینی
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            private int frameCount = 0;
            private long startTime = System.nanoTime();

            @Override
            public void doFrame(long frameTimeNanos) {
                frameCount++;
                long currentTime = System.nanoTime();
                if ((currentTime - startTime) >= 1000000000L) {
                    float fps = frameCount / ((currentTime - startTime) / 1000000000.0f);
                    if (fpsText != null && jniTools.getFps().equals("Error: No FPS data")) {
                        fpsText.setText(String.format("API: %.1f", fps));
                    }
                    frameCount = 0;
                    startTime = currentTime;
                }
                Choreographer.getInstance().postFrameCallback(this);
            }
        });
    }

    private void updateFpsFromJNI() {
        if (fpsText != null) {
            String fps = jniTools.getFps();
            if (!fps.equals("Error: No FPS data")) {
                fpsText.setText(String.format("JNI: %s", fps));
            }
        }
    }
}
