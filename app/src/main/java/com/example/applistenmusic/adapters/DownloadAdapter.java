package com.example.applistenmusic.adapters;

import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applistenmusic.R;
import com.example.applistenmusic.activities.PlayView;
import com.example.applistenmusic.models.PlayList;
import com.example.applistenmusic.models.Song;
import com.example.applistenmusic.models.SongDownload;

import java.util.ArrayList;
import java.util.List;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {

    private List<Song> mData;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(String url);
        void onButtonClick(int id);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public DownloadAdapter(List<Song> data) {
        mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_playlist_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song item = mData.get(position);
        String songName = item.getName();
        if (songName != null) {
            holder.textView.setText(songName);
        } else {
            holder.textView.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewPlayListName);

            if (textView == null) {
                throw new RuntimeException("textViewPlayListName not found in itemView");
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Song item = mData.get(position);
                            mListener.onItemClick(item.getUrl());
                        }
                    }
                }
            });
        }
    }
}