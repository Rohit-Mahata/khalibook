package com.rohit.khalibook.data.local.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "posts",
        foreignKeys = @ForeignKey(
                entity = UserEntity.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        )
)
public class PostEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userId;
    public String username;
    public String content;
    public long timestamp;
    
    public int likeCount = 0;
    public int commentCount = 0;
}
