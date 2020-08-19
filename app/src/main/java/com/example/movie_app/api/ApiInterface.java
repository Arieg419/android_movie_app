package com.example.movie_app.api;

import com.example.movie_app.model.MovieReviewsModel;
import com.example.movie_app.model.MovieVideosModel;
import com.example.movie_app.model.PopularMoviesResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("/3/movie/popular?")
    Call<PopularMoviesResponseModel> doGetPopularMovieList();

    @GET("/3/movie/top_rated?")
    Call<PopularMoviesResponseModel> doGetTopRatedMovieList();

    @GET("/3/movie/{id}/videos")
    Call<MovieVideosModel> getTrailers(@Path("id") Long id);

    @GET("/3/movie/{id}/reviews")
    Call<MovieReviewsModel> getReviews(@Path("id") Long id);
}
