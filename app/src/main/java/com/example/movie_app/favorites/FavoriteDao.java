package com.example.movie_app.favorites;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM favorites ORDER BY updated_at")
    List<FavoriteEntity> loadAllFavorites();

    @Insert
    void insertFavorite(FavoriteEntity favoriteEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavorite(FavoriteEntity favoriteEntity);

    @Delete
    void deleteFavorite(FavoriteEntity favoriteEntity);
}
