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

import com.example.movie_app.MovieDetailActivity;
import com.example.movie_app.fragments.MovieDetailFragment;
import com.example.movie_app.MovieHomeActivity;
import com.example.movie_app.R;
import com.example.movie_app.model.PopularMoviesModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
        TextView movieTitle = (TextView) listItemView.findViewById(R.id.textview_movie_name);
        movieTitle.setText(currentMovie.title);

        // Set movie rating
        TextView movieRating = (TextView) listItemView.findViewById(R.id.textview_movie_rating);
        movieRating.setText(Double.toString(currentMovie.voteAverage));

        // Set movie poster
        ImageView moviePoster = (ImageView) listItemView.findViewById(R.id.imageview_movie_poster);
        String imgPath = getMovieDetailPosterPath(context, currentMovie.posterPath, 400);
        Picasso.get().load(imgPath).into(moviePoster);

        // Set movie item click listener
        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle detailBundle = new Bundle();
                detailBundle.putString(MovieDetailFragment.TITLE, currentMovie.title);
                detailBundle.putString(MovieDetailFragment.OVERVIEW, currentMovie.overview);
                detailBundle.putString(MovieDetailFragment.RELEASE_DATE, currentMovie.releaseDate);
                detailBundle.putString(MovieDetailFragment.RATING, currentMovie.voteAverage.toString());
                detailBundle.putString(MovieDetailFragment.POSTER_PATH, currentMovie.posterPath);
                Intent movieDetailIntent = new Intent((MovieHomeActivity) getContext(), MovieDetailActivity.class);
                movieDetailIntent.putExtras(detailBundle);
                ((MovieHomeActivity) getContext()).startActivity(movieDetailIntent);
            }
        });

        return listItemView;
    }


}
