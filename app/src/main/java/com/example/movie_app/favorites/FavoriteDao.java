package com.example.movie_app.favorites;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM favorites ORDER BY title")
    LiveData<List<FavoriteEntity>> loadAllFavorites();

    @Insert
    void insertFavorite(FavoriteEntity favoriteEntity);

    @Query("SELECT * FROM favorites WHERE  title = :title AND overview = :overview")
    @Nullable
    FavoriteEntity getFavoriteByIdentifiers(String title, String overview);

    @Delete
    void deleteFavorite(FavoriteEntity favoriteEntity);
}
