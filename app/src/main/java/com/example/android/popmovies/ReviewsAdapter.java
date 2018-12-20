package com.example.android.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReViewHolder>{
    private ArrayList<Review> mReviews = new ArrayList<>();
    GetIndex mReviewIndex;
    private Context mContext;

    ReviewsAdapter(Context context, ArrayList<Review> reviews, GetIndex reviewIndex){
        this.mContext = context;
        this.mReviews = reviews;
        mReviewIndex = reviewIndex;
    }


    @Override
    public ReViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layout = R.layout.single_review_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout, parent, false);
        return new ReViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ReViewHolder holder, int position) {
        mReviewIndex.onIndexChange(holder.getAdapterPosition());
        Log.d("TAG", "onBindViewHolder: "+holder.getAdapterPosition());
        Review review = mReviews.get(position);
        holder.reviewerName.setText(review.getReviewAuthor());
        holder.review.setText(review.getReviewContent());
    }



    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    class ReViewHolder extends RecyclerView.ViewHolder{
        TextView reviewerName;
        TextView review;
        ReViewHolder(View itemView) {
            super(itemView);
            reviewerName = itemView.findViewById(R.id.tv_name);
            review = itemView.findViewById(R.id.tv_review);
        }
    }

}
