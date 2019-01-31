package com.fcrcompany.fcrprojects.data.db;

import com.fcrcompany.fcrprojects.data.db.model.ProjectFileEntity;

import java.util.List;

import io.reactivex.Flowable;

public interface Database {

    void saveProjectFiles(List<ProjectFileEntity> projectFiles);

    void saveProjectFile(ProjectFileEntity projectFile);

    Flowable<List<ProjectFileEntity>> getProjectFilesInFolder(String folderId);

    ProjectFileEntity getProjectFileById(String fileId);

    List<ProjectFileEntity> getProjectFilesByNameAndType(String fileName, String mimeType);

}
