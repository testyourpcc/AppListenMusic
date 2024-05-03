package com.example.applistenmusic.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applistenmusic.R;
import com.example.applistenmusic.models.Artist;
import com.example.applistenmusic.models.UserInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArtistManagerAdapter extends RecyclerView.Adapter<ArtistManagerAdapter.MyViewHolder>{
    Context context;
    List<Artist> artistList;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ArtistManagerAdapter(Context context, List<Artist> artistList) {
        this.context=context;
        this.artistList =artistList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.artist_list_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.userName.setText(artistList.get(position).getName());


        if(!artistList.get(position).getImage().isEmpty()) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(artistList.get(position).getImage());
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(holder.userImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Xử lý lỗi nếu có
                    Log.e("TAG", "Error downloading image", exception);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView userImage;
        TextView userName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.userImage);
            userName = itemView.findViewById(R.id.userName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
    public void updateList(List<Artist> newList) {
        artistList = newList;
        notifyDataSetChanged();
    }

}


