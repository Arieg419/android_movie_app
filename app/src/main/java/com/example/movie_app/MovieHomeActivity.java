package com.example.movie_app;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

interface FragmentSwapper {
    public void setMovieDetailFragment(Bundle bundle);
    public void setMovieHomeFragment();
}

public class MovieHomeActivity extends AppCompatActivity implements FragmentSwapper {

    private static final String MOVIE_HOME_FRAGMENT = "MovieHomeFragment";
    private static final String MOVIE_DETAIL_FRAGMENT = "MovieDetailFragment";
    private static final String SORT_BY_TITLE = "TITLE";
    private static final String SORT_BY_RATING = "RATING";

    private Toolbar mToolbar;
    private MovieHomeFragment mMovieHomeFragment;
    private MovieDetailFragment mMovieDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar init
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (mMovieHomeFragment == null) {
            mMovieHomeFragment = new MovieHomeFragment();
        }
        ft.replace(R.id.fragment_holder, mMovieHomeFragment, MOVIE_HOME_FRAGMENT).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.alphabetical_sort:
                sort(SORT_BY_TITLE);
                Toast.makeText(this, "You clicked title sort", Toast.LENGTH_SHORT).show();
                break;

            case R.id.rating_sort:
                sort(SORT_BY_RATING);
                Toast.makeText(this, "You clicked ratings sort", Toast.LENGTH_SHORT).show();
                break;

        }
        return true;
    }

    private void sort(String sortType) {
        MovieHomeFragment movieFragment = (MovieHomeFragment) getSupportFragmentManager().findFragmentByTag(MOVIE_HOME_FRAGMENT);
        if (movieFragment != null && movieFragment.isVisible()) {
            if (sortType.contentEquals(SORT_BY_RATING)) {
                movieFragment.sortByRating();
            } else if (sortType.contentEquals(SORT_BY_TITLE)) {
                movieFragment.sortByTitle();
            }
        }
    }

    public void setMovieDetailFragment(Bundle bundle) {
        if (mMovieDetailFragment == null) {
            mMovieDetailFragment = new MovieDetailFragment();
        }
        mMovieDetailFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_holder, mMovieDetailFragment, MOVIE_DETAIL_FRAGMENT);
        fragmentTransaction.commit();

        // toolbar setup
        showToolbarBackButton();
        mToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMovieHomeFragment();
            }
        });
        mToolbar.setTitle(bundle.getString(MovieDetailFragment.TITLE));
        Menu menu = mToolbar.getMenu();
        menu.findItem(R.id.alphabetical_sort).setVisible(false);
        menu.findItem(R.id.rating_sort).setVisible(false);
    }

    public void setMovieHomeFragment() {
        if (mMovieHomeFragment == null) {
            mMovieHomeFragment = new MovieHomeFragment();
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_holder, mMovieHomeFragment, MOVIE_HOME_FRAGMENT);
        fragmentTransaction.commit();
        hideToolbarBackButton();
        mToolbar.setTitle(R.string.app_name);
        Menu menu = mToolbar.getMenu();
        menu.findItem(R.id.alphabetical_sort).setVisible(true);
        menu.findItem(R.id.rating_sort).setVisible(true);
        showToolbar();
    }

    public void showToolbarBackButton() {
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public void hideToolbarBackButton() {
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
    }

    public void showToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
    }

}