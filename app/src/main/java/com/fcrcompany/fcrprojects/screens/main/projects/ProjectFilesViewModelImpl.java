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

public class ProjectFilesViewModelImpl extends ProjectFilesViewModel {

    private MutableLiveData<List<ProjectFile>> projectFiles = new MutableLiveData<>();

    private CompositeDisposable disposables = new CompositeDisposable();
    private Api api;
    private Prefs prefs;

    public ProjectFilesViewModelImpl(@NonNull Application application) {
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
    public void getProjectFiles(String folderId) {

        String token = prefs.getToken();
        String orderBy = "folder, name";
        String query = "'" + folderId + "' in parents and trashed = false";

        Disposable disposable = api.files("Bearer " + token, orderBy, ProjectFile.FIELDS_QUERY, query)
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
}
