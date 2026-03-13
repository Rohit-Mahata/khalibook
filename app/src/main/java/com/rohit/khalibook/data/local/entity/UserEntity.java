package com.rohit.khalibook.data.local.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "users",
        indices = {@Index(value = {"phone"}, unique = true)}
)
public class UserEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String username;
    public String phone;
    public String password;

    public int profileCompleted; // 0 = no, 1 = yes
}
