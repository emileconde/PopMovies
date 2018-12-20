package com.example.android.popmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TrailerOrReviewNotFoundFragment extends Fragment {
    public TrailerOrReviewNotFoundFragment() {
    }
    int mMessage;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_trailer_or_review_not_found, container, false);
        TextView mNoReviewOrTrailerTextView;
        mNoReviewOrTrailerTextView = rootView.findViewById(R.id.no_trailer_or_review_found);
        switch (mMessage){
            case 0:
                mNoReviewOrTrailerTextView.setText(getString(R.string.review_not_found));
                break;
            case 1:
                mNoReviewOrTrailerTextView.setText(getString(R.string.trailer_not_found));
                break;
        }

        return rootView;
    }

    public void showNoReviewOrTrailer(int message){
        this.mMessage = message;
    }


}
