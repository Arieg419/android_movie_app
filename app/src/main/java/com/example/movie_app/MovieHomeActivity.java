package com.example.movie_app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MovieHomeActivity extends AppCompatActivity {

    private static final String MOVIE_HOME_FRAGMENT = "MovieHomeFragment";
    private static final String SORT_BY_TITLE = "TITLE";
    private static final String SORT_BY_RATING = "RATING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_holder, new MovieHomeFragment(), "MovieHomeFragment").commit();

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
        MovieHomeFragment movieFragment = (MovieHomeFragment) getSupportFragmentManager().findFragmentByTag("MovieHomeFragment");
        if (movieFragment != null && movieFragment.isVisible()) {
            if (sortType.contentEquals(SORT_BY_RATING)) {
                movieFragment.sortByRating();
            } else if (sortType.contentEquals(SORT_BY_TITLE)) {
                movieFragment.sortByTitle();
            }
        }
    }

}