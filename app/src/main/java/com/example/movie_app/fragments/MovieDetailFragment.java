package com.example.movie_app.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_app.R;
import com.example.movie_app.api.ApiClient;
import com.example.movie_app.api.ApiInterface;
import com.example.movie_app.favorites.Database;
import com.example.movie_app.favorites.FavoriteEntity;
import com.example.movie_app.model.MovieReviewsModel;
import com.example.movie_app.model.MovieVideosModel;
import com.example.movie_app.recylcerview.MovieReviewAdapter;
import com.example.movie_app.recylcerview.MovieTrailerAdapter;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.movie_app.utils.Utils.getMovieDetailPosterPath;

public class MovieDetailFragment extends Fragment {
    public static final String TITLE = "TITLE";
    public static final String OVERVIEW = "OVERVIEW";
    public static final String RELEASE_DATE = "RELEASE_DATE";
    public static final String RATING = "RATING";
    public static final String POSTER_PATH = "POSTER_PATH";
    public static final String MOVIE_ID = "MOVIE_ID";

    // API ref
    ApiInterface mApiInterface;

    // View refs
    ImageView mMiniPoster;
    TextView mMovieTitle;
    TextView mMovieReleaseDate;
    TextView mMovieRating;
    TextView mMovieOverview;
    CompoundButton mFavoriteBtn;

    // Data
    int mMovieId;
    String mMovieTitleString;
    String mMovieReleaseDateString;
    String mMovieRatingString;
    String mMovieOverviewString;
    String mPosterPathString;
    List<MovieVideosModel> mTrailers;
    List<MovieReviewsModel> mReviews;
    private Database mFavoritesDb;

    // Adapters
    MovieTrailerAdapter movieTrailerAdapter;
    MovieReviewAdapter movieReviewAdapter;

    // RVs
    RecyclerView mMovieTrailerRecyclerView;
    RecyclerView mMovieReviewRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get args
        Bundle b = this.getArguments();
        assert(b != null);
        Long movieId = b.getLong(MOVIE_ID);

        // Get http client
        mApiInterface = ApiClient.getClient(Objects.requireNonNull(getContext())).create(ApiInterface.class);

        // Fetch trailer data
        fetchTrailerData(mApiInterface.getTrailers(movieId));

        // Fetch review data
        fetchReviewData(mApiInterface.getReviews(movieId));

        // Get instance of favorite db
        mFavoritesDb = Database.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.movie_detail, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle b = this.getArguments();
        assert(b != null);

        // Extract bundle data
        mMovieId = (int) b.getLong(MOVIE_ID);
        mMovieTitleString = b.getString(TITLE);
        mMovieReleaseDateString = b.getString(RELEASE_DATE);
        mMovieRatingString = b.getString(RATING);
        mMovieOverviewString = b.getString(OVERVIEW);
        mPosterPathString = b.getString(POSTER_PATH);

        // Get views
        mMiniPoster = view.findViewById(R.id.movie_detail_mini_poster);
        mMovieTitle = view.findViewById(R.id.movie_detail_title);
        mMovieReleaseDate = view.findViewById(R.id.movie_detail_release_date);
        mMovieRating = view.findViewById(R.id.movie_detail_rating);
        mMovieOverview = view.findViewById(R.id.movie_detail_overview);
        mFavoriteBtn = view.findViewById(R.id.movie_detail_add_to_fav);
        mMovieTrailerRecyclerView = view.findViewById(R.id.movie_trailer_recycler_view);
        mMovieReviewRecyclerView = view.findViewById(R.id.movie_reviews_recycler_view);

        // Set mini poster
        Picasso.get()
                .load(getMovieDetailPosterPath(Objects.requireNonNull(getContext()), Objects.requireNonNull(mPosterPathString), 400) )
                .into(mMiniPoster);

        // Set title
        mMovieTitle.setText(mMovieTitleString);

        // Set release date
        String releaseDate = mMovieReleaseDateString;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            assert releaseDate != null;
            Date date = simpleDateFormat.parse(releaseDate);
            String day          = (String) DateFormat.format("dd",   date); // 20
            String month = (String) DateFormat.format("MMMM",  date); // Jun
            String year         = (String) DateFormat.format("yyyy", date); // 2013
            mMovieReleaseDateString = "Release: " + month + " " + day + ", " + year;
            mMovieReleaseDate.setText(mMovieReleaseDateString);
        } catch (ParseException e) {
            mMovieReleaseDate.setText(releaseDate);
        }

        // Set rating
        mMovieRating.setText("Rating: " + b.getString(RATING) + "/10");

        // Set overview
        mMovieOverview.setText(mMovieOverviewString);

        // Set favorite btn state
        @Nullable FavoriteEntity favoriteEntity = mFavoritesDb.favoriteDao().getFavoriteByIdentifiers(mMovieTitleString, mMovieOverviewString);
        if (favoriteEntity == null) {
            mFavoriteBtn.setChecked(false);
        } else {
            mFavoriteBtn.setChecked(true);
        }

        // Set favorite btn animation
        mFavoriteBtn.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            ScaleAnimation scaleAnimation = new ScaleAnimation(
                    0.7f,
                    1.0f,
                    0.7f,
                    1.0f,
                    Animation.RELATIVE_TO_SELF,
                    0.7f,
                    Animation.RELATIVE_TO_SELF,
                    0.7f
            );
            scaleAnimation.setDuration(500);
            BounceInterpolator bounceInterpolator = new BounceInterpolator();
            scaleAnimation.setInterpolator(bounceInterpolator);
            compoundButton.startAnimation(scaleAnimation);

            // Favorite btn click listener
            FavoriteEntity favorite = new FavoriteEntity(
                    mMovieId,
                    mMovieTitleString,
                    mPosterPathString,
                    mMovieRatingString,
                    mMovieOverviewString,
                    mMovieReleaseDateString);
            if (isChecked) {
                mFavoritesDb
                        .favoriteDao()
                        .insertFavorite(favorite);
                Toast.makeText(getActivity(), "Added to favorites!", Toast.LENGTH_SHORT).show();
            } else  {
                mFavoritesDb
                        .favoriteDao()
                        .deleteFavorite(favorite);
                Toast.makeText(getActivity(), "Removed from favorites", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchTrailerData(Call<MovieVideosModel> call) {
        // Async request with callback invocation
        call.enqueue(new Callback<MovieVideosModel>() {
            @Override
            public void onResponse(Call<MovieVideosModel> call, Response<MovieVideosModel> response) {
                MovieVideosModel result = response.body();
                if (result == null) {
                    return;
                }
                mTrailers = result.videos;
                movieTrailerAdapter = new MovieTrailerAdapter(mTrailers);
                mMovieTrailerRecyclerView.setAdapter(movieTrailerAdapter);
                mMovieTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
            }

            @Override
            public void onFailure(Call<MovieVideosModel> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void fetchReviewData(Call<MovieReviewsModel> call) {
        // Async request with callback invocation
        call.enqueue(new Callback<MovieReviewsModel>() {
            @Override
            public void onResponse(Call<MovieReviewsModel> call, Response<MovieReviewsModel> response) {
                MovieReviewsModel result = response.body();
                if (result == null) {
                    return;
                }
                mReviews = result.reviews;
                movieReviewAdapter = new MovieReviewAdapter(mReviews);
                mMovieReviewRecyclerView.setAdapter(movieReviewAdapter);
                mMovieReviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
            }

            @Override
            public void onFailure(Call<MovieReviewsModel > call, Throwable t) {
                call.cancel();
            }
        });
    }
}
