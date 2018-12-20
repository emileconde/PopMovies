package com.example.android.popmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieDatabaseContract{
    public static final String AUTHORITY = "com.example.android.popmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);
    public static final String PATH_FAVORITE_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();
        public static final String TABLE_NAME = "favoriteMovies";
        public static final String COLUMN_MOVIE_NAME = "name";
        public static final String COLUMN_MOVIE_ID = "movieID";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_BACKDROP_POSTER = "backdropPoster";
    }

}
