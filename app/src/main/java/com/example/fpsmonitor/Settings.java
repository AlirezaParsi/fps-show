package com.example.fpsmonitor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

public class Settings {
    private static AlertDialog dialog;

    public static void createDialog(Context context) {
        dialog = new AlertDialog.Builder(context)
                .setView(settingsView(context))
                .setTitle(R.string.settings)
                .create();
        dialog.show();
    }

    private static View settingsView(final Context context) {
        ScrollView scrollView = new ScrollView(context);
        LinearLayout linearLayout = new LinearLayout(context);
        scrollView.addView(linearLayout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        {
            final Switch sw = new Switch(context);
            linearLayout.addView(sw);
            sw.setText(R.string.skip_first_screen_str);
            if (SharedPreferencesUtil.sharedPreferences.getBoolean(SharedPreferencesUtil.SKIP_FIRST_SCREEN, SharedPreferencesUtil.DEFAULT_SKIP_FIRST_SCREEN))
                sw.setChecked(true);
            sw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sw.isChecked()) {
                        new AlertDialog.Builder(context)
                                .setTitle(R.string.notice)
                                .setMessage(R.string.skip_first_screen_str2)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        SharedPreferencesUtil.sharedPreferences.edit().putBoolean(SharedPreferencesUtil.SKIP_FIRST_SCREEN, true).commit();
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        sw.setChecked(false);
                                    }
                                })
                                .create().show();
                    } else {
                        SharedPreferencesUtil.sharedPreferences.edit().putBoolean(SharedPreferencesUtil.SKIP_FIRST_SCREEN, false).commit();
                    }
                }
            });
        }

        {
            LinearLayout line = new LinearLayout(context);
            linearLayout.addView(line);

            TextView textView = new TextView(context);
            line.addView(textView);
            textView.setText(R.string.refresh_interval);

            final EditText editText = new EditText(context);
            line.addView(editText);
            editText.setHint(R.string.default_value);
            editText.setText(SharedPreferencesUtil.sharedPreferences.getInt(SharedPreferencesUtil.REFRESHING_DELAY, SharedPreferencesUtil.DEFAULT_DELAY) + "");
            editText.setWidth(500);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        SharedPreferencesUtil.sharedPreferences.edit().putInt(SharedPreferencesUtil.REFRESHING_DELAY, Integer.parseInt(s.toString())).commit();
                    } catch (Exception e) {
                        SharedPreferencesUtil.sharedPreferences.edit().remove(SharedPreferencesUtil.REFRESHING_DELAY).commit();
                    }
                }
            });
        }
        {
            TextView textView = new TextView(context);
            linearLayout.addView(textView);
            textView.setText(R.string.interval_notice);
        }
        {
            LinearLayout line = new LinearLayout(context);
            linearLayout.addView(line);

            TextView textView = new TextView(context);
            line.addView(textView);
            textView.setText(R.string.size_multiple);

            final EditText editText = new EditText(context);
            line.addView(editText);
            editText.setHint(R.string.default_value);
            editText.setText(SharedPreferencesUtil.sharedPreferences.getFloat(SharedPreferencesUtil.SIZE_MULTIPLE, SharedPreferencesUtil.SIZE_MULTIPLE_DEFAULT) + "");
            editText.setWidth(500);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        SharedPreferencesUtil.sharedPreferences.edit().putFloat(SharedPreferencesUtil.SIZE_MULTIPLE, Float.parseFloat(s.toString())).commit();
                    } catch (Exception e) {
                        SharedPreferencesUtil.sharedPreferences.edit().remove(SharedPreferencesUtil.SIZE_MULTIPLE).commit();
                    }
                }
            });
        }
        {
            TextView textView = new TextView(context);
            textView.setText(R.string.moni_ctl);
            linearLayout.addView(textView);
            {
                final Switch sw = new Switch(context);
                linearLayout.addView(sw);
                sw.setText(R.string.show_fps);
                if (SharedPreferencesUtil.sharedPreferences.getBoolean(SharedPreferencesUtil.SHOW_FPS, SharedPreferencesUtil.SHOW_FPS_DEFAULT))
                    sw.setChecked(true);
                sw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferencesUtil.sharedPreferences.edit().putBoolean(SharedPreferencesUtil.SHOW_FPS, sw.isChecked()).commit();
                    }
                });
            }
        }
        return scrollView;
    }
}
