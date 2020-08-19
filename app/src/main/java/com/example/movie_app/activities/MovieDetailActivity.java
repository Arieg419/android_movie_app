package com.example.movie_app.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_app.R;
import com.example.movie_app.adapters.MovieReviewAdapter;
import com.example.movie_app.adapters.MovieTrailerAdapter;
import com.example.movie_app.api.ApiClient;
import com.example.movie_app.api.ApiInterface;
import com.example.movie_app.database.Database;
import com.example.movie_app.database.FavoriteEntity;
import com.example.movie_app.executors.AppExecutors;
import com.example.movie_app.model.MovieReviewsModel;
import com.example.movie_app.model.MovieVideosModel;
import com.example.movie_app.viewmodel.DetailViewModel;
import com.example.movie_app.viewmodel.DetailViewModelFactory;
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

import static com.example.movie_app.constants.Constants.MOVIE_ID;
import static com.example.movie_app.constants.Constants.OVERVIEW;
import static com.example.movie_app.constants.Constants.POSTER_PATH;
import static com.example.movie_app.constants.Constants.RATING;
import static com.example.movie_app.constants.Constants.RELEASE_DATE;
import static com.example.movie_app.constants.Constants.TITLE;
import static com.example.movie_app.utils.Utils.getMovieDetailPosterPath;

public class MovieDetailActivity extends AppCompatActivity {

    // Context
    Context mContext;

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

    // Adapters
    MovieTrailerAdapter movieTrailerAdapter;
    MovieReviewAdapter movieReviewAdapter;

    // RVs
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView mMovieTrailerRecyclerView;
    RecyclerView mMovieReviewRecyclerView;

    private Database mDb;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_detail);
        mContext = this;
        mDb = Database.getInstance(getApplicationContext());

        // Get views
        mMiniPoster = findViewById(R.id.movie_detail_mini_poster);
        mMovieTitle = findViewById(R.id.movie_detail_title);
        mMovieReleaseDate = findViewById(R.id.movie_detail_release_date);
        mMovieRating = findViewById(R.id.movie_detail_rating);
        mMovieOverview = findViewById(R.id.movie_detail_overview);
        mFavoriteBtn = findViewById(R.id.movie_detail_add_to_fav);
        mLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mMovieTrailerRecyclerView = findViewById(R.id.movie_trailer_recycler_view);
        mMovieTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        mMovieTrailerRecyclerView.setAdapter(new MovieTrailerAdapter(null));
        mMovieReviewRecyclerView = findViewById(R.id.movie_reviews_recycler_view);
        mMovieReviewRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        mMovieReviewRecyclerView.setAdapter(new MovieReviewAdapter(null));

        // Extra intent data
        Bundle b = getIntent().getExtras();
        assert b != null;
        mMovieId = (int) b.getLong(MOVIE_ID);
        mMovieTitleString = b.getString(TITLE);
        mMovieReleaseDateString = b.getString(RELEASE_DATE);
        mMovieRatingString = b.getString(RATING);
        mMovieOverviewString = b.getString(OVERVIEW);
        mPosterPathString = b.getString(POSTER_PATH);
        Long movieId = b.getLong(MOVIE_ID);

        // Actionbar setup
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle(b.getString(TITLE));

        // Get http client
        mApiInterface = ApiClient.getClient(this).create(ApiInterface.class);

        // Fetch trailer data
        fetchTrailerData(mApiInterface.getTrailers(movieId));

        // Fetch review data
        fetchReviewData(mApiInterface.getReviews(movieId));

        // Network data
        setDetailUI();

        // Listen for fav changes
        initFavoriteBtn();

        // Favorite btn setup
        mFavoriteBtn.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            compoundButton.startAnimation(getScaleAnimation());

            // Favorite btn click listener
            FavoriteEntity favorite = new FavoriteEntity(
                    mMovieId,
                    mMovieTitleString,
                    mPosterPathString,
                    mMovieRatingString,
                    mMovieOverviewString,
                    mMovieReleaseDateString);
            if (isChecked) {
                mFavoriteBtn.setChecked(true);
                AppExecutors.getExecutorInstance().getDiskIO().execute(() -> runOnUiThread(() -> mDb.favoriteDao().insertFavorite(favorite)));
            } else  {
                mFavoriteBtn.setChecked(false);
                AppExecutors.getExecutorInstance().getDiskIO().execute(() -> runOnUiThread(() -> mDb.favoriteDao().deleteFavorite(favorite)));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
                new Handler(Looper.getMainLooper()).post(() -> {
                    movieTrailerAdapter = new MovieTrailerAdapter(mTrailers);
                    mMovieTrailerRecyclerView.setAdapter(movieTrailerAdapter);
                });
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
                new Handler(Looper.getMainLooper()).post(() -> {
                    movieReviewAdapter = new MovieReviewAdapter(mReviews);
                    mMovieReviewRecyclerView.setAdapter(movieReviewAdapter);
                });
            }

            @Override
            public void onFailure(Call<MovieReviewsModel > call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void setDetailUI() {
        // Set mini poster
        Picasso.get()
                .load(getMovieDetailPosterPath(this, Objects.requireNonNull(mPosterPathString), 400) )
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
        mMovieRating.setText("Rating: " + mMovieRatingString + "/10");

        // Set overview
        mMovieOverview.setText(mMovieOverviewString);
    }

    private ScaleAnimation getScaleAnimation() {
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
        return scaleAnimation;
    }

    private void initFavoriteBtn() {
        DetailViewModelFactory factory =
                new DetailViewModelFactory(mDb, mMovieId);
        final DetailViewModel detailViewModel = new ViewModelProvider(this, factory).get(DetailViewModel.class);
        detailViewModel.getFavorite().observe(this, new Observer<FavoriteEntity>() {
            @Override
            public void onChanged(FavoriteEntity favoriteEntity) {
                detailViewModel.getFavorite().removeObserver(this);
                if (favoriteEntity == null) {
                    mFavoriteBtn.setChecked(false);
                } else {
                    mFavoriteBtn.setChecked(true);
                }
            }
        });
    }
}
