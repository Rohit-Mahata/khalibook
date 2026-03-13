package com.rohit.khalibook.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_profiles")
public class UserProfileEntity {

    @PrimaryKey
    public int userId;

    public String firstName;
    public String lastName;
    public String address;
    public String emoji;
}
