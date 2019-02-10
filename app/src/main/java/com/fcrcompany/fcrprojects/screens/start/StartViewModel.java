package com.fcrcompany.fcrprojects.screens.start;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

public abstract class StartViewModel extends AndroidViewModel {

    public StartViewModel(@NonNull Application application) {
        super(application);
    }

    public abstract LiveData<String> token();

    public abstract LiveData<Boolean> access();

    public abstract void receiveToken(String accountName);

    public abstract void checkAccess(String token);
}
