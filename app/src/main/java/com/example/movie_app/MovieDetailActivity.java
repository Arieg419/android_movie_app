package com.example.movie_app;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_app.adapters.MovieReviewAdapter;
import com.example.movie_app.adapters.MovieTrailerAdapter;
import com.example.movie_app.api.ApiClient;
import com.example.movie_app.api.ApiInterface;
import com.example.movie_app.database.FavoriteEntity;
import com.example.movie_app.executors.AppExecutors;
import com.example.movie_app.model.MovieReviewsModel;
import com.example.movie_app.model.MovieVideosModel;
import com.example.movie_app.viewmodel.DetailViewModel;
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

public class MovieDetailActivity extends AppCompatActivity {

    public static final String TITLE = "TITLE";
    public static final String OVERVIEW = "OVERVIEW";
    public static final String RELEASE_DATE = "RELEASE_DATE";
    public static final String RATING = "RATING";
    public static final String POSTER_PATH = "POSTER_PATH";
    public static final String MOVIE_ID = "MOVIE_ID";

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
    RecyclerView mMovieTrailerRecyclerView;
    RecyclerView mMovieReviewRecyclerView;

    private DetailViewModel mDetailViewModel;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_detail);
        mContext = this;

        // Get views
        mMiniPoster = findViewById(R.id.movie_detail_mini_poster);
        mMovieTitle = findViewById(R.id.movie_detail_title);
        mMovieReleaseDate = findViewById(R.id.movie_detail_release_date);
        mMovieRating = findViewById(R.id.movie_detail_rating);
        mMovieOverview = findViewById(R.id.movie_detail_overview);
        mFavoriteBtn = findViewById(R.id.movie_detail_add_to_fav);
        mMovieTrailerRecyclerView = findViewById(R.id.movie_trailer_recycler_view);
        mMovieReviewRecyclerView = findViewById(R.id.movie_reviews_recycler_view);

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

        // Get instance of favorite db
        mDetailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);

        // Get http client
        mApiInterface = ApiClient.getClient(this).create(ApiInterface.class);

        // Fetch trailer data
        fetchTrailerData(mApiInterface.getTrailers(movieId));

        // Fetch review data
        fetchReviewData(mApiInterface.getReviews(movieId));

        // Network data
        setDetailUI();

        // Favorite btn
        setFavoriteBtn();

        // toolbar setup
        Toolbar mToolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(mToolbar);
        showToolbarBackButton();
        mToolbar.setTitleMarginStart(0);
        mToolbar.setNavigationOnClickListener(view -> finish());
        Objects.requireNonNull(getSupportActionBar()).setTitle(b.getString(TITLE));

    }

    public void showToolbarBackButton() {
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
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
                movieTrailerAdapter = new MovieTrailerAdapter(mTrailers);
                mMovieTrailerRecyclerView.setAdapter(movieTrailerAdapter);
                mMovieTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
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
                mMovieReviewRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            }

            @Override
            public void onFailure(Call<MovieReviewsModel > call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void setFavoriteBtn() {
        AppExecutors.getExecutorInstance().getDiskIO().execute(() -> {
            List<FavoriteEntity> favs = mDetailViewModel.loadFavorites().getValue();

            if (favs != null) {
                for (FavoriteEntity fav: favs) {
                    System.out.println("Omer -> found fav: " + fav.getTitle());
                }
            } else {
                System.out.println("Omer -> found 0000 favs!");
            }
        });

        // Query room db for favorite
        AppExecutors.getExecutorInstance().getDiskIO().execute(() -> {
            System.out.println("Omer -> query by title: " + mMovieTitleString + " id: " + mMovieId);
            @Nullable FavoriteEntity favoriteEntity1 = mDetailViewModel.getFavoriteById(mMovieId).getValue();
            if (favoriteEntity1 == null) {
                this.runOnUiThread(() -> mFavoriteBtn.setChecked(false));
            } else {
                this.runOnUiThread(() -> mFavoriteBtn.setChecked(true));
            }
        });

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
                mDetailViewModel.addFavorite(favorite);
                Toast.makeText(this, "Added to favorites!", Toast.LENGTH_SHORT).show();
            } else  {
                mDetailViewModel.removeFavorite(favorite);
                Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
            }
            // TODO: send to home with favorite flags
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
}
