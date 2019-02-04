package com.fcrcompany.fcrprojects;

import android.app.Application;

import com.fcrcompany.fcrprojects.data.api.Api;
import com.fcrcompany.fcrprojects.data.api.ApiInitializer;
import com.fcrcompany.fcrprojects.data.db.Database;
import com.fcrcompany.fcrprojects.data.db.DatabaseInitializer;

public class App extends Application {

    private Api api;
    private Database database;
    private String token;

    @Override
    public void onCreate() {
        super.onCreate();

        api = new ApiInitializer().init();
        database = new DatabaseInitializer().init(this);
    }

    public Api getApi() {
        return api;
    }

    public Database getDatabase() {
        return database;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
