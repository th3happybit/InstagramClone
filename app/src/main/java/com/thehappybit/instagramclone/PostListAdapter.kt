package com.thehappybit.instagramclone

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thehappybit.instagramclone.models.Post


class PostListAdapter(val context : Context, var posts: List<Post?>) : RecyclerView.Adapter<PostViewHolder>() {

    private val  inflater= LayoutInflater.from(context)





    override fun  onCreateViewHolder(parent:ViewGroup,  viewType:Int):PostViewHolder {
        val view = inflater.inflate(R.layout.feed_item, parent, false)
        return PostViewHolder(view)
    }


    override fun  onBindViewHolder(  holder:PostViewHolder,  position:Int) {

        if (posts != null){
            val  current = posts[position]
            holder.bindToPost(current)
        }
    }

    override fun  getItemCount():Int {
        return if (posts != null){
            posts.size
        } else
            0;
    }

     fun setposts(posts:List<Post?>) {
        this.posts = posts
        notifyDataSetChanged();
    }
}
