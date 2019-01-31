package com.fcrcompany.fcrprojects.data.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "ProjectFile")
public class ProjectFileEntity {

    @NonNull
    @PrimaryKey
    public String fileId;

    public String parentId;

    public String name;

    public String mimeType;

    public long creationTime;

}
