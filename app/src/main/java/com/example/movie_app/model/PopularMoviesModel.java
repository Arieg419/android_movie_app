package com.example.movie_app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PopularMoviesModel {
    @SerializedName("results")
    public List<Result> results = null;

    public static class Result implements Comparable<Result>{

        @Override
        public int compareTo(Result r) {
            return this.title.compareTo(r.title);
        }

        @SerializedName("poster_path")
        public String posterPath;
        @SerializedName("id")
        public Integer id;
        @SerializedName("title")
        public String title;
        @SerializedName("vote_average")
        public Double voteAverage;
        @SerializedName("overview")
        public String overview;
        @SerializedName("release_date")
        public String releaseDate;
    }
}
