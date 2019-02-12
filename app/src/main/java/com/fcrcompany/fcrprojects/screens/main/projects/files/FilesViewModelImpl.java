package com.fcrcompany.fcrprojects.screens.main.projects.files;

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

public class FilesViewModelImpl extends FilesViewModel {

    private MutableLiveData<List<ProjectFile>> projectFiles = new MutableLiveData<>();

    private CompositeDisposable disposables = new CompositeDisposable();
    private Api api;
    private Prefs prefs;

    public FilesViewModelImpl(@NonNull Application application) {
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
    public void getProjectFiles(String parentId) {

        String token = prefs.getToken();
        String orderBy = "folder, name";
        String query = "'" + parentId + "' in parents";

        Disposable disposable = api.files("Bearer " + token, orderBy, ProjectFile.FIELDS_QUERY,query)
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
