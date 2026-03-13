package com.rohit.khalibook.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.rohit.khalibook.data.local.entity.UserEntity;

@Dao
public interface UserDao {

    @Insert
    long insert(UserEntity user);

    @Query("SELECT * FROM users WHERE phone = :phone AND password = :password LIMIT 1")
    UserEntity login(String phone, String password);

    @Query("SELECT * FROM users WHERE phone = :phone LIMIT 1")
    UserEntity getByPhone(String phone);

    @Query("UPDATE users SET profileCompleted = 1 WHERE id = :userId")
    void markProfileCompleted(int userId);
}
