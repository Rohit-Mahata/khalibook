package com.rohit.khalibook.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.rohit.khalibook.data.local.entity.UserProfileEntity;

@Dao
public interface UserProfileDao {

    @Insert
    void insert(UserProfileEntity profile);

    @Query("SELECT * FROM user_profiles WHERE userId = :userId LIMIT 1")
    UserProfileEntity getByUserId(int userId);
}
