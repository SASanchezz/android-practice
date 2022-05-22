package com.example.practice_1_hrachov.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PreferencesRepository {

    private TblPrefsDao mTblPrefsDao;
    private LiveData<List<TblPrefs>> mAllPrefs;
    private List<TblPrefs> mAllPrefsList;

    public PreferencesRepository(Application application) {
        PrefsRoomDatabase db = PrefsRoomDatabase.getDatabase(application);
        mTblPrefsDao = db.tblPrefsDao();
        mAllPrefs = mTblPrefsDao.getAllPrefs();
        mAllPrefsList = mTblPrefsDao.getAllPrefsList();
    }

    public LiveData<List<TblPrefs>> getAllPrefs() {
        return mAllPrefs;
    }

    public List<TblPrefs> getAllPrefsList() {
        return mAllPrefsList;
    }

    public TblPrefs getPrefByPk(String key) {
        return mTblPrefsDao.getPref(key);
    }

    public void insert (TblPrefs tblPref) {
        new insertAsyncTask(mTblPrefsDao).execute(tblPref);
    }

    private static class insertAsyncTask extends AsyncTask<TblPrefs, Void, Void> {
        private TblPrefsDao mAsyncTaskDao;

        insertAsyncTask(TblPrefsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TblPrefs... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void update (TblPrefs pref) {
        new updateAsyncTask(mTblPrefsDao).execute(pref);
    }

    private static class updateAsyncTask extends AsyncTask<TblPrefs, Void, Void> {
        private TblPrefsDao mAsyncTaskDao;

        updateAsyncTask(TblPrefsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TblPrefs... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}
