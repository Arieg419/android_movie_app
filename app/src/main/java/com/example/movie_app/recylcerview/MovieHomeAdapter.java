package com.example.movie_app.recylcerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_app.MovieDetailActivity;
import com.example.movie_app.MovieHomeActivity;
import com.example.movie_app.R;
import com.example.movie_app.fragments.MovieDetailFragment;
import com.example.movie_app.model.PopularMoviesModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.movie_app.utils.Utils.getMovieDetailPosterPath;

public class MovieHomeAdapter extends RecyclerView.Adapter<MovieHomeAdapter.ViewHolder>{
    Context mContext;
    List<PopularMoviesModel.Result> mPopularMovies;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView movieTitle;
        public TextView movieRating;
        public ImageView moviePoster;

        public ViewHolder(View view) {
            super(view);
            itemView = view;
            movieTitle = (TextView) itemView.findViewById(R.id.textview_movie_name);
            movieRating = (TextView) itemView.findViewById(R.id.textview_movie_rating);
            moviePoster = (ImageView) itemView.findViewById(R.id.imageview_movie_poster);
        }
    }

    public MovieHomeAdapter(List<PopularMoviesModel.Result> popularMovies) {
        super();
        mPopularMovies = popularMovies;
    }

    @Override
    public int getItemCount() {
        return mPopularMovies.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        // Inflate the custom layout
        View movieView = inflater.inflate(R.layout.movie_poster_item, parent, false);

        // Return a new holder instance
        return new ViewHolder(movieView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get current movie
        PopularMoviesModel.Result movie = mPopularMovies.get(position);

        // Set movie title
        holder.movieTitle.setText(movie.title);

        // Set movie rating
        holder.movieRating.setText(Double.toString(movie.voteAverage));

        // Set movie poster
        String imgPath = getMovieDetailPosterPath(mContext, movie.posterPath, 400);
        Picasso.get().load(imgPath).into(holder.moviePoster);

        // Set movie item click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle detailBundle = new Bundle();
                detailBundle.putString(MovieDetailFragment.TITLE, movie.title);
                detailBundle.putString(MovieDetailFragment.OVERVIEW, movie.overview);
                detailBundle.putString(MovieDetailFragment.RELEASE_DATE, movie.releaseDate);
                detailBundle.putString(MovieDetailFragment.RATING, movie.voteAverage.toString());
                detailBundle.putString(MovieDetailFragment.POSTER_PATH, movie.posterPath);
                Intent movieDetailIntent = new Intent((MovieHomeActivity) mContext, MovieDetailActivity.class);
                movieDetailIntent.putExtras(detailBundle);
                ((MovieHomeActivity) mContext).startActivity(movieDetailIntent);
            }
        });
    }
}


