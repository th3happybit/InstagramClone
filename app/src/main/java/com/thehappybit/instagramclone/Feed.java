package com.thehappybit.instagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.thehappybit.instagramclone.Models.Post;
import com.thehappybit.instagramclone.Unsplash.Unsplash;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Feed extends AppCompatActivity {


    private RecyclerView feed_recyclerview;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.action_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() ==R.id.action_profile){
                    startActivity(new Intent(getApplicationContext(), Profile.class));
                    return true;
                }
                return false;
            }
        });

        feed_recyclerview = findViewById(R.id.feed_recyclerview);
        feed_recyclerview.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        feed_recyclerview.setLayoutManager(linearLayoutManager);

        List<Post> postList = new ArrayList<>();
        postList.add(new Post("Oussama Messabih", "Art <3","https://th3happybit.github.io/img/ouss.png","http://i.imgur.com/DvpvklR.png"));
        postList.add(new Post("Oussama Messabih", "So cute ","http://i.imgur.com/DvpvklR.png", "https://i.imgur.com/PCOy58O.jpg"));
        postList.add(new Post("Sam Willis", "yum yum do you like to eat ? xD","https://images.pexels.com/users/avatars/457311/sam-willis-646.jpeg?w=256&h=256&fit=crop&crop=faces" , "https://images.pexels.com/photos/1279330/pexels-photo-1279330.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"));
        postList.add(new Post("Joyce Jusayan", "Traveling is life :3 ","https://images.pexels.com/users/avatars/504041/joyce-jusayan-361.jpeg?w=256&h=256&fit=crop&crop=faces" , "https://images.pexels.com/photos/1448885/pexels-photo-1448885.jpeg?cs=srgb&dl=air-aircraft-airplane-1448885.jpg&fm=jpg"));

        postList.add(new Post("Jiroe", "I was at NAMM 2018 this weekend and wow!","https://images.unsplash.com/profile-1533972281012-009a17218288?dpr=1&auto=format&fit=crop&w=32&h=32&q=60&crop=faces&bg=fff", "https://images.unsplash.com/photo-1517273006195-51fa3364bce2?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=966&q=80"));
        postList.add(new Post("Nathan Dumlao", "a Book && Coffee :D weekend is awesome! ","https://images.unsplash.com/profile-1495427732560-fe5248ad6638?dpr=1&auto=format&fit=crop&w=32&h=32&q=60&crop=faces&bg=fff", "https://images.unsplash.com/photo-1520736868738-84c5be99f465?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80"));
        PostListAdapter postListAdapter = new PostListAdapter(this, postList);

        feed_recyclerview.setAdapter(postListAdapter);

        Unsplash unsplash = new Unsplash("326bef9760764eee43c5b3bf729d12207f4a607f1e23c9b4b11db9275fd79cde");

        unsplash.getPhotos();

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
