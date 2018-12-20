package com.example.android.popmovies;

import java.util.List;

public interface BackgroundTaskCompleteListener {
    void onTaskComplete(List<Movie> result);
    void onReviewTaskComplete(List<Review> reviews);
    void onTrailerTaskComplete(List<Trailer> trailers);
}
