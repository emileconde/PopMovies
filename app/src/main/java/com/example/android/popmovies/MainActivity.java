package com.example.android.popmovies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.popmovies.data.FavoriteMovieDatabase;
import com.example.android.popmovies.data.MovieDatabaseContract;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener, LoaderManager.LoaderCallbacks<List<Movie>>,
        SharedPreferences.OnSharedPreferenceChangeListener, GetIndex {
    Context context = MainActivity.this;
    MovieAdapter mMovieAdapter;
    RecyclerView mRecyclerView;
    GridLayoutManager mGridLayoutManager;
    private static final int MOST_POPULAR = 1;
    private static final int TOP_RATED = 2;
    private static final int FAVORITES = 3;
    private static final int TASK_LOADER_ID = 0;
    private static final int GRID_OFFSET = 0;
    private static final int INTENT_DEFAULT = 0;
    private boolean fromFavorite = false;
    private int mScrollIndex = 0;
    private static final String SCROLL_POSITION_INDEX_KEY = "scrollKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.rv_movie_display);
        mGridLayoutManager = new GridLayoutManager(this, numberOfColumns());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        displayMovieListFromSharedPreferences(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        if(savedInstanceState!=null) {
            if (savedInstanceState.containsKey(SCROLL_POSITION_INDEX_KEY)) {
                mScrollIndex = savedInstanceState.getInt(SCROLL_POSITION_INDEX_KEY);
                mGridLayoutManager.scrollToPositionWithOffset(mScrollIndex, GRID_OFFSET);
            }
        }

        Intent intent = getIntent();
        if(intent.hasExtra(getString(R.string.scroll_position))){
            mScrollIndex = intent.getIntExtra(getString(R.string.scroll_position), INTENT_DEFAULT);
            mGridLayoutManager.scrollToPositionWithOffset(mScrollIndex, GRID_OFFSET);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putInt(SCROLL_POSITION_INDEX_KEY, mScrollIndex);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, DisplaySettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Helper method to display the setting selected by user()
    public void displayMovieListFromSharedPreferences(SharedPreferences sharedPreferences) {
        int movie = Integer.valueOf(sharedPreferences.getString(getString(R.string.pref_movie_key), getString(R.string.pref_most_popular_value)));
        switch (movie) {
            case MOST_POPULAR:
                sortMovie(movie);
                fromFavorite = false;
                break;
            case TOP_RATED:
                sortMovie(movie);
                fromFavorite = false;
                break;
            case FAVORITES:
                fromFavorite = true;
                getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
                break;
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_movie_key))) {
            displayMovieListFromSharedPreferences(sharedPreferences);
        }
    }


    /**
     * @param clickedItemIndex is the index of the item clicked
     * @param list             is the list that is going to be used to extract the infos that are going to be
     *                         sent to MovieActivity
     */
    @Override
    public void onListItemClick(int clickedItemIndex, List<Movie> list) {
        FavoriteMovieDatabase favoriteMovieDatabase = new FavoriteMovieDatabase(this);
        //In the database
        if (fromFavorite) {
            fireIntent(list.get(clickedItemIndex).getName(), list.get(clickedItemIndex).getPoster(),
                    list.get(clickedItemIndex).getSynopsis(), list.get(clickedItemIndex).getRating(),
                    list.get(clickedItemIndex).getReleaseDate(), list.get(clickedItemIndex).getBackdropPoster(),
                    list.get(clickedItemIndex).getId(),
                    list.get(clickedItemIndex).getDatabaseId(),
                    fromFavorite,
                    mScrollIndex
            );
        }else
            //In the database but not selected from the movies displayed in the database
            if(favoriteMovieDatabase.containsMovie(list.get(clickedItemIndex).getId())){
                fireIntent(list.get(clickedItemIndex).getName(), list.get(clickedItemIndex).getPoster(),
                        list.get(clickedItemIndex).getSynopsis(), list.get(clickedItemIndex).getRating(),
                        list.get(clickedItemIndex).getReleaseDate(), list.get(clickedItemIndex).getBackdropPoster(),
                        list.get(clickedItemIndex).getId(),
                        favoriteMovieDatabase.getId(list.get(clickedItemIndex).getId()),
                        fromFavorite,
                        mScrollIndex
                );
        }else
            //Not in the database
            if(!fromFavorite){
                fireIntent(list.get(clickedItemIndex).getName(), list.get(clickedItemIndex).getPoster(),
                        list.get(clickedItemIndex).getSynopsis(), list.get(clickedItemIndex).getRating(),
                        list.get(clickedItemIndex).getReleaseDate(), list.get(clickedItemIndex).getBackdropPoster(),
                        list.get(clickedItemIndex).getId(),
                        list.get(clickedItemIndex).getDatabaseId(),
                        fromFavorite,
                        mScrollIndex
                );
            }
    }

    /**
     * This Method sends an intent to the Destination Activity with all the info needed to display
     */
    void fireIntent(String name, String poster, String synopsis, int rating, String releaseDate, String backdropPoster, int id, int
                    databaseId, boolean fromFavorite, int scrollIndex) {
        Context context = MainActivity.this;
        Class movieActivity = MovieActivity.class;
        String[] infos = {name, poster, synopsis, releaseDate, backdropPoster};
        Intent startMovieActivity = new Intent(context, movieActivity);
        startMovieActivity.putExtra(getString(R.string.intent_info_key), infos);
        startMovieActivity.putExtra(getString(R.string.intent_rating_key), rating);
        startMovieActivity.putExtra(getString(R.string.intent_id_key), id);
        startMovieActivity.putExtra(getString(R.string.intent_databaseID_key), databaseId);
        startMovieActivity.putExtra(getString(R.string.intent_from_database_key), fromFavorite);
        startMovieActivity.putExtra(getString(R.string.scroll_index), scrollIndex);
        startActivity(startMovieActivity);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Movie>>(this) {

            @Override
            protected void onStartLoading() {
                if (getFavoriteMovies()!= null) {
                    deliverResult(getFavoriteMovies());
                } else {
                    forceLoad();
                }
            }

            @Override
            public List<Movie> loadInBackground() {
                return getFavoriteMovies();
            }

            public void deliverResult(List<Movie> data) {
                super.deliverResult(data);
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        if(data != null && data.size() > 0){
        showMovieList(data);
        }else {
       Toast.makeText(this, getString(R.string.toast_db_empty), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        showMovieList(null);
    }

    @Override
    public void onIndexChange(int index) {
        mScrollIndex = index;
    }


    public class FetchDataOnComplete extends BackgroundTaskListener {
        @Override
        public void onTaskComplete(List<Movie> result) {
            showMovieList(result);
        }

    }

    /**
     * This method alters the url based on what user chooses
     */
    public void sortMovie(int n) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            URL movieUrl = GetRawData.buildUrl(n);
            new MovieBackgroundTask(MainActivity.this, new FetchDataOnComplete()).execute(movieUrl);
        } else {
            Toast.makeText(context, R.string.error_no_internet, Toast.LENGTH_SHORT).show();
        }
    }


    public void showMovieList(List<Movie> films) {
        mMovieAdapter = new MovieAdapter(getApplicationContext(), films, this, this);
        //mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns()));
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mMovieAdapter);

    }

    public List<Movie> getFavoriteMovies() {
        List<Movie> favorites = new ArrayList<>();
        String[] columns = {
                MovieDatabaseContract.MovieEntry._ID,
                MovieDatabaseContract.MovieEntry.COLUMN_MOVIE_NAME,
                MovieDatabaseContract.MovieEntry.COLUMN_MOVIE_ID,
                MovieDatabaseContract.MovieEntry.COLUMN_POSTER,
                MovieDatabaseContract.MovieEntry.COLUMN_SYNOPSIS,
                MovieDatabaseContract.MovieEntry.COLUMN_RATING,
                MovieDatabaseContract.MovieEntry.COLUMN_RELEASE_DATE,
                MovieDatabaseContract.MovieEntry.COLUMN_BACKDROP_POSTER
        };

        String sortOrder = MovieDatabaseContract.MovieEntry._ID + " ASC";
        Cursor cursor = getContentResolver().query(MovieDatabaseContract.MovieEntry.CONTENT_URI,
                columns,
                null,
                null,
                sortOrder
        );

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    Movie movie = new Movie();
                    movie.setDatabaseId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(MovieDatabaseContract.MovieEntry._ID))));
                    movie.setName(cursor.getString(cursor.getColumnIndex(MovieDatabaseContract.MovieEntry.COLUMN_MOVIE_NAME)));
                    movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MovieDatabaseContract.MovieEntry.COLUMN_MOVIE_ID))));
                    movie.setPoster(cursor.getString(cursor.getColumnIndex(MovieDatabaseContract.MovieEntry.COLUMN_POSTER)));
                    movie.setSynopsis(cursor.getString(cursor.getColumnIndex(MovieDatabaseContract.MovieEntry.COLUMN_SYNOPSIS)));
                    movie.setRating(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MovieDatabaseContract.MovieEntry.COLUMN_RATING))));
                    movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieDatabaseContract.MovieEntry.COLUMN_RELEASE_DATE)));
                    movie.setBackdropPoster(cursor.getString(cursor.getColumnIndex(MovieDatabaseContract.MovieEntry.COLUMN_BACKDROP_POSTER)));
                    favorites.add(movie);
                } while (cursor.moveToNext());
            }

            cursor.close();
        }else {
            return null;
        }
        return favorites;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

}
