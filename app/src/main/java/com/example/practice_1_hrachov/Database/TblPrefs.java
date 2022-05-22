package com.example.practice_1_hrachov.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "TblPrefs")
public class TblPrefs {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "prefKey")
    private String prefKey;

    @NonNull
    @ColumnInfo(name = "prefValue")
    private String prefValue;

    public TblPrefs(@NonNull String prefKey, @NonNull String prefValue) {
        this.prefKey = prefKey;
        this.prefValue = prefValue;
    }

    public String getPrefKey() {
        return prefKey;
    }

    public String getPrefValue() {
        return prefValue;
    }
}
