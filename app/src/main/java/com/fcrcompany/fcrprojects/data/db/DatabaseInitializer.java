package com.fcrcompany.fcrprojects.data.db;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.fcrcompany.fcrprojects.data.db.room.AppDatabase;
import com.fcrcompany.fcrprojects.data.db.room.DatabaseRoomImpl;

public class DatabaseInitializer {

    public Database init(Context context){
        AppDatabase appDatabase = Room
                .databaseBuilder(context, AppDatabase.class, "fcrprojects.db")
                .build();
        return new DatabaseRoomImpl(appDatabase);
    }
}
