package com.rohit.khalibook.data.local.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.rohit.khalibook.data.local.dao.CommentDao;
import com.rohit.khalibook.data.local.dao.PostDao;
import com.rohit.khalibook.data.local.dao.UserDao;
import com.rohit.khalibook.data.local.dao.UserProfileDao;
import com.rohit.khalibook.data.local.entity.CommentEntity;
import com.rohit.khalibook.data.local.entity.PostEntity;
import com.rohit.khalibook.data.local.entity.UserEntity;
import com.rohit.khalibook.data.local.entity.UserProfileEntity;

@Database(
        entities = {
                UserEntity.class,
                UserProfileEntity.class,
                PostEntity.class,
                CommentEntity.class
        },
        version = 3,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract UserProfileDao userProfileDao();
    public abstract PostDao postDao();
    public abstract CommentDao commentDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "khalibook_db"
            )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build();
        }
        return INSTANCE;
    }
}
