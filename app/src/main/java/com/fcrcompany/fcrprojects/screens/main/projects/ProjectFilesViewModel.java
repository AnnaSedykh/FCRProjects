package com.fcrcompany.fcrprojects.screens.main.projects;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.fcrcompany.fcrprojects.data.api.model.ProjectFile;

import java.util.List;

public abstract class ProjectFilesViewModel extends AndroidViewModel {

    public ProjectFilesViewModel(@NonNull Application application) {
        super(application);
    }

    public abstract void getProjectFiles(String folderId);

    public abstract LiveData<List<ProjectFile>> projectFiles();
}
