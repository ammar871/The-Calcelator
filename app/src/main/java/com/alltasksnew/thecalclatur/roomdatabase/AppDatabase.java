package com.alltasksnew.thecalclatur.roomdatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ModelRoom.class
        , Modle_itemMunth.class,
        DaysModle.class,
ModleObjct.class}, version =8, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase mInstance;
    private static final String DATABASE_NAME = "wave-database";

    public abstract UserDao userDao();
    public abstract MounthDao mounthDao();
    public abstract DaysDao daysDao();
    public abstract ObejectDao obejectDao();

    public synchronized static AppDatabase getDatabaseInstance(Context context) {

        if (mInstance == null) {
            mInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return mInstance;
    }

    public static void destroyInstance() {
        mInstance = null;
    }

}

