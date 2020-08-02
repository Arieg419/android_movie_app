package com.example.movie_app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PopularMoviesModel {
    @SerializedName("page")
    public Integer page;
    @SerializedName("total_results")
    public Integer totalResults;
    @SerializedName("total_pages")
    public Integer totalPages;
    @SerializedName("results")
    public List<Result> results = null;

    public static class Result implements Comparable<Result>{

        @Override
        public int compareTo(Result r) {
            return this.title.compareTo(r.title);
        }

        @SerializedName("popularity")
        public Float popularity;
        @SerializedName("vote_count")
        public Integer voteCount;
        @SerializedName("video")
        public boolean video;
        @SerializedName("poster_path")
        public String posterPath;
        @SerializedName("id")
        public Integer id;
        @SerializedName("adult")
        public boolean adult;
        @SerializedName("backdrop_path")
        public String backdropPath;
        @SerializedName("original_language")
        public String originalLanguage;
        @SerializedName("original_title")
        public String originalTitle;
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
