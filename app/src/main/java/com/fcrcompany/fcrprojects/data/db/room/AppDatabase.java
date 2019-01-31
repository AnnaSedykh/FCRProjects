package com.fcrcompany.fcrprojects.data.db.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.fcrcompany.fcrprojects.data.db.model.ProjectFileEntity;

@Database(entities = {ProjectFileEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ProjectFileDao projectFileDao();
}
