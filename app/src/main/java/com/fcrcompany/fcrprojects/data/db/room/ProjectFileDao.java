package com.fcrcompany.fcrprojects.data.db.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.fcrcompany.fcrprojects.data.db.model.ProjectFileEntity;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ProjectFileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveProjectFiles(List<ProjectFileEntity> projectFiles);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveProjectFile(ProjectFileEntity projectFile);

    @Query("SELECT * FROM ProjectFile WHERE parentId = :folderId")
    Flowable<List<ProjectFileEntity>> getProjectFilesInFolder(String folderId);

    @Query("SELECT * FROM ProjectFile WHERE fileId = :fileId")
    ProjectFileEntity getProjectFileById(String fileId);

    @Query("SELECT * FROM ProjectFile WHERE name LIKE :fileName AND mimeType = :mimeType")
    List<ProjectFileEntity> getProjectFilesByNameAndType(String fileName, String mimeType);

}
