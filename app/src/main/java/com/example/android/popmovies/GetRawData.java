package com.example.android.popmovies;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

class GetRawData {

    private final static String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private final static String PATH_TOP_RATED = "top_rated";
    private final static String PARAM_API_KEY = "api_key";
    private static final String API_KEY = BuildConfig.API_KEY;
    private final static String PATH_MOST_POPULAR = "popular";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_TRAILERS = "videos";


    /**@param path is :
     * 1 for popular movies
     * 2 for top rated movies
     * */
    static URL buildUrl(int path) {
        Uri builtUri = null;
        switch(path){

            case 1:
                builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(PATH_MOST_POPULAR)
                        .appendQueryParameter(PARAM_API_KEY, API_KEY)
                        .build();

                break;
            case 2:
                builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(PATH_TOP_RATED)
                        .appendQueryParameter(PARAM_API_KEY, API_KEY)
                        .build();
                break;

        }
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    static URL buildReviewUrl(int movieId){

       Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(String.valueOf(movieId))
                .appendPath(PATH_REVIEWS)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    static URL buildTrailerUrl(int movieId){

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(String.valueOf(movieId))
                .appendPath(PATH_TRAILERS)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }





    static String getResponseFromHttpUrl(URL url) throws
            IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }


    }


}
