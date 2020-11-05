package com.alltaskes.thecalclatur.roomdatabase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "munths")
@TypeConverters({DateConverter.class})
public class Modle_itemMunth {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "key")
    private String key;

    @ColumnInfo(name = "numberMunth")
    private String numberMunth;

    @ColumnInfo(name = "nameMunth")
    private String nameMunth;

    public @Ignore Modle_itemMunth(int id, String key, String numberMunth, String nameMunth) {
        this.id = id;
        this.key = key;
        this.numberMunth = numberMunth;
        this.nameMunth = nameMunth;
    }

    public Modle_itemMunth() {
    }

    public String getNumberMunth() {
        return numberMunth;
    }

    public void setNumberMunth(String numberMunth) {
        this.numberMunth = numberMunth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNameMunth() {
        return nameMunth;
    }

    public void setNameMunth(String nameMunth) {
        this.nameMunth = nameMunth;
    }
}
