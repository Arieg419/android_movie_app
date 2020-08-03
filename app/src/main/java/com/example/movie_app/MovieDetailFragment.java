package com.example.movie_app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.util.Objects;

import static com.example.movie_app.utils.Utils.getMovieDetailPosterPath;

public class MovieDetailFragment extends Fragment {
    public static final String TITLE = "TITLE";
    public static final String OVERVIEW = "OVERVIEW";
    public static final String RELEASE_DATE = "RELEASE_DATE";
    public static final String RATING = "RATING";
    public static final String POSTER_PATH = "POSTER_PATH";

    ImageView mMoviePoster;
    ImageView mMiniPoster;
    TextView mMovieTitle;
    TextView mMovieReleaseDate;
    TextView mMovieRating;
    TextView mMovieOverview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.movie_detail, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle b = getArguments();
        assert(b != null);

        // Get views
        mMoviePoster = view.findViewById(R.id.movie_detail_poster);
        mMiniPoster = view.findViewById(R.id.movie_detail_mini_poster);
        mMovieTitle = view.findViewById(R.id.movie_detail_title);
        mMovieReleaseDate = view.findViewById(R.id.movie_detail_release_date);
        mMovieRating = view.findViewById(R.id.movie_detail_rating);
        mMovieOverview = view.findViewById(R.id.movie_detail_overview);

        // Set movie poster
        Picasso.get()
                .load(getMovieDetailPosterPath(Objects.requireNonNull(getContext()), Objects.requireNonNull(b.getString(POSTER_PATH)), 500))
                .fit()
                .centerCrop()
                .into(mMoviePoster);

        // Set mini poster
        Picasso.get()
                .load(getMovieDetailPosterPath(Objects.requireNonNull(getContext()), Objects.requireNonNull(b.getString(POSTER_PATH)), 200))
                .fit()
                .centerCrop()
                .into(mMiniPoster);

        // Set title
        mMovieTitle.setText(b.getString(TITLE));

        // Set release date
        mMovieReleaseDate.setText(b.getString(RELEASE_DATE));

        // Set rating
        mMovieRating.setText(b.getString(RATING) + "/10");

        // Set overview
        mMovieOverview.setText(b.getString(OVERVIEW));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
