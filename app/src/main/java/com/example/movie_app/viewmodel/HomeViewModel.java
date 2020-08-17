package com.example.movie_app.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.movie_app.database.FavoriteEntity;
import com.example.movie_app.database.Repository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel  {

    private Repository repo;
    private LiveData<List<FavoriteEntity>> mFavorites;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repo = new Repository(application);
        System.out.println("Omer -> retrieving favorites via homeviewmodel");
        mFavorites = (repo).loadFavorites();
    }

    public LiveData<List<FavoriteEntity>> getFavorites() {
        return mFavorites;
    }

    public @Nullable
    LiveData<FavoriteEntity> getFavoriteById(int id) {
        return repo.getFavoriteById(id);
    }

    public void addFavorite(FavoriteEntity fav) {
        repo.addFavorite(fav);
    }

    public void removeFavorite(FavoriteEntity fav) {
        repo.deleteFavoriteMovie(fav);
    }

}
