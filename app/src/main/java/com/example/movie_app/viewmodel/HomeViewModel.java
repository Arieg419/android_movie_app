package com.example.movie_app.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.movie_app.database.Database;
import com.example.movie_app.database.FavoriteEntity;

import java.util.List;

public class HomeViewModel extends AndroidViewModel  {

    private Database mDb;
    private LiveData<List<FavoriteEntity>> mFavorites;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        mDb = Database.getInstance(this.getApplication());
        mFavorites = mDb.favoriteDao().loadAllFavorites();
    }

    public LiveData<List<FavoriteEntity>> getFavorites() {
        return mFavorites;
    }
}
