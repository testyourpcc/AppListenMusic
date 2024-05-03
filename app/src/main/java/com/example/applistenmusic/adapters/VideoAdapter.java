package com.example.applistenmusic.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.applistenmusic.R;
import com.example.applistenmusic.models.Song;
import com.example.applistenmusic.models.VideoItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private List<VideoItem> videoItems;
    private OnItemClickListener mListener;

    public VideoAdapter(List<VideoItem> videoItems) {
        this.videoItems = videoItems;
    }
    public void setOnItemClickListener(VideoAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public void setmData(List<VideoItem> filteredList) {
        this.videoItems = filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_youtube_result_layout, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoItem videoItem = videoItems.get(position);
        holder.titleTextView.setText(videoItem.getTitle());
        Glide.with(holder.itemView.getContext())
                .load(videoItem.getThumbnailUrl())
                .into(holder.thumbnailImageView);
    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }

     class VideoViewHolder extends RecyclerView.ViewHolder{
        ImageView thumbnailImageView;
        TextView titleTextView;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.image_view_thumbnail);
            titleTextView = itemView.findViewById(R.id.text_view_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            VideoItem item = videoItems.get(position);
                            mListener.onItemClick(item.getVideoId());
                        }
                    }
                }
            });
        }

    }

    public interface OnItemClickListener {
        void onItemClick(String videoId);
    }
}