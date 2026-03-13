package com.rohit.khalibook.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rohit.khalibook.data.local.entity.CommentEntity;

import java.util.List;

@Dao
public interface CommentDao {

    @Insert
    void insert(CommentEntity comment);

    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY timestamp ASC")
    List<CommentEntity> getCommentsForPost(int postId);

    @Delete
    void delete(CommentEntity comment);

    @Query("DELETE FROM comments WHERE postId = :postId")
    void deleteCommentsByPostId(int postId);
}
