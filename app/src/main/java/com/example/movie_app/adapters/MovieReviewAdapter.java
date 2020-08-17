package com.example.movie_app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_app.R;
import com.example.movie_app.api.ApiInterface;
import com.example.movie_app.model.MovieReviewsModel;

import java.util.List;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ViewHolder>{
    Context mContext;
    ApiInterface mApiInterface;
    List<MovieReviewsModel> mReviews;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView movieReview;
        public TextView movieReviewAuthor;
        public TextView movieReviewUrl;

        public ViewHolder(View view) {
            super(view);
            itemView = view;
            movieReview = (TextView) itemView.findViewById(R.id.review_content);
            movieReviewAuthor = (TextView) itemView.findViewById(R.id.review_critic);
            movieReviewUrl = (TextView) itemView.findViewById(R.id.review_url);
        }
    }

    public MovieReviewAdapter(List<MovieReviewsModel> movieReviews) {
        super();
        mReviews = movieReviews;
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        // Inflate the custom layout
        View movieView = inflater.inflate(R.layout.movie_detail_review_item, parent, false);

        // Return a new holder instance
        return new ViewHolder(movieView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get current movie
        MovieReviewsModel review = mReviews.get(position);

        // Set review content
        holder.movieReview.setText(review.content);

        // Set review author
        holder.movieReviewAuthor.setText("Author: " + review.author);

        // Set review link
        holder.movieReviewUrl.setText(review.url);

        // Set movie item click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Open link in youtube if exists or default browser
                System.out.println("Omer -> open review now!");
            }
        });
    }
}


