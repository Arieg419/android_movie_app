package com.example.movie_app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.movie_app.database.Database;
import com.example.movie_app.database.FavoriteEntity;

import java.util.List;

public class DetailViewModel extends ViewModel {
    private LiveData<FavoriteEntity> mMovie;
    private Database mDb;

    public DetailViewModel (Database db, int id) {
        mDb = db;
        mMovie = db.favoriteDao().getFavoriteById(id);
    }

    public LiveData<List<FavoriteEntity>> loadFavorites() { return mDb.favoriteDao().loadAllFavorites();}

    public void addFavorite(FavoriteEntity fav) {
        mDb.favoriteDao().insertFavorite(fav);
    }

    public void removeFavorite(FavoriteEntity fav) {
        mDb.favoriteDao().deleteFavorite(fav);
    }

    public LiveData<FavoriteEntity> getFavorite() {
        return mMovie;
    }
}
