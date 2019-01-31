package com.fcrcompany.fcrprojects.data.db.room;

import com.fcrcompany.fcrprojects.data.db.Database;
import com.fcrcompany.fcrprojects.data.db.model.ProjectFileEntity;

import java.util.List;

import io.reactivex.Flowable;

public class DatabaseRoomImpl implements Database {

    private AppDatabase database;

    public DatabaseRoomImpl(AppDatabase database) {
        this.database = database;
    }

    @Override
    public void saveProjectFiles(List<ProjectFileEntity> projectFiles) {
        database.projectFileDao().saveProjectFiles(projectFiles);
    }

    @Override
    public void saveProjectFile(ProjectFileEntity projectFile) {
        database.projectFileDao().saveProjectFile(projectFile);
    }

    @Override
    public Flowable<List<ProjectFileEntity>> getProjectFilesInFolder(String folderId) {
        return database.projectFileDao().getProjectFilesInFolder(folderId);
    }

    @Override
    public ProjectFileEntity getProjectFileById(String fileId) {
        return database.projectFileDao().getProjectFileById(fileId);
    }

    @Override
    public List<ProjectFileEntity> getProjectFilesByNameAndType(String fileName, String mimeType) {
        return database.projectFileDao().getProjectFilesByNameAndType(fileName, mimeType);
    }
}
