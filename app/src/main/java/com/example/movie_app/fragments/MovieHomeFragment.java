package com.example.movie_app.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.movie_app.R;
import com.example.movie_app.api.ApiClient;
import com.example.movie_app.api.ApiInterface;
import com.example.movie_app.model.PopularMoviesModel;
import com.example.movie_app.recylcerview.MovieHomeAdapter;
import com.example.movie_app.sort.SortableList;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.*;

public class MovieHomeFragment extends Fragment implements SortableList.SortableMovieAdapter, SwipeRefreshLayout.OnRefreshListener {
    private ApiInterface mApiInterface;
    private MovieHomeAdapter mMovieHomeAdapter;
    private ArrayList<PopularMoviesModel.Result> mPopularMovies;
    private RecyclerView mGridView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiInterface = ApiClient.getClient(Objects.requireNonNull(getContext())).create(ApiInterface.class);
        fetchMovieData(mApiInterface.doGetPopularMovieList());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_view, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mGridView = (RecyclerView) Objects.requireNonNull(getActivity()).findViewById(R.id.movies_recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) Objects.requireNonNull(getActivity()).findViewById(R.id.movie_home_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        Toast.makeText(getActivity(), R.string.refresh_text, LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO: Handle internal state of popular / sorted
                // TODO: Make network request based on state
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    public void setMovieGridAdapter(ArrayList<PopularMoviesModel.Result> movies) {
        mMovieHomeAdapter = new MovieHomeAdapter(movies);
        mGridView.setAdapter(mMovieHomeAdapter);
        mGridView.setLayoutManager(new GridLayoutManager(getContext(), 2));
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
        System.out.println("Omer -> fetch movie data 0");
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