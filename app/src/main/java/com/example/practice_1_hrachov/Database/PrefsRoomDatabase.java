package com.example.practice_1_hrachov.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;

@Database(entities = {TblPrefs.class}, version = 1, exportSchema = false)
public abstract class PrefsRoomDatabase extends RoomDatabase {
    public abstract TblPrefsDao tblPrefsDao();

    private static PrefsRoomDatabase INSTANCE;

    public static PrefsRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PrefsRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PrefsRoomDatabase.class, "DBPrefs")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries() // Dangerous
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){
        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final TblPrefsDao mDao;
        private List<TblPrefs> allPreferences;

        String[] prefKeys = {"fontColor", "backgroundColor"};
        String[] prefDefaultValues = {"black", "blue"};


        PopulateDbAsync(PrefsRoomDatabase db) {
            mDao = db.tblPrefsDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            allPreferences = mDao.getAllPrefsList();

            if (allPreferences.size() != 0) {
                //TODO: get info from preferences storage
                return null;
            }

            for (int i = 0; i < prefKeys.length; i++) {
                TblPrefs preference = new TblPrefs(prefKeys[i], prefDefaultValues[i]);
                mDao.insert(preference);
            }
            return null;
        }
    }
}
