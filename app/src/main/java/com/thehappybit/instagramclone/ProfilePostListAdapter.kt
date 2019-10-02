package com.thehappybit.instagramclone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProfilePostListAdapter extends RecyclerView.Adapter<ProfilePostViewHolder> {

    private final LayoutInflater inflater;
    private List<String> urls;

    public ProfilePostListAdapter(Context context, List<String> urls){
        inflater = LayoutInflater.from(context);
        this.urls = urls;
    }

    @NonNull
    @Override
    public ProfilePostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.profile_item, parent, false);
        return new ProfilePostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfilePostViewHolder holder, int position) {

        if (urls != null){
            String current = urls.get(position);
            holder.bindToPost(current);
        }
    }

    @Override
    public int getItemCount() {
        if (urls != null){
            return urls.size();
        } else
            return 0;
    }

    public void setPosts(List<String> urls) {
        this.urls = urls;
        notifyDataSetChanged();
    }
}
