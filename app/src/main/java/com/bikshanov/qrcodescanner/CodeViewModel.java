package com.bikshanov.qrcodescanner;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CodeViewModel extends AndroidViewModel {

    private CodeRepository repository;
    private LiveData<List<Code>> allCodes;

    public CodeViewModel(@NonNull Application application) {
        super(application);
        repository = new CodeRepository(application);
        allCodes = repository.getAllCodes();
    }

    public void insert(Code code) {
        repository.insert(code);
    }

    public void update(Code code) {
        repository.update(code);
    }

    public void delete(Code code) {
        repository.delete(code);
    }

    public void deleteAllCodes() {
        repository.deleteAllCodes();
    }

    public LiveData<List<Code>> getAllCodes() {
        return allCodes;
    }
}
