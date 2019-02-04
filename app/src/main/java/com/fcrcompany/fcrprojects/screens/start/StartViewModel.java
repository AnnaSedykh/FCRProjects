package com.fcrcompany.fcrprojects.screens.start;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

public abstract class StartViewModel extends AndroidViewModel {

    public StartViewModel(@NonNull Application application) {
        super(application);
    }

    public abstract LiveData<String> token();

    public abstract LiveData<Boolean> access();

    public abstract void receiveToken(Intent data, GoogleAccountCredential credential);

    public abstract void checkAccess(String token);
}
