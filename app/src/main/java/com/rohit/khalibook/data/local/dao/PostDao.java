package com.rohit.khalibook.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rohit.khalibook.data.local.entity.PostEntity;

import java.util.List;

@Dao
public interface PostDao {

    @Insert
    void insert(PostEntity post);

    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    List<PostEntity> getAllPosts();

    @Query("SELECT * FROM posts WHERE userId = :userId ORDER BY timestamp DESC")
    List<PostEntity> getPostsByUser(int userId);

    @Query("SELECT * FROM posts WHERE content LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    List<PostEntity> searchPosts(String query);

    @Delete
    void delete(PostEntity post);

    @Query("DELETE FROM posts WHERE id = :postId")
    void deleteById(int postId);

    @Query("UPDATE posts SET likeCount = likeCount + 1 WHERE id = :postId")
    void incrementLikeCount(int postId);

    @Query("UPDATE posts SET commentCount = commentCount + 1 WHERE id = :postId")
    void incrementCommentCount(int postId);
}
