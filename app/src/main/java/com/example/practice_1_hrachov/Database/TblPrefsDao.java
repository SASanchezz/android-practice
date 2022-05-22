package com.example.practice_1_hrachov.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TblPrefsDao {

        @Insert
        void insert(TblPrefs tblPref);

        @Query("DELETE FROM TblPrefs")
        void deleteAll();

        @Query("SELECT * from TblPrefs ORDER BY prefKey ASC")
        LiveData<List<TblPrefs>> getAllPrefs();

        @Query("SELECT * from TblPrefs ORDER BY prefKey ASC")
        List<TblPrefs> getAllPrefsList();

        @Query("SELECT * from TblPrefs WHERE prefKey=:pref ORDER BY prefKey ASC")
        TblPrefs getPref(String pref);

        @Update
        public void update(TblPrefs... prefs);
}
