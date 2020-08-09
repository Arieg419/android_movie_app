package com.example.movie_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class MovieHomeFragment extends Fragment implements SortableList.SortableMovieAdapter {
    ApiInterface mApiInterface;
    MovieHomeAdapter mMovieHomeAdapter;
    ArrayList<PopularMoviesModel.Result> mPopularMovies;
    RecyclerView mGridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_view, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mGridView = (RecyclerView) Objects.requireNonNull(getActivity()).findViewById(R.id.movies_recycler_view);
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiInterface = ApiClient.getClient(Objects.requireNonNull(getContext())).create(ApiInterface.class);
        System.out.println("Omer -> movieHome on create");
        fetchMovieData(mApiInterface.doGetPopularMovieList());
        System.out.println("Omer -> movieHome on create after");
    }

    private void fetchMovieData(Call<PopularMoviesModel> call) {
        System.out.println("Omer -> fetch movie data 0");
        // Async request with callback invocation
        call.enqueue(new Callback<PopularMoviesModel>() {
            @Override
            public void onResponse(Call<PopularMoviesModel> call, Response<PopularMoviesModel> response) {
                System.out.println("Omer -> processing request!");
                PopularMoviesModel resource = response.body();
                if (resource == null) {
                    System.out.println("Omer -> resp on movie home frag is null");
                    return;
                }
                System.out.println("Omer -> resp on movie home frag is not null");
                mPopularMovies = (ArrayList<PopularMoviesModel.Result>) resource.results;
                setMovieGridAdapter(mPopularMovies);
            }

            @Override
            public void onFailure(Call<PopularMoviesModel> call, Throwable t) {
                System.out.println("Omer -> onFailuerMovieHome api " + t.toString());
                call.cancel();
            }
        });
    }
}