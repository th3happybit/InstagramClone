package com.thehappybit.instagramclone;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.thehappybit.instagramclone.models.Post

class PostViewHolder(val view: View) : RecyclerView.ViewHolder(view) {



    val postImage = itemView.findViewById<ImageView>(R.id.post_image);
    val profile_image = itemView.findViewById<ImageView>(R.id.profile_image);
    val userName = itemView.findViewById<TextView>(R.id.user_name);
    val postText = itemView.findViewById<TextView>(R.id.post_text);


    fun  bindToPost(post: Post?){

        if (post != null){
            userName.setText(post.userName);
            postText.setText(post.postText);
            if (!TextUtils.isEmpty(post.postImage))
                Picasso.get().load(post.postImage).into(postImage);
            if (!TextUtils.isEmpty(post.userProfileImage))
                Picasso.get().load(post.userProfileImage).into(profile_image);
        }
    }
}
