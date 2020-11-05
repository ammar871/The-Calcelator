package com.alltaskes.thecalclatur.roomdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;
@Dao
public interface DaysDao {
    @Query("SELECT * FROM days")
    List<DaysModle> getAll();

    @Insert(onConflict = REPLACE)
    void insertUser(DaysModle mUser);

    @Insert
    void insertAllUser(DaysModle... mUsersList);

    @Delete
    void delete(DaysModle mUser);

    @Update
    void updateUser(DaysModle mUser);

    @Query("SELECT * FROM days WHERE id = :uId")
    DaysModle getUserById(int uId);

    @Query("SELECT * FROM days WHERE id IN (:userIds)")
    List<DaysModle> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM days WHERE `key` LIKE :name LIMIT 1")
    boolean findByName(String name);

    @Query("DELETE FROM munths")
    void nukeTable();
}


