package com.example.movie_app.favorites;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM favorites ORDER BY title")
    List<FavoriteEntity> loadAllFavorites();

    // TODO: Don't create duplicate records
    @Insert
    void insertFavorite(FavoriteEntity favoriteEntity);

    @Query("SELECT * FROM favorites WHERE  title = :title AND overview = :overview")
    @Nullable
    FavoriteEntity getFavoriteByIdentifiers(String title, String overview);

    // TODO: Delete by title?
    @Delete
    void deleteFavorite(FavoriteEntity favoriteEntity);
}
