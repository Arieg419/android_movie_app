package com.example.movie_app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM favorites ORDER BY title")
    LiveData<List<FavoriteEntity>> loadAllFavorites();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertFavorite(FavoriteEntity favoriteEntity);

    @Query("SELECT * FROM favorites WHERE id = :id")
    LiveData<FavoriteEntity>getFavoriteById(int id);

    @Delete
    void deleteFavorite(FavoriteEntity favoriteEntity);
}
