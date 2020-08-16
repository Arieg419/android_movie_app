package com.example.movie_app.favorites;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * Represents one record of the Favorites table.
 */
@Entity(tableName = "favorites")
public class FavoriteEntity {
    @PrimaryKey(autoGenerate = true)
    private int mId;

    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "poster_path")
    private String mPosterPath;

    @ColumnInfo(name = "vote_average")
    private String mVoteAverage;

    @ColumnInfo(name = "overview")
    private String mOverview;

    @ColumnInfo(name = "release_date")
    private String mReleaseDate;

    @ColumnInfo(name = "updated_at")
    private Date mUpdatedAt;

    public FavoriteEntity(String title, String posterPath, String voteAverage, String overview, String releaseDate) {
       mTitle = title;
       mPosterPath = posterPath;
       mVoteAverage = voteAverage;
       mOverview = overview;
       mReleaseDate = releaseDate;
       mUpdatedAt = new Date();
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

    public Date getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        mUpdatedAt= updatedAt;
    }
}
