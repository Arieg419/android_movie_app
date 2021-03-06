package com.example.movie_app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieVideosModel {
    @SerializedName("id")
    public String id;
    @SerializedName("key")
    public String key;
    @SerializedName("name")
    public String name;
    @SerializedName("results")
    public List<MovieVideosModel> videos = null;
}
