package com.example.android.popmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class FavoriteMovieProvider extends ContentProvider {

    public final static int MOVIES = 100;
    public final static int SINGLE_MOVIE = 101;

    private static UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieDatabaseContract.AUTHORITY, MovieDatabaseContract.PATH_FAVORITE_MOVIES, MOVIES);
        uriMatcher.addURI(MovieDatabaseContract.AUTHORITY, MovieDatabaseContract.PATH_FAVORITE_MOVIES + "/#", SINGLE_MOVIE);
        return uriMatcher;
    }

    private FavoriteMovieDatabase mFavoriteMovieDatabase;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFavoriteMovieDatabase = new FavoriteMovieDatabase(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mFavoriteMovieDatabase.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case MOVIES:
                retCursor = db.query(MovieDatabaseContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case SINGLE_MOVIE:
                String id = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};
                retCursor = db.query(MovieDatabaseContract.MovieEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);


                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mFavoriteMovieDatabase.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case MOVIES:
                long id = db.insert(MovieDatabaseContract.MovieEntry.TABLE_NAME, null, contentValues);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(MovieDatabaseContract.MovieEntry.CONTENT_URI, id);
                }else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: "+ uri);

        }

                getContext().getContentResolver().notifyChange(uri, null);
        Log.d("TAG", "FROM PROVIDER insert: "+returnUri);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mFavoriteMovieDatabase.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int moviesDeleted;

        switch (match) {
            case SINGLE_MOVIE:
                String id = uri.getPathSegments().get(1);
                moviesDeleted = db.delete(MovieDatabaseContract.MovieEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (moviesDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        Log.d("TAG", "FROM PROVIDER delete: "+moviesDeleted);
        return moviesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
