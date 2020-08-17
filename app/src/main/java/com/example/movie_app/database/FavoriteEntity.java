package com.example.movie_app.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Represents one record of the Favorites table.
 */
@Entity(tableName = "favorites")
public class FavoriteEntity {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int mId;

    @NonNull
    @ColumnInfo(name = "title")
    private String mTitle;

    @NonNull
    @ColumnInfo(name = "overview")
    private String mOverview;

    @ColumnInfo(name = "poster_path")
    private String mPosterPath;

    @ColumnInfo(name = "vote_average")
    private String mVoteAverage;

    @ColumnInfo(name = "release_date")
    private String mReleaseDate;

    public FavoriteEntity(int id, String title, String posterPath, String voteAverage, String overview, String releaseDate) {
        mId  = id;
        mTitle = title;
        mPosterPath = posterPath;
        mVoteAverage = voteAverage;
        mOverview = overview;
        mReleaseDate = releaseDate;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public String getVoteAverage() {
        return mVoteAverage;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }
}
