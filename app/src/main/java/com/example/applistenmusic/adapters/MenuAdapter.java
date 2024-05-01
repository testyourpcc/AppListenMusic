package com.example.applistenmusic.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.applistenmusic.R;
import com.example.applistenmusic.models.MenuItem;
import com.example.applistenmusic.models.Song;

import java.util.HashMap;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private int songId = -1;
    private List<MenuItem> mItems;
    private OnItemClickListener mListener;
    private HashMap<String, Integer> imageResources;

    public interface OnItemClickListener {
        void onItemClick(int id, int songId);
    }
    public void setOnItemClickListener(MenuAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public void setmData(List<MenuItem> filteredList) {
        this.mItems = filteredList;
        notifyDataSetChanged();
    }
    public void setSongId(int filteredList) {
        this.songId = filteredList;
        notifyDataSetChanged();
    }
    public MenuAdapter(List<MenuItem> mItems) {
        this.mItems = mItems;
        imageResources = new HashMap<>();
        // Thêm các ánh xạ tên tài nguyên và resId vào HashMap
        imageResources.put("artist", R.drawable.ic_library_filled_24px);
        imageResources.put("playlist", R.drawable.ic_library_filled_24px);
        imageResources.put("album", R.drawable.ic_library_filled_24px);
        imageResources.put("song", R.drawable.ic_library_filled_24px);
        imageResources.put("downloaded", R.drawable.ic_library_filled_24px);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_feature_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItem item = mItems.get(position);
        holder.textView.setText(item.getName());
        if (imageResources.containsKey(item.getImage())) {
            int resId = imageResources.get(item.getImage());
            holder.imageView.setImageResource(resId);
        } else {
            Log.e("MyAdapter", "Resource not found: " + item.getImage());
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    MenuItem item = mItems.get(position);
                    mListener.onItemClick(item.getId(), songId);
                }
            }
        }
    }
}
