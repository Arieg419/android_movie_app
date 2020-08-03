package com.example.movie_app.utils;

import android.content.Context;

import com.example.movie_app.R;
import com.example.movie_app.model.PopularMoviesModel;

public class Utils {
    public static String getMovieDetailPosterPath(Context context, String posterPath, int width) {
        return context.getResources().getString(R.string.api_image_base_path) + width + "/" + posterPath.substring(1);
    }
}
