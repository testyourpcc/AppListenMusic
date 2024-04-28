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
import com.example.applistenmusic.helpers.AlbumHelper;
import com.example.applistenmusic.helpers.ArtistHelper;
import com.example.applistenmusic.helpers.GenresHelper;
import com.example.applistenmusic.models.Album;

import java.util.List;

public class AlbumSearchResultAdapter extends RecyclerView.Adapter<AlbumSearchResultAdapter.ViewHolder> {

    private List<Album> mData;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int id);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public AlbumSearchResultAdapter(List<Album> data) {
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
        Album item = mData.get(position);

        holder.textViewAlbumTitle.setText(item.getName());
        Glide.with(holder.itemView)
                .load(item.getImage())
                .override(80, 80) // Kích thước mới của hình ảnh sau khi được cắt
                .centerCrop() // Cắt hình ảnh để vừa với kích thước mới
                .into(holder.imageViewAlbum);
        holder.textViewArtist.setText(ArtistHelper.getArtistNameByID(item.getArtist()));
        holder.textViewAlbum.setText(item.getName());
        holder.textViewGenres.setText(GenresHelper.getGenresNameByID(item.getId()

        ));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public void setmData(List<Album> filteredList) {
        this.mData = filteredList;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewAlbum;
        public TextView textViewAlbumTitle;
        public TextView textViewArtist;
        public TextView textViewAlbum;
        public TextView textViewGenres;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAlbum = itemView.findViewById(R.id.imageViewSong);
            textViewAlbumTitle = itemView.findViewById(R.id.textViewSongTitle);
            textViewArtist = itemView.findViewById(R.id.textViewArtist);
            textViewAlbum = itemView.findViewById(R.id.textViewAlbum);
            textViewGenres = itemView.findViewById(R.id.textViewGenres);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Album item = mData.get(position);
                            mListener.onItemClick(item.getId());
                        }
                    }
                }
            });
        }
    }
}
