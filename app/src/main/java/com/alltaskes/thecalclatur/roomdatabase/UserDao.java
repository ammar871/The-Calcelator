package com.alltaskes.thecalclatur.roomdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {
    @Query("SELECT * FROM nods")
    List<ModelRoom> getAll();

    @Insert(onConflict = REPLACE)
    void insertUser(ModelRoom mUser);

    @Insert
    void insertAllUser(ModelRoom... mUsersList);

    @Delete
    void delete(ModelRoom mUser);

    @Update
    void updateUser(ModelRoom mUser);

    @Query("SELECT * FROM nods WHERE id = :uId")
    ModelRoom getUserById(int uId);

    @Query("SELECT * FROM nods WHERE id IN (:userIds)")
    List<ModelRoom> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM nods WHERE `input` LIKE :name LIMIT 1")
    boolean findByName(String name);

    @Query("DELETE FROM nods")
    void nukeTable();
}