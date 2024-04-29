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
import com.example.applistenmusic.activities.Home;
import com.example.applistenmusic.helpers.AlbumHelper;
import com.example.applistenmusic.helpers.ArtistHelper;
import com.example.applistenmusic.helpers.GenresHelper;
import com.example.applistenmusic.models.Song;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class SongSearchResultAdapter extends RecyclerView.Adapter<SongSearchResultAdapter.ViewHolder> {

    private List<Song> mData;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int id);
        void onButtonClick(int id);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public SongSearchResultAdapter(List<Song> data) {
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
        Song item = mData.get(position);

        holder.textViewSongTitle.setText(item.getName());
        Glide.with(holder.itemView)
                .load(item.getImage())
                .override(80, 80) // Kích thước mới của hình ảnh sau khi được cắt
                .centerCrop() // Cắt hình ảnh để vừa với kích thước mới
                .into(holder.imageViewSong);
        holder.textViewArtist.setText(ArtistHelper.getArtistNameByID(item.getArtist()));
        holder.textViewAlbum.setText(AlbumHelper.getAlbumNameByID(item.getAlbum()));
        holder.textViewGenres.setText(GenresHelper.getGenresNameByID(item.getGenres()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public void setmData(List<Song> filteredList) {
        this.mData = filteredList;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewSong , buttonFeature;
        public TextView textViewSongTitle;
        public TextView textViewArtist;
        public TextView textViewAlbum;
        public TextView textViewGenres;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewSong = itemView.findViewById(R.id.imageViewSong);
            textViewSongTitle = itemView.findViewById(R.id.textViewSongTitle);
            textViewArtist = itemView.findViewById(R.id.textViewArtist);
            textViewAlbum = itemView.findViewById(R.id.textViewAlbum);
            textViewGenres = itemView.findViewById(R.id.textViewGenres);
            buttonFeature = itemView.findViewById(R.id.imageViewFeature);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Song item = mData.get(position);
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
                            Song item = mData.get(position);
                            mListener.onButtonClick(item.getId());
                        }
                    }
                }
            });
        }
    }
}
