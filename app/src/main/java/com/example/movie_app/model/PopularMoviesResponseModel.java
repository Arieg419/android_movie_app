package com.example.movie_app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PopularMoviesResponseModel {
    @SerializedName("results") public List<PopularMoviesModel> results = null;
}
