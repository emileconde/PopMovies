package com.example.android.popmovies.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popmovies.data.MovieDatabaseContract.MovieEntry;

public class FavoriteMovieDatabase extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "favorites.db";
    private static final int VERSION = 1;

    public FavoriteMovieDatabase(Context context){
        super(context, DATABASE_NAME,null , VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE = "CREATE TABLE "+ MovieEntry.TABLE_NAME + " ("+MovieEntry._ID + " INTEGER PRIMARY KEY, "+
                MovieEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, " + MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_POSTER + " TEXT NOT NULL , "+MovieEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, "+
                MovieEntry.COLUMN_RATING + " INTEGER NOT NULL, "+MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "+
                MovieEntry.COLUMN_BACKDROP_POSTER + " TEXT NOT NULL)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean containsMovie(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String queryString = "SELECT * FROM " + MovieEntry.TABLE_NAME+" WHERE " + MovieEntry.COLUMN_MOVIE_ID + " =?";
        Cursor cursor = db.rawQuery(queryString, new String[] {String.valueOf(id)});
        boolean hasObject = false;
        if(cursor.moveToFirst()) {
            hasObject = true;
        }
        cursor.close();
        db.close();
        return hasObject;
    }


    public int getId(int movieID){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " +MovieEntry._ID +
                " FROM " + MovieEntry.TABLE_NAME +
                " WHERE " + MovieEntry.COLUMN_MOVIE_ID + " = ?;";
        Cursor res = db.rawQuery(query, new String[]{String.valueOf(movieID)});
        int id=-1;
        if(res!=null&&res.moveToFirst())
        id=res.getInt(res.getColumnIndex("_id"));
        res.close();
        db.close();
        return id;
    }



}
