package com.example.movie_app.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

public class PopularMoviesModel implements Parcelable {
    @SerializedName("poster_path")
    public String posterPath;
    @SerializedName("id")
    public Integer id;
    @SerializedName("title")
    public String title;
    @SerializedName("vote_average")
    public Double voteAverage;
    @SerializedName("overview")
    public String overview;
    @SerializedName("release_date")
    public String releaseDate;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(title);
        dest.writeDouble(voteAverage);
    }

    @Ignore
    public PopularMoviesModel() {
    }


    @Ignore
    private PopularMoviesModel(Parcel in){
        this.id = in.readInt();
        this.posterPath = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.title = in.readString();
        this.voteAverage = in.readDouble();
    }

    public static final Parcelable.Creator<PopularMoviesModel> CREATOR = new Parcelable.Creator<PopularMoviesModel>() {
        @Override
        public PopularMoviesModel createFromParcel(Parcel source) {
            return new PopularMoviesModel(source);
        }

        @Override
        public PopularMoviesModel[] newArray(int size) {
            return new PopularMoviesModel[size];
        }
    };
}
