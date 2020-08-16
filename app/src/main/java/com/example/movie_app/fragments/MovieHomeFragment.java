package com.example.movie_app.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.movie_app.R;
import com.example.movie_app.api.ApiClient;
import com.example.movie_app.api.ApiInterface;
import com.example.movie_app.favorites.Database;
import com.example.movie_app.favorites.FavoriteEntity;
import com.example.movie_app.model.PopularMoviesModel;
import com.example.movie_app.recylcerview.MovieHomeAdapter;
import com.example.movie_app.sort.SortableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_SHORT;

public class MovieHomeFragment extends Fragment implements SortableList.SortableMovieAdapter, SwipeRefreshLayout.OnRefreshListener {

    public enum State {
        POPULAR, TOP_RATED, FAVORITES
    }

    private ApiInterface mApiInterface;
    private MovieHomeAdapter mMovieHomeAdapter;
    private ArrayList<PopularMoviesModel.Result> mPopularMovies;
    private ArrayList<PopularMoviesModel.Result> mFavoriteMovies;
    private RecyclerView mGridView;
    private Database mFavoritesDb;
    private State mState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mState = State.POPULAR;
        mApiInterface = ApiClient.getClient(Objects.requireNonNull(getContext())).create(ApiInterface.class);
        fetchMovieData(mApiInterface.doGetPopularMovieList());
        mFavoritesDb = Database.getInstance(getContext());
        loadFavoriteMovies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_view, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mGridView = Objects.requireNonNull(getActivity()).findViewById(R.id.movies_recycler_view);
    }

    @Override
    public void onRefresh() {
        Toast.makeText(getActivity(), R.string.refresh_text, LENGTH_SHORT).show();
    }

    public void setMovieGridAdapter(ArrayList<PopularMoviesModel.Result> movies) {
        mMovieHomeAdapter = new MovieHomeAdapter(movies);
        mGridView.setAdapter(mMovieHomeAdapter);
        mGridView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    private void loadFavoriteMovies() {
        LiveData<List<FavoriteEntity>> liveFavorites = mFavoritesDb.favoriteDao().loadAllFavorites();
        liveFavorites.observe(this, new Observer<List<FavoriteEntity>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteEntity> favorites) {
                System.out.println("Omer -> observed db change");
                ArrayList<PopularMoviesModel.Result> favoritesModel = new ArrayList<>();
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
                    favoritesModel.add(res);
                }
                mFavoriteMovies = favoritesModel;
            }
        });
    }

    public void sortByFavorite() {
        setMovieGridAdapter(mFavoriteMovies);
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
        setMovieGridAdapter(mPopularMovies);
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
                mPopularMovies = (ArrayList<PopularMoviesModel.Result>) resource.results;
                setMovieGridAdapter(mPopularMovies);
            }

            @Override
            public void onFailure(Call<PopularMoviesModel> call, Throwable t) {
                call.cancel();
            }
        });
    }
}