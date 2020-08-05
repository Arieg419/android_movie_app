package com.example.movie_app;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.example.movie_app.fragments.MovieDetailFragment;

import java.util.Objects;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String MOVIE_DETAIL_FRAGMENT = "MovieDetailFragment";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);

        // Toolbar init
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        Bundle b = getIntent().getExtras();
        movieDetailFragment.setArguments(b);
        ft.replace(R.id.fragment_holder, movieDetailFragment, MOVIE_DETAIL_FRAGMENT).commit();

        // toolbar setup
        showToolbarBackButton();
        mToolbar.setTitleMarginStart(0);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        assert b != null;
        Objects.requireNonNull(getSupportActionBar()).setTitle(b.getString(MovieDetailFragment.TITLE));
    }

    public void showToolbarBackButton() {
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }
}
