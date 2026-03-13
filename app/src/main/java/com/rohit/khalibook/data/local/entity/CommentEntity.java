package com.rohit.khalibook.data.local.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "comments",
        foreignKeys = {
                @ForeignKey(
                        entity = PostEntity.class,
                        parentColumns = "id",
                        childColumns = "postId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class CommentEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int postId;
    public int userId;
    public String username;
    public String content;
    public long timestamp;
}
