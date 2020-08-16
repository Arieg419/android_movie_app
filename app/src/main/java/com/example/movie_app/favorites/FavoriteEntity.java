package com.example.movie_app.favorites;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

/**
 * Represents one record of the Favorites table.
 */
@Entity(tableName = "favorites", primaryKeys = {"title", "overview"})
public class FavoriteEntity {

    @NonNull
    @ColumnInfo(name = "title")
    private String mTitle;

    @NonNull
    @ColumnInfo(name = "overview")
    private String mOverview;

    @ColumnInfo(name = "id")
    private int mId;

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

    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    public String getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(String voterAverage) {
        mVoteAverage = voterAverage;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }
}
