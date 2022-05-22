package com.example.practice_1_hrachov;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.practice_1_hrachov.Database.PreferencesRepository;
import com.example.practice_1_hrachov.Database.TblPrefs;

import java.util.List;

public class TblPrefsViewModel extends AndroidViewModel {

    private PreferencesRepository mRepository;

    private LiveData<List<TblPrefs>> mAllPrefs;

    public TblPrefsViewModel (Application application) {
        super(application);
        mRepository = new PreferencesRepository(application);
        mAllPrefs = mRepository.getAllPrefs();
    }

    LiveData<List<TblPrefs>> getAllPrefs() { return mAllPrefs; }

    public void insert(TblPrefs pref) { mRepository.insert(pref); }
}
