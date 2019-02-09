package com.thehappybit.instagramclone;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thehappybit.instagramclone.Models.Post;
import com.thehappybit.instagramclone.Models.User;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

public class Feed extends AppCompatActivity {


    private RecyclerView feed_recyclerview;
    private BottomNavigationView bottomNavigationView;

    private FloatingActionButton add_fab;
    private Dialog postImageDialog;
    private ImageView postImage;
    private String postText = "";
    private Bitmap imageBitmap;
    private int requestCode = 100;


    private DatabaseReference databaseReference;
    private StorageReference postsImages;

    private FirebaseUser user;

    private User dbuser;


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
                    Intent intent = new Intent(getApplicationContext(), Profile.class);
                    intent.putExtra("user", dbuser);
                    startActivity(intent);

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

        final PostListAdapter postListAdapter = new PostListAdapter(this, postList);

        feed_recyclerview.setAdapter(postListAdapter);


        add_fab = findViewById(R.id.add_fab);
        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postImageD();
            }
        });

        // get current user
        user = FirebaseAuth.getInstance().getCurrentUser();

        // Initialize the database reference "users"
        databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        // Initialize the storage reference "profiles"
        postsImages = FirebaseStorage.getInstance().getReference("posts");

        dbuser = (User) getIntent().getExtras().getParcelable("user");


        //Initialize posts valueListener
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Post> posts = new ArrayList<>();
                // get posts
                for (DataSnapshot post : dataSnapshot.getChildren()){
                    posts.add(post.getValue(Post.class));
                }
                // Set posts in the list
                postListAdapter.setPosts(posts);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "getting posts failed, check your connection!", Toast.LENGTH_LONG).show();

            }
        };

        //add values listener to posts reference
        databaseReference.addValueEventListener(postListener);

    }

    //Post an image dialog
    private void postImageD(){

        // Initialize profile post dialog
        postImageDialog = new Dialog(Feed.this);
        postImageDialog.setTitle("Post");
        postImageDialog.setContentView(R.layout.post_image_dialog);

        //Initialize views
        postImage = postImageDialog.findViewById(R.id.post_image);
        final EditText postTextInput = postImageDialog.findViewById(R.id.post_text_input);;
        Button saveBtn = postImageDialog.findViewById(R.id.save_button);
        Button cancelBtn = postImageDialog.findViewById(R.id.cancel_button);
        final ProgressBar progressBar = postImageDialog.findViewById(R.id.progress_bar);

        // when user click on image view (add image icon) this took him to his images gallery to chose an image
        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, requestCode);
            }
        });

        //watch text inputs
        postTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                postText = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // save button
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // show progress bar
                progressBar.setVisibility(View.VISIBLE);

                // save the new post to Firebase database
                final DatabaseReference fpost = databaseReference.push();
                if (dbuser != null){
                    fpost.setValue(new Post(dbuser.getName(), postText,  dbuser.getImageUrl(), ""));

                    if (imageBitmap != null){
                        //upload the post image to storage anf give it the post unique key as a name
                        String imageName = fpost.getKey() + ".jpg";
                        final StorageReference profileImageRef = postsImages.child(imageName);

                        //bitmap to byte array
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        // Initialize the upload task
                        UploadTask uploadTask = profileImageRef.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Upload of the profile image failed, Try Again!", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                //To get the download url of file in storage
                                Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String photoStringUrl = uri.toString();
                                        // save the download url in post image field in database
                                        fpost.child("postImage").setValue(photoStringUrl);

                                        //hide progress bar
                                        progressBar.setVisibility(View.GONE);
                                        //close dialog
                                        postImageDialog.dismiss();
                                    }
                                });
                            }
                        });
                    }
                }else
                    progressBar.setVisibility(View.GONE);

            }
        });

        //close dialog
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postText = null;
                imageBitmap = null;
                postImageDialog.dismiss();
                progressBar.setVisibility(View.GONE);
            }
        });

        postImageDialog.show();
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

    // on activity of gallery result (to handle the choose image)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.requestCode) {
            if (data != null) {
                Uri selectedImage = data.getData();
                try {
                    //get image from media store
                    imageBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedImage);
                    // set image in image view of dialog
                    postImage.setImageBitmap(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
