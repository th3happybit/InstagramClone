package com.thehappybit.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
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
        ViewCompat.setNestedScrollingEnabled(postsPreviewRecyclerview, false);
        postsPreviewRecyclerview.setHasFixedSize(false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

        postsPreviewRecyclerview.setLayoutManager(gridLayoutManager);

        List<String> postList = new ArrayList<>();
        postList.add("http://i.imgur.com/DvpvklR.png");
        postList.add("https://i.imgur.com/PCOy58O.jpg");
        postList.add("https://images.pexels.com/photos/1279330/pexels-photo-1279330.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        postList.add("https://images.pexels.com/photos/1448885/pexels-photo-1448885.jpeg?cs=srgb&dl=air-aircraft-airplane-1448885.jpg&fm=jpg");
        postList.add("https://images.unsplash.com/photo-1517273006195-51fa3364bce2?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=966&q=80");
        postList.add("https://i.imgur.com/PCOy58O.jpg");
        postList.add("https://images.pexels.com/photos/1279330/pexels-photo-1279330.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        postList.add("https://images.pexels.com/photos/1448885/pexels-photo-1448885.jpeg?cs=srgb&dl=air-aircraft-airplane-1448885.jpg&fm=jpg");
        postList.add("https://i.imgur.com/PCOy58O.jpg");
        ProfilePostListAdapter postListAdapter = new ProfilePostListAdapter(this, postList);
        postsPreviewRecyclerview.setAdapter(postListAdapter);
    }
}
