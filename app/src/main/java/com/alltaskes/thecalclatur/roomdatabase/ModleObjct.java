package com.alltaskes.thecalclatur.roomdatabase;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "objects")
@TypeConverters({DateConverter.class})
public class ModleObjct implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "nameObj")
    private String nameObj ;

    @ColumnInfo(name = "contentObj")
    private String contentObj;

    @ColumnInfo(name = "recordObj")
    private String recordObj;

    @ColumnInfo(name = "date")
    private String dare;

    public
    ModleObjct(int id, String nameObj, String contentObj, String recordObj) {
        this.id = id;
        this.nameObj = nameObj;
        this.contentObj = contentObj;
        this.recordObj = recordObj;
    }

    public @Ignore
    ModleObjct(int id, String nameObj, String contentObj, String recordObj, String dare) {
        this.id = id;
        this.nameObj = nameObj;
        this.contentObj = contentObj;
        this.recordObj = recordObj;
        this.dare = dare;
    }

    public ModleObjct() {
    }

    public String getDare() {
        return dare;
    }

    public void setDare(String dare) {
        this.dare = dare;
    }

    protected ModleObjct(Parcel in) {
        id = in.readInt();
        nameObj = in.readString();
        contentObj = in.readString();
        recordObj = in.readString();
        dare = in.readString();
    }

    public static final Creator<ModleObjct> CREATOR = new Creator<ModleObjct>() {
        @Override
        public ModleObjct createFromParcel(Parcel in) {
            return new ModleObjct(in);
        }

        @Override
        public ModleObjct[] newArray(int size) {
            return new ModleObjct[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameObj() {
        return nameObj;
    }

    public void setNameObj(String nameObj) {
        this.nameObj = nameObj;
    }

    public String getContentObj() {
        return contentObj;
    }

    public void setContentObj(String contentObj) {
        this.contentObj = contentObj;
    }

    public String getRecordObj() {
        return recordObj;
    }

    public void setRecordObj(String recordObj) {
        this.recordObj = recordObj;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(nameObj);
        parcel.writeString(contentObj);
        parcel.writeString(recordObj);
        parcel.writeString(dare);
    }
}
