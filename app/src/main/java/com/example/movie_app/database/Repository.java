package com.example.movie_app.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.movie_app.executors.AppExecutors;

import java.util.List;

public class Repository {
    private FavoriteDao mDao;
    private AppExecutors mAppExecutors;
    private LiveData<List<FavoriteEntity>> mMovies;

    public Repository (Application application) {
        mDao = Database.getInstance(application).favoriteDao();
        mMovies = mDao.loadAllFavorites();
        mAppExecutors = AppExecutors.getExecutorInstance();
    }

    public LiveData<List<FavoriteEntity>> loadFavorites() {
        return mMovies;
    }

    public LiveData<FavoriteEntity> getFavoriteById(final int id){
        return mDao.getFavoriteById(id);
    }

    public void addFavorite(FavoriteEntity favorite) {
        mAppExecutors.getDiskIO().execute(() -> mDao.insertFavorite(favorite));
    }

    public void deleteFavoriteMovie(FavoriteEntity favorite) {
        mAppExecutors.getDiskIO().execute(() -> mDao.deleteFavorite(favorite));
    }

}
