package com.example.android.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

class MovieAdapter extends RecyclerView.Adapter<
        MovieAdapter.MovieViewHolder> {
    private List<Movie> mMovies;
    private Context mContext;
    private ListItemClickListener mOnClickListener;
    private  GetIndex mMovieIndex;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, List<Movie> list);
    }


    MovieAdapter(Context context, List<Movie> movies, ListItemClickListener listener, GetIndex index) {
        this.mMovies = movies;
        this.mContext = context;
        this.mOnClickListener = listener;
        this.mMovieIndex = index;
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutID = R.layout.movie_item_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutID, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        mMovieIndex.onIndexChange(holder.getAdapterPosition());
        final Movie movie = mMovies.get(position);
        holder.itemView.setTag(position);
        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w342/" + movie.getPoster())
                .placeholder(R.drawable.ic_image_black_32dp)
                .error(R.drawable.ic_broken_image_black_32dp)
                .into(holder.mMovieImage);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }


    //ViewHolder
    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mMovieImage;

        MovieViewHolder(View itemView) {
            super(itemView);
            mMovieImage = itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onListItemClick(getAdapterPosition(), mMovies);
        }
    }

}
