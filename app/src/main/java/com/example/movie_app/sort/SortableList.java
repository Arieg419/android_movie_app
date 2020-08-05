package com.example.movie_app.sort;

import com.example.movie_app.fragments.MovieHomeFragment;
import com.example.movie_app.model.PopularMoviesModel;

import java.util.Comparator;

public class SortableList {

    public static final String SORT_BY_TITLE = "TITLE";
    public static final String SORT_BY_RATING = "RATING";

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

    public static void sort(String sortType, MovieHomeFragment movieFragment) {
        if (movieFragment != null && movieFragment.isVisible()) {
            if (sortType.contentEquals(SortableList.SORT_BY_RATING)) {
                movieFragment.sortByRating();
            } else if (sortType.contentEquals(SortableList.SORT_BY_TITLE)) {
                movieFragment.sortByTitle();
            }
        }
    }
}


