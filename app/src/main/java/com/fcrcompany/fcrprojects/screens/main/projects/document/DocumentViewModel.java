package com.fcrcompany.fcrprojects.screens.main.projects.document;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.io.File;

public abstract class DocumentViewModel extends AndroidViewModel {

    public DocumentViewModel(@NonNull Application application) {
        super(application);
    }

    public abstract void downloadFile(String fileId, File fileDir);

    public abstract LiveData<Boolean> isDownloaded();
}
