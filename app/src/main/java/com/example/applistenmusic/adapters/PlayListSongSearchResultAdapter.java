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
import com.example.applistenmusic.helpers.PlayListHelper;
import com.example.applistenmusic.helpers.ArtistHelper;
import com.example.applistenmusic.helpers.GenresHelper;
import com.example.applistenmusic.models.PlayList;
import com.example.applistenmusic.models.MenuItem;
import com.example.applistenmusic.models.PlayList;
import com.example.applistenmusic.models.Song;

import java.util.List;

public class PlayListSongSearchResultAdapter extends RecyclerView.Adapter<PlayListSongSearchResultAdapter.ViewHolder> {

    private List<PlayList> mData;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int id);
        void onButtonClick(int id);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public PlayListSongSearchResultAdapter(List<PlayList> data) {
        mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_search_result_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlayList item = mData.get(position);

        holder.textViewPlayListTitle.setText(item.getName());
        Glide.with(holder.itemView)
                .load(item.getImage())
                .override(80, 80) // Kích thước mới của hình ảnh sau khi được cắt
                .centerCrop() // Cắt hình ảnh để vừa với kích thước mới
                .into(holder.imageViewPlayList);
        holder.textViewArtist.setText("");
        holder.textViewPlayList.setText(item.getName());
        holder.textViewGenres.setText(GenresHelper.getGenresNameByID(item.getId()

        ));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public void setmData(List<PlayList> filteredList) {
        this.mData = filteredList;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewPlayList;
        public TextView textViewPlayListTitle;
        public TextView textViewArtist;
        public TextView textViewPlayList;
        public TextView textViewGenres;
        public  ImageView buttonFeature;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPlayList = itemView.findViewById(R.id.imageViewSong);
            textViewPlayListTitle = itemView.findViewById(R.id.textViewSongTitle);
            textViewArtist = itemView.findViewById(R.id.textViewArtist);
            textViewPlayList = itemView.findViewById(R.id.textViewAlbum);
            textViewGenres = itemView.findViewById(R.id.textViewGenres);
            buttonFeature = itemView.findViewById(R.id.imageViewFeature);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            PlayList item = mData.get(position);
                            mListener.onItemClick(item.getId());
                        }
                    }
                }
            });

            buttonFeature.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            PlayList item = mData.get(position);
                            mListener.onButtonClick(item.getId());
                        }
                    }
                }
            });
        }
    }
}
