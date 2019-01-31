package com.fcrcompany.fcrprojects.screens.start;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.fcrcompany.fcrprojects.data.db.Database;

import io.reactivex.disposables.CompositeDisposable;

public class StartViewModelImpl extends StartViewModel {

    private MutableLiveData<Boolean> access = new MutableLiveData<>();

    private Database database;
    private CompositeDisposable disposables = new CompositeDisposable();


    public StartViewModelImpl(@NonNull Application application) {
        super(application);
    }

    @Override
    public void getProjects() {


    }

    @Override
    public LiveData<Boolean> access() {
        return access;
    }
}
