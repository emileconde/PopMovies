package com.example.android.popmovies;


import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable{
    private String youTubeKey;
    private String title;

    Trailer(String youTubeKey, String title) {
        this.youTubeKey = youTubeKey;
        this.title = title;
    }

    protected Trailer(Parcel in) {
        youTubeKey = in.readString();
        title = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    String getYouTubeKey() {
        return youTubeKey;
    }

    public void setYouTubeKey(String youTubeKey) {
        this.youTubeKey = youTubeKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(youTubeKey);
        parcel.writeString(title);
    }
}
