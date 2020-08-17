package com.example.movie_app.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.movie_app.database.Database;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Database mDb;
    private final int mMovieId;

    public DetailViewModelFactory(Database db, int id) {
        mDb = db;
        mMovieId = id;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new DetailViewModel(mDb, mMovieId);
    }
}
