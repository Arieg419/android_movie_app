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
import com.example.movie_app.constants.Constants;
import com.example.movie_app.database.FavoriteEntity;
import com.example.movie_app.model.PopularMoviesModel;
import com.example.movie_app.model.PopularMoviesResponseModel;
import com.example.movie_app.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieHomeActivity extends AppCompatActivity {

    private ApiInterface mApiInterface;
    private MovieHomeAdapter mMovieHomeAdapter;
    RecyclerView mRecyclerView;
    private Constants.HomeState mHomeState;
    private ArrayList<PopularMoviesModel> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // View refs
        mRecyclerView = findViewById(R.id.movies_home_recycler_view);

        // Adapter setup
        mMovies = new ArrayList<>();
        mMovieHomeAdapter = new MovieHomeAdapter(this, mMovies, mRecyclerView);
        mRecyclerView.setAdapter(mMovieHomeAdapter);

        if (savedInstanceState == null) {
            // Fetch data from network
            mApiInterface = ApiClient.getClient(this).create(ApiInterface.class);
            fetchMovieData(mApiInterface.doGetPopularMovieList());
            mHomeState = Constants.HomeState.POPULAR;
            return;
        }

       // Populate UI from Bundle
        setTitle(savedInstanceState.getString(Constants.SCREEN_TITLE));
        mMovies = savedInstanceState.getParcelableArrayList(Constants.MOVIES);
        mMovieHomeAdapter.clear();
        mMovieHomeAdapter.addAll(mMovies);
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
                sortByPopular();
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
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putParcelableArrayList(Constants.MOVIES, mMovies);
        state.putString(Constants.SCREEN_TITLE, getTitle().toString());
        state.putParcelable(Constants.STATE, mHomeState);
        state.putParcelable(Constants.RV_STATE, Objects.requireNonNull(mRecyclerView.getLayoutManager()).onSaveInstanceState());
    }

    private void loadFavoriteMovies() {
        HomeViewModel viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        viewModel.getFavorites().observe(this, favorites -> {
            ArrayList<PopularMoviesModel> updatedFavorites = new ArrayList<>();
            if (favorites == null) {
                mMovies = new ArrayList<>();
                mMovieHomeAdapter.clear();
                return;
            }

            for (FavoriteEntity f: favorites) {
                PopularMoviesModel res = new PopularMoviesModel();
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
            mHomeState = Constants.HomeState.FAVORITES;
        });
    }

    public void sortByFavorite() {
        mMovieHomeAdapter.clear();
        loadFavoriteMovies();
    }

    public void sortByRating() {
        mMovieHomeAdapter.clear();
        fetchMovieData(mApiInterface.doGetTopRatedMovieList());
        mHomeState = Constants.HomeState.TOP_RATED;
    }

    public void sortByPopular() {
        mMovieHomeAdapter.clear();
        fetchMovieData(mApiInterface.doGetPopularMovieList());
        mHomeState = Constants.HomeState.POPULAR;
    }

    private void fetchMovieData(Call<PopularMoviesResponseModel> call) {
        // Async request with callback invocation
        call.enqueue(new Callback<PopularMoviesResponseModel>() {
            @Override
            public void onResponse(Call<PopularMoviesResponseModel> call, Response<PopularMoviesResponseModel> response) {
                PopularMoviesResponseModel resource = response.body();
                if (resource == null) {
                    return;
                }
                mMovieHomeAdapter.addAll(resource.results);
            }

            @Override
            public void onFailure(Call<PopularMoviesResponseModel> call, Throwable t) {
                call.cancel();
            }
        });
    }
}