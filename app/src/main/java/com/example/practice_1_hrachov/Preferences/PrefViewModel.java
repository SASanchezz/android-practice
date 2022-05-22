package com.example.practice_1_hrachov.Preferences;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.practice_1_hrachov.Database.PreferencesRepository;
import com.example.practice_1_hrachov.Database.TblPrefs;

import java.util.List;

public class PrefViewModel extends AndroidViewModel {

    private PreferencesRepository mRepository;

    private LiveData<List<TblPrefs>> mAllPrefs;
    private List<TblPrefs> mAllPrefsList;
    private TblPrefs mPref;


    public PrefViewModel (Application application) {
        super(application);
        mRepository = new PreferencesRepository(application);
        mAllPrefs = mRepository.getAllPrefs();
        mAllPrefsList = mRepository.getAllPrefsList();
    }

    public TblPrefs getPrefByPk() { return mPref; }

    public LiveData<List<TblPrefs>> getAllPrefs() { return mAllPrefs; }

    public List<TblPrefs> getAllPrefsList() { return mAllPrefsList; }

    public TblPrefs getPrefByPk(String key) { return mRepository.getPrefByPk(key); }

    public void insert(TblPrefs pref) { mRepository.insert(pref); }

    public void update(TblPrefs pref) { mRepository.update(pref); }
}

