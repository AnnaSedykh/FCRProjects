package com.fcrcompany.fcrprojects.screens.main.projects.photo;

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

class PhotoViewModelImpl extends PhotoViewModel {

    private MutableLiveData<List<ProjectFile>> photos = new MutableLiveData<>();

    private CompositeDisposable disposables = new CompositeDisposable();
    private Api api;
    private Prefs prefs;

    public PhotoViewModelImpl(@NonNull Application application) {
        super(application);
        App app = (App) application;
        api = app.getApi();
        prefs = app.getPrefs();
    }

    @Override
    public LiveData<List<ProjectFile>> photos() {
        return photos;
    }

    @Override
    public void getPhotos(String parentId) {

        String token = prefs.getToken();
        String orderBy = "folder, createdTime desc";
        String query = "'" + parentId + "' in parents " +
                "and ((mimeType = 'application/vnd.google-apps.folder' or mimeType = 'image/jpeg') " +
                "and trashed = false)";

        Disposable disposable = api.files("Bearer " + token, orderBy, ProjectFile.FIELDS_QUERY, query)
                .subscribeOn(Schedulers.io())
                .subscribe(driveResponse -> {
                    List<ProjectFile> data = driveResponse.files;
                    if (data == null || data.isEmpty()) {
                        photos.postValue(null);
                    } else {
                        photos.postValue(data);
                    }
                });

        disposables.add(disposable);
    }

}
