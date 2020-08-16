package com.example.movie_app.sort;

import com.example.movie_app.fragments.MovieHomeFragment;

public class SortableList {

    public static final String SORT_BY_TITLE = "TITLE";
    public static final String SORT_BY_RATING = "RATING";
    public static final String SORT_BY_FAVORITE = "FAVORITE";

    public interface SortableMovieAdapter {
        public void sortByRating();
        public void sortByTitle();
        public void sortByFavorite();
    }

    public static void sort(String sortType, MovieHomeFragment movieFragment) {
        if (movieFragment != null && movieFragment.isVisible()) {
            if (sortType.contentEquals(SortableList.SORT_BY_RATING)) {
                movieFragment.sortByRating();
            } else if (sortType.contentEquals(SortableList.SORT_BY_TITLE)) {
                movieFragment.sortByTitle();
            } else if (sortType.contentEquals(SortableList.SORT_BY_FAVORITE)) {
                movieFragment.sortByFavorite();
            }
        }
    }
}


