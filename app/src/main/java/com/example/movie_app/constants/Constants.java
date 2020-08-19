package com.example.movie_app.constants;

import android.os.Parcel;
import android.os.Parcelable;

public class Constants {
    public static final String TITLE = "TITLE";
    public static final String OVERVIEW = "OVERVIEW";
    public static final String RELEASE_DATE = "RELEASE_DATE";
    public static final String RATING = "RATING";
    public static final String POSTER_PATH = "POSTER_PATH";
    public static final String MOVIE_ID = "MOVIE_ID";
    public static final String MOVIES = "MOVIES";
    public static final String SCREEN_TITLE = "SCREEN_TITLE";
    public static final String STATE = "STATE";
    public static final String RV_STATE = "RV_STATE";

    public enum HomeState implements Parcelable {
        POPULAR, TOP_RATED, FAVORITES;

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(ordinal());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<HomeState> CREATOR = new Creator<HomeState>() {
            @Override
            public HomeState createFromParcel(Parcel in) {
                return HomeState.values()[in.readInt()];
            }

            @Override
            public HomeState[] newArray(int size) {
                return new HomeState[size];
            }
        };
    }

}
