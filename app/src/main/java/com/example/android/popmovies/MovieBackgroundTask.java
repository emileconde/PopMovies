package com.example.android.popmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


class MovieBackgroundTask extends AsyncTask<URL, Void, List<Movie>> {
    private List<Movie> mMovies = new ArrayList<>();
private static final String TAG = "MovieBackgroundTask";
    private Context mContext;
    private BackgroundTaskCompleteListener mListener;

        public MovieBackgroundTask(Context context, BackgroundTaskCompleteListener listener ){
            mContext = context;
            this.mListener = listener;
        }

        @Override
        protected List doInBackground(URL... urls) {
            URL movieUrl = urls[0];
            String movieResults;
            Log.d(TAG, "doInBackground");
            try {
                Log.d(TAG, "doInBackground: STARTS");
                movieResults = GetRawData.getResponseFromHttpUrl(movieUrl);
                if (movieResults != null && !movieResults.equals("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(movieResults);
                        JSONArray jsonArray = jsonObject.getJSONArray("results");

                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            Movie movie = new Movie(object.getString("title"),
                                    object.getInt("id"),
                                    object.getString("poster_path"),
                                    object.getString("overview"),
                                    object.getInt("vote_average"),
                                    object.getString("release_date"),
                                    object.getString("backdrop_path")
                            );
                            mMovies.add(movie);
                        }

                        Log.d(TAG, "doInBackground: ENDS");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return mMovies;
        }

        @Override
        protected void onPostExecute(List<Movie> movieResult) {
          mListener.onTaskComplete(movieResult);
        }

    }

