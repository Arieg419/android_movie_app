package com.example.movie_app.adapters;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_app.R;
import com.example.movie_app.activities.MovieDetailActivity;
import com.example.movie_app.model.PopularMoviesModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.movie_app.constants.Constants.MOVIE_ID;
import static com.example.movie_app.constants.Constants.OVERVIEW;
import static com.example.movie_app.constants.Constants.POSTER_PATH;
import static com.example.movie_app.constants.Constants.RATING;
import static com.example.movie_app.constants.Constants.RELEASE_DATE;
import static com.example.movie_app.constants.Constants.TITLE;
import static com.example.movie_app.utils.Utils.getMovieDetailPosterPath;

public class MovieHomeAdapter extends RecyclerView.Adapter<MovieHomeAdapter.ViewHolder>{
    Context mContext;
    RecyclerView mRecyclerView;
    List<PopularMoviesModel> mPopularMovies;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView movieTitle;
        public TextView movieRating;
        public ImageView moviePoster;
        public int movieId;

        public ViewHolder(View view) {
            super(view);
            itemView = view;
            movieTitle = itemView.findViewById(R.id.textview_movie_name);
            movieRating = itemView.findViewById(R.id.textview_movie_rating);
            moviePoster = itemView.findViewById(R.id.imageview_movie_poster);
        }
    }

    public MovieHomeAdapter(Context context, List<PopularMoviesModel> popularMovies, RecyclerView rv) {
        super();
        mPopularMovies = popularMovies;
        mRecyclerView = rv;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
    }

    @Override
    public int getItemCount() {
        return mPopularMovies == null ? 0 : mPopularMovies.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View movieView = inflater.inflate(R.layout.movie_poster_item, parent, false);
        return new ViewHolder(movieView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PopularMoviesModel movie = mPopularMovies.get(position);
        holder.movieId = movie.id;
        holder.movieTitle.setText(movie.title);
        holder.movieRating.setText(Double.toString(movie.voteAverage));
        String imgPath = getMovieDetailPosterPath(mContext, movie.posterPath, 400);
        Picasso.get().load(imgPath).into(holder.moviePoster);

        holder.itemView.setOnClickListener(view -> {
            Bundle detailBundle = new Bundle();
            detailBundle.putString(TITLE, movie.title);
            detailBundle.putString(OVERVIEW, movie.overview);
            detailBundle.putString(RELEASE_DATE, movie.releaseDate);
            detailBundle.putString(RATING, movie.voteAverage.toString());
            detailBundle.putString(POSTER_PATH, movie.posterPath);
            detailBundle.putLong(MOVIE_ID, movie.id);
            Intent movieDetailIntent = new Intent(mContext, MovieDetailActivity.class);
            movieDetailIntent.putExtras(detailBundle);
            (mContext).startActivity(movieDetailIntent);
        });
    }

    public void addAll(List<PopularMoviesModel> movies) {
        mPopularMovies.addAll(movies);
        notifyDataSetChanged();
    }

    public void clear() {
        mPopularMovies.clear();
        notifyDataSetChanged();
    }
}


