package com.example.android.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable{
   private String reviewAuthor;
   private String reviewContent;

    Review(String reviewAuthor, String reviewContent) {
        this.reviewAuthor = reviewAuthor;
        this.reviewContent = reviewContent;
    }

    protected Review(Parcel in) {
        reviewAuthor = in.readString();
        reviewContent = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    String getReviewAuthor() {
        return reviewAuthor;
    }

    public void setReviewAuthor(String reviewAuthor) {
        this.reviewAuthor = reviewAuthor;
    }

    String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(reviewAuthor);
        parcel.writeString(reviewContent);
    }
}
