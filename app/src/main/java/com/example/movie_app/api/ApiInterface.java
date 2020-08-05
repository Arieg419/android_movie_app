package com.example.movie_app.api;

import com.example.movie_app.model.PopularMoviesModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/3/movie/popular?")
    Call<PopularMoviesModel> doGetPopularMovieList(@Query("api_key") String api_key);

}
