package com.example.movie_app;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.example.movie_app.fragments.MovieHomeFragment;
import com.example.movie_app.sort.SortableList;

import static com.example.movie_app.sort.SortableList.sort;

public class MovieHomeActivity extends AppCompatActivity {

    private static final String MOVIE_HOME_FRAGMENT = "MovieHomeFragment";

    private MovieHomeFragment mMovieHomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar init
        Toolbar mToolbar = findViewById(R.id.toolbar);
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
        MovieHomeFragment movieHomeFragment = (MovieHomeFragment) getSupportFragmentManager().findFragmentByTag(MOVIE_HOME_FRAGMENT);
        switch(item.getItemId()){
            case R.id.popular_sort:
                sort(SortableList.SORT_BY_TITLE, movieHomeFragment);
                break;

            case R.id.rating_sort:
                sort(SortableList.SORT_BY_RATING, movieHomeFragment);
                break;

            case R.id.favorite_sort:
                sort(SortableList.SORT_BY_FAVORITE, movieHomeFragment);
                break;

        }
        return true;
    }
}