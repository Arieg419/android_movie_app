package com.example.movie_app.listview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movie_app.activities.MovieDetailActivity;
import com.example.movie_app.R;
import com.example.movie_app.model.PopularMoviesModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.movie_app.constants.Constants.OVERVIEW;
import static com.example.movie_app.constants.Constants.POSTER_PATH;
import static com.example.movie_app.constants.Constants.RATING;
import static com.example.movie_app.constants.Constants.RELEASE_DATE;
import static com.example.movie_app.constants.Constants.TITLE;
import static com.example.movie_app.utils.Utils.getMovieDetailPosterPath;

public class MovieHomeAdapter extends ArrayAdapter<PopularMoviesModel.Result> {

    public MovieHomeAdapter(Activity context, ArrayList<PopularMoviesModel.Result> popularMovies) {
        super(context, 0, popularMovies);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        Context context = getContext();
        if(listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(
                    R.layout.movie_poster_item, parent, false);
        }

        // Get current movie
        PopularMoviesModel.Result currentMovie = getItem(position);
        assert(currentMovie != null);

        // Set movie title
        TextView movieTitle = listItemView.findViewById(R.id.textview_movie_name);
        movieTitle.setText(currentMovie.title);

        // Set movie rating
        TextView movieRating = listItemView.findViewById(R.id.textview_movie_rating);
        movieRating.setText(Double.toString(currentMovie.voteAverage));

        // Set movie poster
        ImageView moviePoster = listItemView.findViewById(R.id.imageview_movie_poster);
        String imgPath = getMovieDetailPosterPath(context, currentMovie.posterPath, 400);
        Picasso.get().load(imgPath).into(moviePoster);

        // Set movie item click listener
        listItemView.setOnClickListener(view -> {
            Bundle detailBundle = new Bundle();
            detailBundle.putString(TITLE, currentMovie.title);
            detailBundle.putString(OVERVIEW, currentMovie.overview);
            detailBundle.putString(RELEASE_DATE, currentMovie.releaseDate);
            detailBundle.putString(RATING, currentMovie.voteAverage.toString());
            detailBundle.putString(POSTER_PATH, currentMovie.posterPath);
            Intent movieDetailIntent = new Intent(getContext(), MovieDetailActivity.class);
            movieDetailIntent.putExtras(detailBundle);
            (getContext()).startActivity(movieDetailIntent);
        });

        return listItemView;
    }


}
