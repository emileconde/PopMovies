package com.example.android.popmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.popmovies.data.FavoriteMovieDatabase;
import com.example.android.popmovies.data.MovieDatabaseContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
public class MovieActivity extends AppCompatActivity implements TrailersAdapter.TrailerListItemClickListener, GetIndex
    {
    private final int BACKDROP_POSTER = 4;
    private final int POSTER = 1;
    private String[] mInfos;
    private TextView reviewNumberTextView;
    private
    String poster;
    String backdropPoster;
    String synopsis;
    String releaseDate;
    int id;
    AppBarLayout Appbar;
    CollapsingToolbarLayout CoolToolbar;
    Toolbar toolbar;
    private boolean ExpandedActionBar = true;
    FloatingActionButton mFavoriteFab;
    private boolean mClicked = false;
    private String title;
    private int rating;
    //The Uri returned upon a successful save in the database
    Uri returnedUri;
    private int dbId;
    //private ImageView mPoster;
    private RelativeLayout mReviewsLayout;
    private LinearLayout mTrailersLayout;
    private FrameLayout mNoReviewLayout;
    private FrameLayout mNoTrailerLayout;
    //private boolean fromFavorite = false;
    private final int ACTION_ADD = 1;
    private final int ACTION_REMOVE = 0;
    private int mReviewIndex;
    private int numberOfReview;
    private static final String REVIEW_INDEX_KEY = "reviewIndex";
    private static final String NUMBER_OF_REVIEW = "numberOfReview";
    private static final String INFOS_KEY = "infos";
    private static final String ID_KEY = "id";
    private static final String RATING_KEY = "rating";
    private static final String DB_ID_KEY = "dbId";
    private static final String REVIEWS_KEY = "reviews";
    private static final String TRAILERS_KEY = "trailers";
    private final int NO_REVIEW = 0;
    ArrayList<Review> mReviews;
    ArrayList<Trailer> mTrailers;
    int scrollPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_movie);
        ImageView posterImage = findViewById(R.id.iv_backdrop_poster);
        ImageView mPoster = findViewById(R.id.iv_poster);
        TextView mSynopsis = findViewById(R.id.tv_synopsis);
        TextView mTitle = findViewById(R.id.tv_title);
        TextView mReleaseDate = findViewById(R.id.tv_releaseDate);
        TextView mRating = findViewById(R.id.tv_rating);
        mFavoriteFab = findViewById(R.id.fab_favorite);
        Appbar = findViewById(R.id.appbar);
        CoolToolbar = findViewById(R.id.ct_toolbar);
        toolbar = findViewById(R.id.toolbar);
        mReviewsLayout = findViewById(R.id.reviews_layout);
        mTrailersLayout = findViewById(R.id.trailers_layout);
        mNoReviewLayout = findViewById(R.id.no_review);
        mNoTrailerLayout = findViewById(R.id.no_trailer);
        reviewNumberTextView = findViewById(R.id.tv_review_number);
        final int TITLE = 0;
        final int SYNOPSIS = 2;
        final int RELEASE_DATE = 3;
        final int INTENT_DEFAULT = 0;
        final int DATABASE_DEFAULT = 1;
        boolean fromFavorite;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if(savedInstanceState != null){
//                Toast.makeText(this, " "+savedInstanceState.getInt(RATING_KEY), Toast.LENGTH_SHORT).show();
            if(savedInstanceState.containsKey(REVIEW_INDEX_KEY) && savedInstanceState.containsKey(NUMBER_OF_REVIEW)
                    && savedInstanceState.containsKey(INFOS_KEY) && savedInstanceState.containsKey(ID_KEY) &&
                    savedInstanceState.containsKey(RATING_KEY) && savedInstanceState.containsKey(DB_ID_KEY)
                    && savedInstanceState.containsKey(TRAILERS_KEY) && savedInstanceState.containsKey(REVIEWS_KEY)
                    ){
                mInfos = savedInstanceState.getStringArray(INFOS_KEY);
                title = mInfos[TITLE];
                poster = "http://image.tmdb.org/t/p/w342/" + mInfos[POSTER];
                backdropPoster = "http://image.tmdb.org/t/p/w1280/" + mInfos[BACKDROP_POSTER];
                synopsis = mInfos[SYNOPSIS];
                releaseDate = mInfos[RELEASE_DATE];
                mReviewIndex = savedInstanceState.getInt(REVIEW_INDEX_KEY);
                numberOfReview = savedInstanceState.getInt(NUMBER_OF_REVIEW);
                rating = savedInstanceState.getInt(RATING_KEY);
                dbId = savedInstanceState.getInt(DB_ID_KEY);
                id = savedInstanceState.getInt(ID_KEY);
                mReviews = savedInstanceState.getParcelableArrayList(REVIEWS_KEY);
                mTrailers = savedInstanceState.getParcelableArrayList(TRAILERS_KEY);
                if(numberOfReview > 0){
                reviewNumberTextView.setText(String.format("%s/%s", String.valueOf(mReviewIndex), String.valueOf(numberOfReview)));
                }else {
                    mReviewsLayout.setVisibility(View.GONE);
                    mNoReviewLayout.setVisibility(View.VISIBLE);
                    TrailerOrReviewNotFoundFragment trailerOrReviewNotFoundFragment = new TrailerOrReviewNotFoundFragment();
                    trailerOrReviewNotFoundFragment.showNoReviewOrTrailer(NO_REVIEW);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().add(R.id.no_review, trailerOrReviewNotFoundFragment)
                            .commit();
                }
                showReviews(mReviews);
                showTrailers(mTrailers);
            }
        }else {

        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.intent_info_key)) && intent.hasExtra(getString(R.string.intent_rating_key))
                && intent.hasExtra(getString(R.string.intent_id_key)) && intent.hasExtra(getString(R.string.intent_databaseID_key))
                && intent.hasExtra(getString(R.string.intent_from_database_key)) && intent.hasExtra(getString(R.string.scroll_index))
                )
            mInfos = intent.getStringArrayExtra(getString(R.string.intent_info_key));
            title = mInfos[TITLE];
            poster = "http://image.tmdb.org/t/p/w342/" + mInfos[POSTER];
            backdropPoster = "http://image.tmdb.org/t/p/w1280/" + mInfos[BACKDROP_POSTER];
            synopsis = mInfos[SYNOPSIS];
            releaseDate = mInfos[RELEASE_DATE];
            id = intent.getIntExtra(getString(R.string.intent_id_key), INTENT_DEFAULT);
            rating = intent.getIntExtra(getString(R.string.intent_rating_key), INTENT_DEFAULT);
            dbId = intent.getIntExtra(getString(R.string.intent_databaseID_key), DATABASE_DEFAULT);
            scrollPosition = intent.getIntExtra(getString(R.string.scroll_index), INTENT_DEFAULT) ;
            fromFavorite = intent.getBooleanExtra(getString(R.string.intent_from_database_key), false);
            new TrailersAndReviewsBackgroundTask(MovieActivity.this, new FetchDataOnComplete()).execute(id);
            //If offline, make useless views invisible
            if (fromFavorite) {
                mTrailersLayout.setVisibility(View.GONE);
                mReviewsLayout.setVisibility(View.GONE);
            }
        }


        mTitle.setText(title);
        mRating.setText(String.format("%s%s", String.valueOf(rating), getString(R.string.rating_ten)));
        mReleaseDate.setText(releaseDate);
        mSynopsis.setText(synopsis);
        Picasso.with(getApplicationContext())
                .load(backdropPoster)
                .placeholder(R.drawable.ic_image_black_32dp)
                .error(R.drawable.ic_broken_image_black_32dp)
                .into(posterImage);

        Picasso.with(getApplicationContext()).load(poster)
                .placeholder(R.drawable.ic_image_black_32dp)
                .error(R.drawable.ic_broken_image_black_32dp)
                .into(mPoster);
        mFavoriteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFavoritePreference();
            }
        });

        Appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Math.abs(verticalOffset) > 200){
                    ExpandedActionBar = false;
                    CoolToolbar.setTitle(title);
                    invalidateOptionsMenu();
                } else {
                    ExpandedActionBar = true;
                    CoolToolbar.setTitle("");
                    invalidateOptionsMenu();
                }
            }
        });

        FavoriteMovieDatabase favoriteMovieDatabase = new FavoriteMovieDatabase(this);
        boolean inDatabase = favoriteMovieDatabase.containsMovie(id);
        updateFavoriteButton(inDatabase);

    }


        @Override
        protected void onSaveInstanceState(Bundle currentState) {
            super.onSaveInstanceState(currentState);
            currentState.putInt(REVIEW_INDEX_KEY, mReviewIndex);
            currentState.putInt(NUMBER_OF_REVIEW, numberOfReview);
            currentState.putStringArray(INFOS_KEY, mInfos);
            currentState.putInt(ID_KEY, id);
            currentState.putInt(RATING_KEY, rating);
            currentState.putInt(DB_ID_KEY, dbId);
            currentState.putParcelableArrayList(REVIEWS_KEY, mReviews);
            currentState.putParcelableArrayList(TRAILERS_KEY, mTrailers);
        }


        @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(MovieActivity.this, MainActivity.class);
        intent.putExtra(getString(R.string.scroll_position), scrollPosition);
        startActivity(intent);
        return true;
    }

    @Override
    public void onListItemClick(int clickedItemIndex, List<Trailer> list) {
        playYoutubeVideo(MovieActivity.this, list.get(clickedItemIndex).getYouTubeKey());

    }

        @Override
        public void onIndexChange(int reviewIndex) {
            mReviewIndex = reviewIndex+1;
                //Toast.makeText(this, "Sup", Toast.LENGTH_SHORT).show();
                reviewNumberTextView.setText(String.format("%s/%s", String.valueOf(this.mReviewIndex), String.valueOf(numberOfReview)));
        }


        public class FetchDataOnComplete extends BackgroundTaskListener{
        private final int NO_TRAILER = 1;
        @Override
        public void onReviewTaskComplete(List<Review> reviews) {
            mReviews = new ArrayList<>(reviews);
            if(reviews.size() > 0){
                     showReviews((ArrayList<Review>) reviews);
                     numberOfReview = reviews.size();
            }else {
                mReviewsLayout.setVisibility(View.GONE);
                mNoReviewLayout.setVisibility(View.VISIBLE);
                TrailerOrReviewNotFoundFragment trailerOrReviewNotFoundFragment = new TrailerOrReviewNotFoundFragment();
                trailerOrReviewNotFoundFragment.showNoReviewOrTrailer(NO_REVIEW);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.no_review, trailerOrReviewNotFoundFragment)
                        .commit();
            }
        }

        @Override
        public void onTrailerTaskComplete(List<Trailer> trailers) {
            mTrailers = new ArrayList<>(trailers);
            if(trailers.size() > 0) {
                showTrailers((ArrayList<Trailer>) trailers);
            }else {
                mTrailersLayout.setVisibility(View.GONE);
                mNoTrailerLayout.setVisibility(View.VISIBLE);
                TrailerOrReviewNotFoundFragment trailerOrReviewNotFoundFragment = new TrailerOrReviewNotFoundFragment();
                trailerOrReviewNotFoundFragment.showNoReviewOrTrailer(NO_TRAILER);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.no_trailer, trailerOrReviewNotFoundFragment)
                        .commit();
            }
        }

        }

    public static void playYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }}

        public void showReviews(ArrayList<Review> reviews){
            RecyclerView reviewRecyclerView = findViewById(R.id.rv_review);
            reviewRecyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            reviewRecyclerView.setLayoutManager(layoutManager);
            ReviewsAdapter reviewsAdapter = new ReviewsAdapter(this, reviews, this);
            reviewRecyclerView.setAdapter(reviewsAdapter);

        }

        public void showTrailers(ArrayList<Trailer> trailers){
            RecyclerView trailersRecyclerView= findViewById(R.id.rv_tailers);
            trailersRecyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            trailersRecyclerView.setLayoutManager(linearLayoutManager);
            TrailersAdapter trailersAdapter= new TrailersAdapter(this, trailers, this);
            trailersRecyclerView.setAdapter(trailersAdapter);
        }

        public void getFavoritePreference() {
            if (mClicked) {
               removeFavorite();
            } else
                {
                  addFavorite();
                }
            }

        //Let's the user know whether a movie is favorited or not by keeping the icon selected
        public void updateFavoriteButton(boolean isFavorite){
           if(isFavorite){
               mFavoriteFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_clicked_24dp));
               mClicked = true;
           }else {
               mFavoriteFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_not_clicked));
               mClicked = false;
           }
        }

        public void showSnackBar(String message, int action){
                Snackbar movieSnackBar = Snackbar.make(mFavoriteFab, message,
                        Snackbar.LENGTH_SHORT);
            switch (action) {
                case ACTION_ADD:
                movieSnackBar.setAction(getString(R.string.snack_bar_undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeFavorite();
                    }
                });
                movieSnackBar.show();
                break;
                case ACTION_REMOVE:
                    movieSnackBar.setAction(getString(R.string.snack_bar_undo), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addFavorite();
                        }
                    });
                    movieSnackBar.show();
                    break;
            }
        }

        public void addFavorite(){
            mFavoriteFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_clicked_24dp));
            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieDatabaseContract.MovieEntry.COLUMN_MOVIE_NAME, title);
            contentValues.put(MovieDatabaseContract.MovieEntry.COLUMN_MOVIE_ID, id);
            contentValues.put(MovieDatabaseContract.MovieEntry.COLUMN_POSTER, mInfos[POSTER]);
            contentValues.put(MovieDatabaseContract.MovieEntry.COLUMN_BACKDROP_POSTER, mInfos[BACKDROP_POSTER]);
            contentValues.put(MovieDatabaseContract.MovieEntry.COLUMN_SYNOPSIS, synopsis);
            contentValues.put(MovieDatabaseContract.MovieEntry.COLUMN_RATING, rating);
            contentValues.put(MovieDatabaseContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
            returnedUri = getContentResolver().insert(MovieDatabaseContract.MovieEntry.CONTENT_URI, contentValues);
            if(returnedUri!=null){
                showSnackBar(getString(R.string.snackbar_movie_added), ACTION_ADD);
            }
            mClicked = true;
        }

        public void removeFavorite(){
            mFavoriteFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_not_clicked));
            if (returnedUri != null) {
                //User hasn't left the activity
                getContentResolver().delete(returnedUri, null, null);
                showSnackBar(getString(R.string.snackbar_movie_removed ), ACTION_REMOVE);
            } else {
                //User has left the activity and is back again
                returnedUri = ContentUris.withAppendedId(MovieDatabaseContract.MovieEntry.CONTENT_URI, dbId);
                getContentResolver().delete(returnedUri, null, null);
                showSnackBar(getString(R.string.snackbar_movie_removed ), ACTION_REMOVE);
            }

            mClicked = false;
        }

}
