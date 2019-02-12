package com.fcrcompany.fcrprojects.screens.main.projects.photo;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.fcrcompany.fcrprojects.data.api.model.ProjectFile;

import java.util.List;

public abstract class PhotoViewModel extends AndroidViewModel {

    public PhotoViewModel(@NonNull Application application) {
        super(application);
    }

    public abstract void getPhotos(String parentId);

    public abstract LiveData<List<ProjectFile>> photos();
}
