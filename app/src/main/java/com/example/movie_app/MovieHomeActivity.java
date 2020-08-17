package com.example.movie_app;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private ArrayList<PopularMoviesModel.Result> mMovies;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar init
        Toolbar mToolbar = this.findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Fetch data
        mApiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        fetchMovieData(mApiInterface.doGetPopularMovieList());
        loadFavoriteMovies();

        // Grid ref
        mRecyclerView = findViewById(R.id.movies_home_recycler_view);


    }

    public void setMovieGridAdapter(ArrayList<PopularMoviesModel.Result> movies) {
        mMovieHomeAdapter = new MovieHomeAdapter(movies);
        mRecyclerView.setAdapter(mMovieHomeAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void loadFavoriteMovies() {
        HomeViewModel viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        viewModel.getFavorites().observe(this, favorites -> {
            System.out.println("Omer -> observed db change");
            ArrayList<PopularMoviesModel.Result> updatedFavorites = new ArrayList<>();
            if (favorites == null) {
                mMovies = updatedFavorites;
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
            mMovies = updatedFavorites;
        });
    }

    public void sortByFavorite() {
        mMovieHomeAdapter.clear();
        setMovieGridAdapter(mMovies);
        mMovieHomeAdapter.notifyDataSetChanged();
    }

    public void sortByRating() {
        fetchMovieData(mApiInterface.doGetTopRatedMovieList());
        updateAdapter();
    }

    public void sortByTitle() {
        fetchMovieData(mApiInterface.doGetPopularMovieList());
        updateAdapter();
    }

    private void updateAdapter() {
        setMovieGridAdapter(mMovies);
        mMovieHomeAdapter.notifyDataSetChanged();
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
                mMovies = (ArrayList<PopularMoviesModel.Result>) resource.results;
                setMovieGridAdapter(mMovies);
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