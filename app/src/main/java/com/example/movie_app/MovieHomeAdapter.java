package com.example.movie_app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movie_app.model.PopularMoviesModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieHomeAdapter extends ArrayAdapter<PopularMoviesModel.Result> {
    private static final String LOG_TAG = MovieHomeAdapter .class.getSimpleName();

    public MovieHomeAdapter(Activity context, ArrayList<PopularMoviesModel.Result> popularMovies) {
        super(context, 0, popularMovies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
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
        String imgPath = listItemView.getContext().getResources().getString(R.string.api_image_base_path) + currentMovie.posterPath.substring(1);
        Picasso.get().load(imgPath).into(moviePoster);

        return listItemView;
    }

}
