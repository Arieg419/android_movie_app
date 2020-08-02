package com.example.movie_app.sort;

import com.example.movie_app.model.PopularMoviesModel;

import java.util.Comparator;

public class SortableList {
    public interface SortableMovieAdapter {
        public void sortByRating();
        public void sortByTitle();
    }
    public static class MovieTitleSorter implements Comparator<PopularMoviesModel.Result> {
        public int compare(PopularMoviesModel.Result r1, PopularMoviesModel.Result r2) {
            return r1.title.compareTo(r2.title);
        }
    }

    public static class MovieRatingSorter implements Comparator<PopularMoviesModel.Result> {
        public int compare(PopularMoviesModel.Result r1, PopularMoviesModel.Result r2) {
            return r2.voteAverage.compareTo(r1.voteAverage);
        }
    }
}


