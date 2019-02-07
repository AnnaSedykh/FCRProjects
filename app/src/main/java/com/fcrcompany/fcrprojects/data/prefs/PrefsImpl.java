package com.fcrcompany.fcrprojects.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsImpl implements Prefs {

    private static final String PREFS_NAME = "prefs";
    private static final String KEY_AUTH_TOKEN = "auth_token";
    private static final String KEY_ACCOUNT_NAME = "email";
    private static final String KEY_ACCESS_REQUEST_SENT = "access request is sent";
    private static final String KEY_ACCOUNT_CHOOSER_ON_TOP = "account chooser on top";
    private Context context;

    public PrefsImpl(Context context) {
        this.context = context;
    }

    @Override
    public String getToken() {
        return getPrefs().getString(KEY_AUTH_TOKEN, null);
    }

    @Override
    public void setToken(String token) {
        getPrefs().edit().putString(KEY_AUTH_TOKEN, token).apply();
    }

    @Override
    public String getAccountName() {
        return getPrefs().getString(KEY_ACCOUNT_NAME, null);
    }

    @Override
    public void setAccountName(String accountName) {
        getPrefs().edit().putString(KEY_ACCOUNT_NAME, accountName).apply();
    }

    @Override
    public boolean isAccessRequestSent() {
        return getPrefs().getBoolean(KEY_ACCESS_REQUEST_SENT, false);
    }

    @Override
    public void setAccessRequestSent(boolean isSent) {
        getPrefs().edit().putBoolean(KEY_ACCESS_REQUEST_SENT, isSent).apply();
    }

    @Override
    public void setAccountChooserOnTop(boolean isOnTop) {
        getPrefs().edit().putBoolean(KEY_ACCOUNT_CHOOSER_ON_TOP, isOnTop).apply();
    }

    @Override
    public boolean isAccountChooserOnTop() {
        return getPrefs().getBoolean(KEY_ACCOUNT_CHOOSER_ON_TOP, false);
    }

    private SharedPreferences getPrefs(){
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
}
