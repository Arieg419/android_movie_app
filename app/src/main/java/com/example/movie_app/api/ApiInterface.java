package com.example.movie_app.api;

import com.example.movie_app.model.PopularMoviesModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("/3/movie/popular?")
    Call<PopularMoviesModel> doGetPopularMovieList();

    @GET("/3/movie/top_rated?")
    Call<PopularMoviesModel> doGetTopRatedMovieList();
}
