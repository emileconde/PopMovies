package com.example.android.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;
import java.util.List;
public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.VideoInfoHolder> {
    private static final String YOUTUBEAPIKEY = BuildConfig.YOUTUBE_API_KEY;
    private TrailerListItemClickListener mOnclickListener;

    private ArrayList<Trailer> mTrailers = new ArrayList<>();
    private Context mContext;

    public interface TrailerListItemClickListener {
        void onListItemClick(int clickedItemIndex, List<Trailer> list);
    }


    TrailersAdapter(Context context, ArrayList<Trailer> trailers, TrailerListItemClickListener listener) {
        this.mContext = context;
        this.mTrailers = trailers;
        this.mOnclickListener = listener;
    }

    @Override
    public VideoInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_trailer, parent, false);
        return new VideoInfoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VideoInfoHolder holder, final int position) {

        final Trailer trailer = mTrailers.get(position);
        final YouTubeThumbnailLoader.OnThumbnailLoadedListener  onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener(){
            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

            }

            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                youTubeThumbnailView.setVisibility(View.VISIBLE);
                holder.relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
            }
        };

        holder.youTubeThumbnailView.initialize(YOUTUBEAPIKEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

                youTubeThumbnailLoader.setVideo(trailer.getYouTubeKey());
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public class VideoInfoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout relativeLayoutOverYouTubeThumbnailView;
        YouTubeThumbnailView youTubeThumbnailView;
        ImageView playButton;

        VideoInfoHolder(View itemView) {
            super(itemView);
            playButton= itemView.findViewById(R.id.btnYoutube_player);
            playButton.setOnClickListener(this);
            relativeLayoutOverYouTubeThumbnailView =  itemView.findViewById(R.id.relativeLayout_over_youtube_thumbnail);
            youTubeThumbnailView = itemView.findViewById(R.id.youtube_thumbnail);
        }

        @Override
        public void onClick(View v) {
            mOnclickListener.onListItemClick(getAdapterPosition(), mTrailers);
        }
    }
}
