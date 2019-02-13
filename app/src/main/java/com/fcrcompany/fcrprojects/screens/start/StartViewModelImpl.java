package com.fcrcompany.fcrprojects.screens.start;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.fcrcompany.fcrprojects.App;
import com.fcrcompany.fcrprojects.BuildConfig;
import com.fcrcompany.fcrprojects.data.api.Api;
import com.fcrcompany.fcrprojects.data.api.model.ProjectFile;
import com.fcrcompany.fcrprojects.data.prefs.Prefs;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.drive.DriveScopes;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StartViewModelImpl extends StartViewModel {

    private MutableLiveData<String> token = new MutableLiveData<>();
    private MutableLiveData<Boolean> access = new MutableLiveData<>();

    private Api api;
    private Prefs prefs;
    private GoogleAccountCredential credential;
    private CompositeDisposable disposables = new CompositeDisposable();


    public StartViewModelImpl(@NonNull Application application) {
        super(application);
        App app = (App) application;
        api = app.getApi();
        prefs = app.getPrefs();
        credential = GoogleAccountCredential.usingOAuth2(app.getApplicationContext(), Collections.singleton(DriveScopes.DRIVE));
    }

    public LiveData<String> token() {
        return token;
    }

    @Override
    public LiveData<Boolean> access() {
        return access;
    }

    @Override
    public void checkAccess(String token) {

       if (BuildConfig.FCR_ACCOUNT.equals(prefs.getAccountName())) {
            access.setValue(true);
        } else {
           String orderBy = "folder, name";
            String query = "'" + BuildConfig.FCR_ACCOUNT + "' in owners " +
                    "and (mimeType != 'application/vnd.google-apps.folder' " +
                    "and trashed = false)";
            Disposable disposable = api.files("Bearer " + token, orderBy, ProjectFile.FIELDS_QUERY, query)
                    .subscribeOn(Schedulers.io())
                    .subscribe(driveResponse -> {
                        List<ProjectFile> data = driveResponse.files;
                        if (data == null || data.isEmpty()) {
                            access.postValue(false);
                        } else {
                            access.postValue(true);
                        }
                    });

            disposables.add(disposable);
        }
    }

    @Override
    public void receiveToken(String accountName) {
        if (accountName != null) {
            credential.setSelectedAccountName(accountName);
            new ReceiveTokenTask().execute();
        } else {
            token.setValue(null);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class ReceiveTokenTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                return credential.getToken();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GoogleAuthException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String authToken) {
            prefs.setToken(authToken);
            token.setValue(authToken);
        }
    }

}
