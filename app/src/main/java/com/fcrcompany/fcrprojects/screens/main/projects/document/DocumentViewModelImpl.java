package com.fcrcompany.fcrprojects.screens.main.projects.document;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.fcrcompany.fcrprojects.App;
import com.fcrcompany.fcrprojects.data.api.Api;
import com.fcrcompany.fcrprojects.data.prefs.Prefs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

class DocumentViewModelImpl extends DocumentViewModel {

    private MutableLiveData<Boolean> isDownloaded = new MutableLiveData<>();

    private CompositeDisposable disposables = new CompositeDisposable();
    private Api api;
    private Prefs prefs;

    public DocumentViewModelImpl(@NonNull Application application) {
        super(application);

        App app = (App) application;
        api = app.getApi();
        prefs = app.getPrefs();
    }

    @Override
    public LiveData<Boolean> isDownloaded() {
        return isDownloaded;
    }

    @Override
    public void downloadFile(String fileId, File fileDir) {

        if (fileDir.exists()) {
            isDownloaded.setValue(true);
            return;
        }

        String token = prefs.getToken();
        String altParam = "media";

        Disposable disposable = api.downloadFile("Bearer " + token, fileId, altParam)
                .subscribeOn(Schedulers.io())
                .subscribe(responseBody -> {

                    boolean isWrittenToDisk = writeResponseBodyToDisk(responseBody, fileDir);
                    isDownloaded.postValue(isWrittenToDisk);
                });

        disposables.add(disposable);
    }

    private boolean writeResponseBodyToDisk(ResponseBody responseBody, File fileDir) {
        try {

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = responseBody.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = responseBody.byteStream();
                outputStream = new FileOutputStream(fileDir);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("File Download: ", fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
