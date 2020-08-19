package com.example.movie_app.utils;

import android.content.Context;

import com.example.movie_app.R;

public class Utils {
    public static String getMovieDetailPosterPath(Context context, String posterPath, int width) {
        return context.getResources().getString(R.string.api_image_base_path) + width + "/" + posterPath.substring(1);
    }

    public static String getYoutubeThumbnail(Context context, String videoId) {
        return context.getString(R.string.youtube_thumbnail_base_url) + videoId + "/0.jpg";
    }
}
