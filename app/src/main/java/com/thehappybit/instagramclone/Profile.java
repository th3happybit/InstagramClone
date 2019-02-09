package com.thehappybit.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import com.squareup.picasso.Picasso;
import com.thehappybit.instagramclone.Models.Post;
import com.thehappybit.instagramclone.Models.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

public class Profile extends AppCompatActivity {

    private RecyclerView postsPreviewRecyclerview;
    private BottomNavigationView bottomNavigationView;

    //declare a dialog
    private Dialog editProfile;
    private int requestCode = 100;
    private Bitmap imageBitmap;
    private String name;
    private String location;
    private ImageView dialogProfileImage;

    private CircleImageView profileImage;
    private TextView nameTextView;
    private TextView locationTextView;
    private ImageButton addImage;

    private DatabaseReference usersReference;
    private StorageReference profileImages;

    private FirebaseUser user;
    private User userdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // get current user
        user = FirebaseAuth.getInstance().getCurrentUser();
        userdb = (User) getIntent().getExtras().getParcelable("user");

        // Initialize the database reference "users"
        usersReference = FirebaseDatabase.getInstance().getReference("users");
        // Initialize the storage reference "profiles"
        profileImages = FirebaseStorage.getInstance().getReference("profiles");

        // Initialize views
        profileImage = findViewById(R.id.userPhoto);
        nameTextView = findViewById(R.id.user_name);
        locationTextView = findViewById(R.id.location_name);
        addImage = findViewById(R.id.add_image);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show dialog
                editProfile();
            }
        });

        //Event Listener for profile data
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userdb = dataSnapshot.getValue(User.class);
                //set change to views
                if (userdb != null){
                    if (!TextUtils.isEmpty(userdb.getName()))
                        nameTextView.setText(userdb.getName());

                    if (!TextUtils.isEmpty(userdb.getLocation()))
                        locationTextView.setText(userdb.getLocation());

                    if (!TextUtils.isEmpty(userdb.getImageUrl()))
                        Picasso.get().load(userdb.getImageUrl()).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "getting profile data failed, check your connection!", Toast.LENGTH_LONG).show();
            }
        };

        //Initialize user reference
        DatabaseReference userRef = usersReference.child(user.getUid());
        // add event listener to the user reference
        userRef.addValueEventListener(valueEventListener);


        // bottom navigation view implementation
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.action_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.action_home){
                    Intent intent = new Intent(getApplicationContext(), Feed.class);
                    intent.putExtra("user", userdb);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });


        // RecyclerView implementation
        postsPreviewRecyclerview = findViewById(R.id.posts_preview_recycler);
        ViewCompat.setNestedScrollingEnabled(postsPreviewRecyclerview, false);
        postsPreviewRecyclerview.setHasFixedSize(false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

        postsPreviewRecyclerview.setLayoutManager(gridLayoutManager);

        List<String> postList = new ArrayList<>();
        final ProfilePostListAdapter postListAdapter = new ProfilePostListAdapter(this, postList);
        postsPreviewRecyclerview.setAdapter(postListAdapter);


        //Initialize posts valueListener
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> posts = new ArrayList<>();
                // get posts
                for (DataSnapshot post : dataSnapshot.getChildren()){
                    // add just the user posts
                    if (userdb != null)
                        if (TextUtils.equals(user.getUid(), userdb.getUid()))
                            posts.add(post.getValue(Post.class).getPostImage());
                }
                //Set posts in the list
                postListAdapter.setPosts(posts);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "getting posts failed, check your connection!", Toast.LENGTH_LONG).show();

            }
        };

        // Initialize the database reference "users"
        FirebaseDatabase.getInstance().getReference("posts").addValueEventListener(postListener);

    }

    //Edit profile dialog
    private void editProfile(){
        // Initialize profile edit dialog
        editProfile = new Dialog(Profile.this);
        editProfile.setTitle("Edit Profile");
        editProfile.setContentView(R.layout.edit_profile_dialog);
        //Initialize views
        dialogProfileImage = editProfile.findViewById(R.id.profile_image);
        EditText nameInput = editProfile.findViewById(R.id.name_input);
        final EditText locationInput = editProfile.findViewById(R.id.location_input);
        Button saveBtn = editProfile.findViewById(R.id.save_button);
        Button cancelBtn = editProfile.findViewById(R.id.cancel_button);
        final ProgressBar progressBar = editProfile.findViewById(R.id.progress_bar);


        //set content to views
        if (!TextUtils.isEmpty(nameTextView.getText()))
            nameInput.setText(nameTextView.getText());

        if (!TextUtils.isEmpty(locationTextView.getText()))
            locationInput.setText(locationTextView.getText());

        if (profileImage.getDrawable() != null)
            dialogProfileImage.setImageDrawable(profileImage.getDrawable());

        // when user click on image this took him to his images gallery to chose an image
        dialogProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, requestCode);
            }
        });

        //watch text inputs
        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        locationInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                location = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // save button
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                // name variable isn't empty
                if (!TextUtils.isEmpty(name)){
                    // save the new name to database
                    usersReference.child(user.getUid()).child("name").setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }

                progressBar.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(location)){
                    // save new location to database
                    usersReference.child(user.getUid()).child("location").setValue(location).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }

                progressBar.setVisibility(View.VISIBLE);
                if (imageBitmap != null){
                    //upload new profile image to storage
                    String imageName = user.getUid()+ ".jpg";
                    final StorageReference profileImageRef = profileImages.child(imageName);

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
                                // to get the download url of file in storage
                                Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String photoStringUrl = uri.toString();
                                        // save the download url in user imageUrl field in database
                                        usersReference.child(user.getUid()).child("imageUrl").setValue(photoStringUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressBar.setVisibility(View.GONE);
                                                editProfile.dismiss();
                                            }
                                        });
                                    }
                                });
                        }
                    });


                }
            }
        });

        //close dialog
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = null;
                location = null;
                imageBitmap = null;
                editProfile.dismiss();
                progressBar.setVisibility(View.GONE);
            }
        });

        editProfile.show();
    }

    // on activity of gallery result (to handle the choose image)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.requestCode) {
            if (data != null) {
                Uri selectedImage = data.getData();
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedImage);
                    dialogProfileImage.setImageBitmap(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
