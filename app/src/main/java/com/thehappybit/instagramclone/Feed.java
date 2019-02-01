package com.thehappybit.instagramclone;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

import com.thehappybit.instagramclone.Models.Post;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Feed extends AppCompatActivity {


    private RecyclerView feed_recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        feed_recyclerview = findViewById(R.id.feed_recyclerview);
        feed_recyclerview.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        feed_recyclerview.setLayoutManager(linearLayoutManager);

        List<Post> postList = new ArrayList<>();
        postList.add(new Post("Oussama Messabih", "Android workshop 1", "",""));
        postList.add(new Post("Oussama Messabih", "Android workshop 2", "",""));
        postList.add(new Post("Oussama Messabih", "Android workshop 3", "",""));
        postList.add(new Post("Oussama Messabih", "Android workshop 4", "",""));

        PostListAdapter postListAdapter = new PostListAdapter(this, postList);

        feed_recyclerview.setAdapter(postListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notifications:

                return true;

            case R.id.action_favorite:

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
