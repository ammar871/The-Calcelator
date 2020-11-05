package com.alltaskes.thecalclatur.roomdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;
@Dao
public interface MounthDao {
    @Query("SELECT * FROM munths")
    List<Modle_itemMunth> getAll();

    @Insert(onConflict = REPLACE)
    void insertUser(Modle_itemMunth mUser);

    @Insert
    void insertAllUser(Modle_itemMunth... mUsersList);

    @Delete
    void delete(Modle_itemMunth mUser);

    @Update
    void updateUser(Modle_itemMunth mUser);

    @Query("SELECT * FROM munths WHERE id = :uId")
    Modle_itemMunth getUserById(int uId);

    @Query("SELECT * FROM munths WHERE id IN (:userIds)")
    List<Modle_itemMunth> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM munths WHERE `key` LIKE :name LIMIT 1")
    boolean findByName(String name);

    @Query("DELETE FROM munths")
    void nukeTable();
}