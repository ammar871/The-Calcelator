package com.alltasksnew.thecalclatur.roomdatabase;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;


@Entity(tableName = "nods")
@TypeConverters({DateConverter.class})
public class ModelRoom implements Parcelable {


    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "input")
    private String input;
    @ColumnInfo(name = "time")
    private Long time;
    @ColumnInfo(name = "date")
    private Long date;
    @ColumnInfo(name = "times")
    private String times;
    @ColumnInfo(name = "dates")
    private String dates;
    @ColumnInfo(name = "audio")
    private String audio;
    @ColumnInfo(name = "image")
    private String image;
    @ColumnInfo(name = "color")
    private int color;

    public @Ignore ModelRoom(int id, String input, Long time, Long date, String times, String dates, String audio, String image, int color) {
        this.id = id;
        this.input = input;
        this.time = time;
        this.date = date;
        this.times = times;
        this.dates = dates;
        this.audio = audio;
        this.image = image;
        this.color = color;
    }

    public ModelRoom() {
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    protected ModelRoom(Parcel in) {
        id = in.readInt();
        input = in.readString();
        if (in.readByte() == 0) {
            time = null;
        } else {
            time = in.readLong();
        }
        if (in.readByte() == 0) {
            date = null;
        } else {
            date = in.readLong();
        }
        times = in.readString();
        dates = in.readString();
        audio = in.readString();
        image = in.readString();
        color = in.readInt();
    }

    public static final Creator<ModelRoom> CREATOR = new Creator<ModelRoom>() {
        @Override
        public ModelRoom createFromParcel(Parcel in) {
            return new ModelRoom(in);
        }

        @Override
        public ModelRoom[] newArray(int size) {
            return new ModelRoom[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(input);
        if (time == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(time);
        }
        if (date == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(date);
        }
        parcel.writeString(times);
        parcel.writeString(dates);
        parcel.writeString(audio);
        parcel.writeString(image);
        parcel.writeInt(color);
    }
}