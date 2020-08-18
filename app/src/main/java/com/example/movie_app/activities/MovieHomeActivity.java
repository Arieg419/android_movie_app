package com.example.movie_app.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_app.R;
import com.example.movie_app.adapters.MovieHomeAdapter;
import com.example.movie_app.api.ApiClient;
import com.example.movie_app.api.ApiInterface;
import com.example.movie_app.database.FavoriteEntity;
import com.example.movie_app.model.PopularMoviesModel;
import com.example.movie_app.viewmodel.HomeViewModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieHomeActivity extends AppCompatActivity {

    private ApiInterface mApiInterface;
    private MovieHomeAdapter mMovieHomeAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // View refs
        mRecyclerView = findViewById(R.id.movies_home_recycler_view);

        // Fetch data
        ArrayList<PopularMoviesModel.Result> movies = new ArrayList<>();
        mMovieHomeAdapter = new MovieHomeAdapter(this, movies, mRecyclerView);
        mRecyclerView.setAdapter(mMovieHomeAdapter);

        mApiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        fetchMovieData(mApiInterface.doGetPopularMovieList());
    }

    private void loadFavoriteMovies() {
        HomeViewModel viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        viewModel.getFavorites().observe(this, favorites -> {
            ArrayList<PopularMoviesModel.Result> updatedFavorites = new ArrayList<>();
            if (favorites == null) {
                mMovieHomeAdapter.clear();
                return;
            }

            for (FavoriteEntity f: favorites) {
                PopularMoviesModel.Result res = new PopularMoviesModel.Result();
                String voteAvg = f.getVoteAverage();
                double movieRatingAverage = voteAvg == null || voteAvg.isEmpty()
                        ? 0.0
                        : Double.parseDouble(voteAvg);
                res.id = f.getId();
                res.title = f.getTitle();
                res.voteAverage = movieRatingAverage;
                res.overview = f.getOverview();
                res.posterPath = f.getPosterPath();
                res.releaseDate = f.getReleaseDate();
                updatedFavorites.add(res);
            }
            mMovieHomeAdapter.addAll(updatedFavorites);
        });
    }

    public void sortByFavorite() {
        mMovieHomeAdapter.clear();
        loadFavoriteMovies();
    }

    public void sortByRating() {
        mMovieHomeAdapter.clear();
        fetchMovieData(mApiInterface.doGetTopRatedMovieList());
    }

    public void sortByTitle() {
        mMovieHomeAdapter.clear();
        fetchMovieData(mApiInterface.doGetPopularMovieList());
    }

    private void fetchMovieData(Call<PopularMoviesModel> call) {
        // Async request with callback invocation
        call.enqueue(new Callback<PopularMoviesModel>() {
            @Override
            public void onResponse(Call<PopularMoviesModel> call, Response<PopularMoviesModel> response) {
                PopularMoviesModel resource = response.body();
                if (resource == null) {
                    return;
                }
                System.out.println("Omer moves is " +resource.results.size());
                mMovieHomeAdapter.addAll(resource.results);
            }

            @Override
            public void onFailure(Call<PopularMoviesModel> call, Throwable t) {
                call.cancel();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.popular_sort:
                sortByTitle();
                setTitle(R.string.app_name);
                break;
            case R.id.rating_sort:
                sortByRating();
                setTitle(R.string.top_rated_movies);
                break;
            case R.id.favorite_sort:
                sortByFavorite();
                setTitle(R.string.favorite_movies);
                break;
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}