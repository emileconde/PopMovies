package com.example.android.popmovies;
import java.util.List;

/**
 * The sole purpose of this class is to avoid unnecessary method implementation for classes that implement
 * BackgroundTaskCompleteListener
 * */
public abstract class BackgroundTaskListener implements BackgroundTaskCompleteListener {
    @Override
    public void onTaskComplete(List<Movie> result) {

    }

    @Override
    public void onReviewTaskComplete(List<Review> reviews) {

    }

    @Override
    public void onTrailerTaskComplete(List<Trailer> trailers) {

    }
}
