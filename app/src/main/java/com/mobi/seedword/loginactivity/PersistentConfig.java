package com.mobi.seedword.loginactivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PersistentConfig {
    private static final String PREFS_NAME = "prefs_file";

    private SharedPreferences settings;

    public PersistentConfig(Context context) {
        settings = context.getSharedPreferences(PREFS_NAME, 0);
    }

    public String getCookieString() {
        Log.e("getCookieString", settings.getString("my_cookie", ""));

        return settings.getString("my_cookie", "");
    }

    public void setCookie(String cookie) {
        Log.e("setCookie", cookie);

        SharedPreferences.Editor editor = settings.edit();
        editor.putString("my_cookie", cookie);
        editor.commit();
    }
}