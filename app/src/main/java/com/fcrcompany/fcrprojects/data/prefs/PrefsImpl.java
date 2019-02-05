package com.fcrcompany.fcrprojects.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsImpl implements Prefs {

    private static final String PREFS_NAME = "prefs";
    private static final String KEY_AUTH_TOKEN = "auth_token";
    private Context context;

    public PrefsImpl(Context context) {
        this.context = context;
    }

    @Override
    public String getToken() {
        return getPrefs().getString(KEY_AUTH_TOKEN, null);
    }

    @Override
    public void saveToken(String token) {
        getPrefs().edit().putString(KEY_AUTH_TOKEN, token).apply();
    }

    private SharedPreferences getPrefs(){
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
}
