package com.fcrcompany.fcrprojects;

import android.app.Application;

import com.fcrcompany.fcrprojects.data.db.Database;
import com.fcrcompany.fcrprojects.data.db.DatabaseInitializer;

public class App extends Application {

    private Database database;

    @Override
    public void onCreate() {
        super.onCreate();

        database = new DatabaseInitializer().init(this);
    }

    public Database getDatabase() {
        return database;
    }
}
