package com.fcrcompany.fcrprojects.screens.main.projects;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.fcrcompany.fcrprojects.App;
import com.fcrcompany.fcrprojects.data.api.Api;
import com.fcrcompany.fcrprojects.data.api.model.ProjectFile;
import com.fcrcompany.fcrprojects.data.prefs.Prefs;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProjectsViewModelImpl extends ProjectsViewModel {

    private MutableLiveData<List<ProjectFile>> projectFiles = new MutableLiveData<>();

    private CompositeDisposable disposables = new CompositeDisposable();
    private Api api;
    private Prefs prefs;

    public ProjectsViewModelImpl(@NonNull Application application) {
        super(application);

        App app = (App) application;
        api = app.getApi();
        prefs = app.getPrefs();
    }

    @Override
    public LiveData<List<ProjectFile>> projectFiles() {
        return projectFiles;
    }

    @Override
    public void getProjectFiles(String type) {

        String token = prefs.getToken();
        String folderId = getFolderId(type);
        String orderBy = "folder, name";
        String query = "'" + folderId + "' in parents";

        Disposable disposable = api.files("Bearer " + token, orderBy, query)
                .subscribeOn(Schedulers.io())
                .subscribe(driveResponse -> {
                    List<ProjectFile> data = driveResponse.files;
                    if (data == null || data.isEmpty()) {
                        projectFiles.postValue(null);
                    } else {
                        projectFiles.postValue(data);
                    }
                });

        disposables.add(disposable);
    }

    private String getFolderId(String type) {
        String folderId = "";
        switch (type){
            case ProjectFile.TYPE_CURRENT:
                folderId = ProjectFile.CURRENT_FOLDER_ID;
                break;
            case ProjectFile.TYPE_ARCHIVE:
                folderId = ProjectFile.ARCHIVE_FOLDER_ID;
        }
        return folderId;
    }
}
