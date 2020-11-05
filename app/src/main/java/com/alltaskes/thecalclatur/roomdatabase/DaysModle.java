package com.alltaskes.thecalclatur.roomdatabase;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "days")
@TypeConverters({DateConverter.class})
public class DaysModle {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "nubDay")
    private  String nubDay;

    @ColumnInfo(name = "kindOutlay")
    private int kindOutlay;

    @ColumnInfo(name = "key")
    private String key;



    @ColumnInfo(name = "outalymony")
    private double outalymony;


    public DaysModle(int id, String nubDay, int kindOutlay, String key,  double outaly) {
        this.id = id;
        this.nubDay = nubDay;
        this.kindOutlay = kindOutlay;
        this.key = key;

        this.outalymony = outaly;
    }

    public DaysModle() {
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNubDay() {
        return nubDay;
    }

    public void setNubDay(String nubDay) {
        this.nubDay = nubDay;
    }

    public int getKindOutlay() {
        return kindOutlay;
    }

    public void setKindOutlay(int kindOutlay) {
        this.kindOutlay = kindOutlay;
    }



    public double getOutalymony() {
        return outalymony;
    }

    public void setOutalymony(double outalymony) {
        this.outalymony = outalymony;
    }
}
