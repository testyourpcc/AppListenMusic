package com.example.applistenmusic.adapters;

import android.content.Context;
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

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private List<MenuItem> mItems;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int id);
    }
    public void setOnItemClickListener(MenuAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public void setmData(List<MenuItem> filteredList) {
        this.mItems = filteredList;
        notifyDataSetChanged();
    }
    public MenuAdapter(List<MenuItem> mItems) {
        this.mItems = mItems;
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
        Glide.with(holder.itemView)
                .load(item.getImage())
                .override(80, 80) // Kích thước mới của hình ảnh sau khi được cắt
                .centerCrop() // Cắt hình ảnh để vừa với kích thước mới
                .into(holder.imageView);
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
                    mListener.onItemClick(item.getId());
                }
            }
        }
    }
}
