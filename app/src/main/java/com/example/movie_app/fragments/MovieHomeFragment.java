package com.example.movie_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.example.movie_app.R;
import com.example.movie_app.adapter.MovieHomeAdapter;
import com.example.movie_app.api.ApiClient;
import com.example.movie_app.api.ApiInterface;
import com.example.movie_app.model.PopularMoviesModel;
import com.example.movie_app.sort.SortableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieHomeFragment extends Fragment implements SortableList.SortableMovieAdapter {
    MovieHomeAdapter mMovieHomeAdapter;
    ArrayList<PopularMoviesModel.Result> mPopularMovies;
    GridView mGridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.grid_view, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mGridView = (GridView) Objects.requireNonNull(getActivity()).findViewById(R.id.movies_grid);
    }

    public void setMovieGridAdapter(ArrayList<PopularMoviesModel.Result> movies) {
        mMovieHomeAdapter = new MovieHomeAdapter(getActivity(), movies);
        mGridView.setAdapter(mMovieHomeAdapter );
    }

    public void sortByRating() {
        Collections.sort(mPopularMovies, new SortableList.MovieRatingSorter());
        updateAdapter();
    }

    public void sortByTitle() {
        Collections.sort(mPopularMovies, new SortableList.MovieTitleSorter());
        updateAdapter();
    }

    private void updateAdapter() {
        setMovieGridAdapter(mPopularMovies);
        mMovieHomeAdapter.notifyDataSetChanged();
        mGridView.invalidateViews();
        mGridView.refreshDrawableState();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApiInterface apiInterface = ApiClient.getClient(Objects.requireNonNull(getContext())).create(ApiInterface.class);
        Call<PopularMoviesModel> call = apiInterface.doGetPopularMovieList(getContext().getResources().getString(R.string.api_key));
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