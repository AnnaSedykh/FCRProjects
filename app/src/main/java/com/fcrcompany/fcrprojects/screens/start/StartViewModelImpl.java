package com.fcrcompany.fcrprojects.screens.start;

import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.fcrcompany.fcrprojects.App;
import com.fcrcompany.fcrprojects.BuildConfig;
import com.fcrcompany.fcrprojects.data.api.Api;
import com.fcrcompany.fcrprojects.data.api.model.ProjectFile;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.io.IOException;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StartViewModelImpl extends StartViewModel {

    private static final String TAG = "StartViewModel";

    private MutableLiveData<String> token = new MutableLiveData<>();
    private MutableLiveData<Boolean> access = new MutableLiveData<>();

    private Api api;
    private CompositeDisposable disposables = new CompositeDisposable();


    public StartViewModelImpl(@NonNull Application application) {
        super(application);
        api = ((App) application).getApi();
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
        String query = "'" + BuildConfig.FCR_ACCOUNT + "' in owners";
        Disposable disposable = api.files("Bearer " + token, query)
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

    @Override
    public void receiveToken(Intent data, GoogleAccountCredential credential) {
        if (data != null && data.getExtras() != null) {
            String accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
            if (accountName != null) {
                credential.setSelectedAccountName(accountName);
                new ReceiveTokenTask(credential).execute();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class ReceiveTokenTask extends AsyncTask<Void, Void, String> {

        private GoogleAccountCredential credential;

        ReceiveTokenTask(GoogleAccountCredential credential) {
            this.credential = credential;
        }

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
            token.setValue(authToken);
        }
    }

}
