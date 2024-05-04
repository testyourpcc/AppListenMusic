package com.example.applistenmusic.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.applistenmusic.R;
import com.example.applistenmusic.activities.SearchYoutube;
import com.example.applistenmusic.models.Song;
import com.example.applistenmusic.models.VideoItem;
import com.squareup.picasso.Picasso;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
        Glide.with(holder.itemView.getContext())
                .load(R.drawable.noimage) // videoItem.getThumbnailUrl() là URL của hình ảnh
                .apply(RequestOptions.circleCropTransform())
                .override(40, 40)
                .into(holder.channelImageView);
        holder.describeTextView.setText(videoItem.getChannelTitle() +" . " + getTimeDifference(videoItem.getPublishTime()));
    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }

     class VideoViewHolder extends RecyclerView.ViewHolder{
        ImageView thumbnailImageView, channelImageView;
        TextView titleTextView, describeTextView;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.image_view_thumbnail);
            titleTextView = itemView.findViewById(R.id.text_view_title);
            describeTextView = itemView.findViewById(R.id.text_view_describe);
            channelImageView = itemView.findViewById(R.id.image_view_channel);
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


    public static String getTimeDifference(String uploadTime) {
        // Chuyển đổi thời gian đăng tải thành đối tượng LocalDateTime
        LocalDateTime uploadDateTime = null;
        long seconds = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            uploadDateTime = LocalDateTime.parse(uploadTime, DateTimeFormatter.ISO_DATE_TIME);


            // Lấy thời gian hiện tại
            Instant currentInstant = Instant.now();
            LocalDateTime currentDateTime = LocalDateTime.ofInstant(currentInstant, ZoneId.systemDefault());

            // Tính khoảng cách giữa hai thời điểm
            Duration duration = Duration.between(uploadDateTime, currentDateTime);

            // Xử lý kết quả
            seconds = duration.getSeconds();
        }
        if (seconds < 3600) {
            // Dưới 1 giờ
            long minutes = seconds / 60;
            return minutes + " phút trước";
        } else if (seconds < 86400) {
            // Dưới 1 ngày
            long hours = seconds / 3600;
            return hours + " giờ trước";
        } else if (seconds < 2592000) {
            // Dưới 1 tháng (30 ngày)
            long days = seconds / 86400;
            return days + " ngày trước";
        } else if (seconds < 31536000) {
            // Dưới 1 năm (365 ngày)
            long months = seconds / 2592000;
            return months + " tháng trước";
        } else {
            // Trên 1 năm
            long years = seconds / 31536000;
            return years + " năm trước";
        }
    }

}