package com.alltaskes.thecalclatur.roomdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;
@Dao
public interface ObejectDao {
    @Query("SELECT * FROM objects")
    List<ModleObjct> getAll();

    @Insert(onConflict = REPLACE)
    void insertUser(ModleObjct mUser);

    @Insert
    void insertAllUser(ModleObjct... mUsersList);

    @Delete
    void delete(ModleObjct mUser);

    @Update
    void updateUser(ModleObjct mUser);

    @Query("SELECT * FROM objects WHERE id = :uId")
    ModleObjct getUserById(int uId);

    @Query("SELECT * FROM objects WHERE id IN (:userIds)")
    List<ModleObjct> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM objects WHERE `nameObj` LIKE :name LIMIT 1")
    boolean findByName(String name);

    @Query("DELETE FROM objects")
    void nukeTable();
}