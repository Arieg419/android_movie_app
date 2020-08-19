package com.example.movie_app.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@androidx.room.Database(entities = {FavoriteEntity.class}, version = 9, exportSchema = false)
@TypeConverters({DateConvertor.class})
public abstract class Database extends RoomDatabase {

    private static final String LOG_TAG = Database.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favorites";
    private static Database sInstance;

    public static Database getInstance(Context context) {
       if (sInstance == null) {
           synchronized (LOCK) {
               Log.d(LOG_TAG, "Creating new db instance");
               sInstance = Room.databaseBuilder(context.getApplicationContext(), Database.class, Database.DATABASE_NAME)
                       .fallbackToDestructiveMigration()
                       .build();
           }
       }
        Log.d(LOG_TAG, "Getting the db instance");
       return sInstance;
    }

    public abstract FavoriteDao favoriteDao();
}
