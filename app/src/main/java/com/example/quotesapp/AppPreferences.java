package com.example.quotesapp;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {

    private static final String PREF_NAME = "MyAppPreferences";
    private static final String KEY_THEME_MODE = "theme_mode";

    public static final int THEME_MODE_LIGHT = 0;
    public static final int THEME_MODE_DARK = 1;

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static int getThemeMode(Context context) {
        return getPreferences(context).getInt(KEY_THEME_MODE, THEME_MODE_LIGHT);
    }

    public static void setThemeMode(Context context, int themeMode) {
        getPreferences(context).edit().putInt(KEY_THEME_MODE, themeMode).apply();
    }
}


