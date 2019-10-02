package com.thehappybit.instagramclone;

import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProfilePostViewHolder extends RecyclerView.ViewHolder {

    private ImageView image;

    public ProfilePostViewHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.post_image);
    }

    public void bindToPost(String url){
        Picasso.get().load(url).into(image);
    }
}
