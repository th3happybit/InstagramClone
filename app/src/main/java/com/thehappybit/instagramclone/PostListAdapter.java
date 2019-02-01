package com.thehappybit.instagramclone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thehappybit.instagramclone.Models.Post;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostListAdapter  extends RecyclerView.Adapter<PostViewHolder> {

    private final LayoutInflater inflater;

    private List<Post> posts;

    public PostListAdapter(Context context,List<Post> posts) {
        inflater = LayoutInflater.from(context);
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.feed_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {

        if (posts != null){
            Post current = posts.get(position);
            holder.bindToPost(current);
        }
    }

    @Override
    public int getItemCount() {
        if (posts != null){
            return posts.size();
        } else
            return 0;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }
}
