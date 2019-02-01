package com.thehappybit.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {

    private RecyclerView postsPreviewRecyclerview;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.action_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() ==R.id.action_home){
                    startActivity(new Intent(getApplicationContext(), Feed.class));
                    return true;
                }
                return false;
            }
        });


        postsPreviewRecyclerview = findViewById(R.id.posts_preview_recycler);

        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 3);

        postsPreviewRecyclerview.setLayoutManager(linearLayoutManager);

        List<String> postList = new ArrayList<>();
        postList.add("http://i.imgur.com/DvpvklR.png");
        postList.add("http://i.imgur.com/DvpvklR.png");
        postList.add("http://i.imgur.com/DvpvklR.png");
        postList.add("http://i.imgur.com/DvpvklR.png");
        postList.add("http://i.imgur.com/DvpvklR.png");
        postList.add("http://i.imgur.com/DvpvklR.png");
        postList.add("http://i.imgur.com/DvpvklR.png");
        postList.add("http://i.imgur.com/DvpvklR.png");
        postList.add("http://i.imgur.com/DvpvklR.png");
        postList.add("http://i.imgur.com/DvpvklR.png");
        postList.add("http://i.imgur.com/DvpvklR.png");
        postList.add("http://i.imgur.com/DvpvklR.png");
        ProfilePostListAdapter postListAdapter = new ProfilePostListAdapter(this, postList);
        postsPreviewRecyclerview.setAdapter(postListAdapter);
    }
}
