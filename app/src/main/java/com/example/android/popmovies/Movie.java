package com.example.android.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable{
        private String name;
        private int id;
        private String poster;
        private String backdropPoster;
        private String synopsis;
        private int rating;
        private String releaseDate;
        private int databaseId;

    Movie(){

        }


    private Movie(Parcel in) {
        name = in.readString();
        id = in.readInt();
        poster = in.readString();
        backdropPoster = in.readString();
        synopsis = in.readString();
        rating = in.readInt();
        releaseDate = in.readString();
        databaseId = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(id);
        parcel.writeString(poster);
        parcel.writeString(backdropPoster);
        parcel.writeString(synopsis);
        parcel.writeInt(rating);
        parcel.writeString(releaseDate);
        parcel.writeInt(databaseId);
    }


    int getDatabaseId() {
        return databaseId;
    }

    void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    Movie(String name, int id, String poster, String synopsis, int rating, String releaseDate, String backdropPoster
        ) {
            this.name = name;
            this.id = id;
            this.poster = poster;
            this.synopsis = synopsis;
            this.rating = rating;
            this.releaseDate = releaseDate;
            this.backdropPoster = backdropPoster;
        }


    String getBackdropPoster() {
        return backdropPoster;
    }

    void setBackdropPoster(String backdropPoster) {
        this.backdropPoster = backdropPoster;
    }

    public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        String getPoster() {
            return poster;
        }

        void setPoster(String poster) {
            this.poster = poster;
        }

         String getSynopsis() {
            return synopsis;
        }

        void setSynopsis(String synopsis) {
            this.synopsis = synopsis;
        }

        int getRating() {
            return rating;
        }

        void setRating(int rating) {
            this.rating = rating;
        }

        String getReleaseDate() {
            return releaseDate;
        }

        void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }
}

