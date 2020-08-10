package com.example.movie_app.fragments;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_app.R;
import com.example.movie_app.api.ApiClient;
import com.example.movie_app.api.ApiInterface;
import com.example.movie_app.model.MovieVideosModel;
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

    ApiInterface mApiInterface;
    ImageView mMoviePoster;
    ImageView mMiniPoster;
    TextView mMovieTitle;
    TextView mMovieReleaseDate;
    TextView mMovieRating;
    TextView mMovieOverview;
    List<MovieVideosModel> mTrailers;
    MovieTrailerAdapter movieTrailerAdapter;
    RecyclerView mMovieTrailerRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get args
        Bundle b = this.getArguments();
        assert(b != null);

        // Fetch trailer data
        mApiInterface = ApiClient.getClient(Objects.requireNonNull(getContext())).create(ApiInterface.class);
        fetchMovieData(mApiInterface.getTrailers(b.getLong(MOVIE_ID)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.movie_detail, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle b = this.getArguments();
        assert(b != null);

        // Get views
        mMoviePoster = view.findViewById(R.id.movie_detail_poster);
        mMiniPoster = view.findViewById(R.id.movie_detail_mini_poster);
        mMovieTitle = view.findViewById(R.id.movie_detail_title);
        mMovieReleaseDate = view.findViewById(R.id.movie_detail_release_date);
        mMovieRating = view.findViewById(R.id.movie_detail_rating);
        mMovieOverview = view.findViewById(R.id.movie_detail_overview);
        mMovieTrailerRecyclerView = view.findViewById(R.id.movie_trailer_recycler_view);

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
        String releaseDate = b.getString(RELEASE_DATE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            assert releaseDate != null;
            Date date = simpleDateFormat.parse(releaseDate);
            String day          = (String) DateFormat.format("dd",   date); // 20
            String month = (String) DateFormat.format("MMMM",  date); // Jun
            String year         = (String) DateFormat.format("yyyy", date); // 2013
            mMovieReleaseDate.setText("Release: " + month + " " + day + ", " + year);
        } catch (ParseException e) {
            mMovieReleaseDate.setText(releaseDate);
        }

        // Set rating
        mMovieRating.setText("Rating: " + b.getString(RATING) + "/10");

        // Set overview
        mMovieOverview.setText(b.getString(OVERVIEW));
    }

    private void fetchMovieData(Call<MovieVideosModel> call) {
        // Async request with callback invocation
        call.enqueue(new Callback<MovieVideosModel>() {
            @Override
            public void onResponse(Call<MovieVideosModel> call, Response<MovieVideosModel> response) {
                MovieVideosModel result = response.body();
                if (result == null) {
                    return;
                }
                mTrailers = result.videos;
                for(MovieVideosModel mv: mTrailers) {
                    System.out.println("Omer is here with title: " + mv.name + " key: " + mv.key);
                }

                movieTrailerAdapter = new MovieTrailerAdapter(mTrailers);
                mMovieTrailerRecyclerView.setAdapter(movieTrailerAdapter);
                mMovieTrailerRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            }

            @Override
            public void onFailure(Call<MovieVideosModel> call, Throwable t) {
                call.cancel();
            }
        });
    }
}
