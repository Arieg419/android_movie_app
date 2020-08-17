package com.example.movie_app.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.movie_app.database.FavoriteEntity;
import com.example.movie_app.database.Repository;

import java.util.List;

public class DetailViewModel extends AndroidViewModel {
    private Repository repo;

    public DetailViewModel (@NonNull Application application) {
        super(application);
        repo = new Repository(application);
    }

    public LiveData<List<FavoriteEntity>> loadFavorites() {
        return repo.loadFavorites();}

    public void addFavorite(FavoriteEntity fav) {
        repo.addFavorite(fav);
    }

    public void removeFavorite(FavoriteEntity fav) {
        repo.deleteFavoriteMovie(fav);
    }

    public LiveData<FavoriteEntity> getFavoriteById(final int id) {
        return repo.getFavoriteById(id);
    }
}
