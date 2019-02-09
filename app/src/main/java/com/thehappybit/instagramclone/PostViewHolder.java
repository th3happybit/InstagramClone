package com.thehappybit.instagramclone;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thehappybit.instagramclone.Models.Post;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostViewHolder extends RecyclerView.ViewHolder {

    private ImageView postImage;
    private ImageView profile_image;
    private TextView userName;
    private TextView postText;
    private EditText commentInput;
    private ImageButton heartButton;
    private ImageButton sendButton;


    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        postImage = itemView.findViewById(R.id.post_image);
        profile_image = itemView.findViewById(R.id.profile_image);
        userName = itemView.findViewById(R.id.user_name);
        postText = itemView.findViewById(R.id.post_text);
        commentInput = itemView.findViewById(R.id.comment_input);
        heartButton = itemView.findViewById(R.id.heart_it);
        sendButton = itemView.findViewById(R.id.send_it);
    }

    public void bindToPost(Post post){

        if (post != null){
            userName.setText(post.getUserName());
            postText.setText(post.getPostText());
            if (!TextUtils.isEmpty(post.getPostImage()))
                Picasso.get().load(post.getPostImage()).into(postImage);
            if (!TextUtils.isEmpty(post.getUserProfileImage()))
                Picasso.get().load(post.getUserProfileImage()).into(profile_image);
        }
    }
}
