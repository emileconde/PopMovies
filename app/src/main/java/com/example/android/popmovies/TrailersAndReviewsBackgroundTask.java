package com.example.android.popmovies;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TrailersAndReviewsBackgroundTask extends AsyncTask<Integer, Void, List<Trailer>> {
    private static ArrayList<Trailer> sMTrailerList;
    private static ArrayList<Review> sMReviewList;
    private Context mContext;
    private BackgroundTaskCompleteListener mListener;
    TrailersAndReviewsBackgroundTask(Context context, BackgroundTaskCompleteListener listener){
        mContext = context;
        this.mListener = listener;
    }

    @Override
    protected List<Trailer> doInBackground(Integer... integers) {
        int movieID = integers[0];
        URL reviewUrl = GetRawData.buildReviewUrl(movieID);
        URL trailerUrl = GetRawData.buildTrailerUrl(movieID);
        sMReviewList = new ArrayList<>();
        sMTrailerList = new ArrayList<>();
        try {
            String trailerResult = GetRawData.getResponseFromHttpUrl(trailerUrl);
            try {
                if (trailerResult != null && !trailerResult.equals("")) {
                    JSONObject jsonObject = new JSONObject(trailerResult);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        Trailer trailer = new Trailer(object.getString("key"),
                                object.getString("name")
                                );
                        sMTrailerList.add(trailer);

                    }
                }
            }catch (Exception e ){
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String reviewResult = GetRawData.getResponseFromHttpUrl(reviewUrl);
            try{
                if(reviewResult != null && !reviewResult.equals("")){
                    JSONObject jsonObject = new JSONObject(reviewResult);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        Review review = new Review(object.getString("author"),
                                object.getString("content")
                        );

                    sMReviewList.add(review);

                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sMTrailerList;
    }

    @Override
    protected void onPostExecute(List<Trailer> trailerResult) {
        mListener.onTrailerTaskComplete(sMTrailerList);
        mListener.onReviewTaskComplete(sMReviewList);
    }
}
