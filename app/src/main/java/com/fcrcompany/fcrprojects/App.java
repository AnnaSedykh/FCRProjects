package com.fcrcompany.fcrprojects;

import android.app.Application;

import com.fcrcompany.fcrprojects.data.api.Api;
import com.fcrcompany.fcrprojects.data.api.ApiInitializer;
import com.fcrcompany.fcrprojects.data.db.Database;
import com.fcrcompany.fcrprojects.data.db.DatabaseInitializer;
import com.fcrcompany.fcrprojects.data.prefs.Prefs;
import com.fcrcompany.fcrprojects.data.prefs.PrefsImpl;

public class App extends Application {

    private Api api;
    private Database database;
    private Prefs prefs;

    @Override
    public void onCreate() {
        super.onCreate();

        api = new ApiInitializer().init();
        database = new DatabaseInitializer().init(this);
        prefs = new PrefsImpl(this);
    }

    public Api getApi() {
        return api;
    }

    public Database getDatabase() {
        return database;
    }

    public Prefs getPrefs() {
        return prefs;
    }
}
