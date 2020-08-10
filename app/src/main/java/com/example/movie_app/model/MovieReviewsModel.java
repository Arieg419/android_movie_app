package com.example.movie_app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieReviewsModel {
    @SerializedName("id")
    public String id;
    @SerializedName("author")
    public String author;
    @SerializedName("content")
    public String content;
    @SerializedName("url")
    public String url;
    @SerializedName("results")
    public List<MovieReviewsModel > reviews = null;
}
