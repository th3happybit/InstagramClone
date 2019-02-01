package com.thehappybit.instagramclone;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.thehappybit.instagramclone.Models.Post;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostViewHolder extends RecyclerView.ViewHolder {

    private ImageView postImage;
    private ImageView profile_image;
    private TextView userName;
    private TextView postText;
    private TextInputEditText commentInput;
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
        }
    }
}
