package com.thehappybit.instagramclone

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.thehappybit.instagramclone.models.Post
import com.thehappybit.instagramclone.models.User
import java.io.ByteArrayOutputStream
import java.io.IOException


class Feed : AppCompatActivity() {

    private lateinit var feed_recyclerview: RecyclerView
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var add_fab: FloatingActionButton
    private lateinit var postImageDialog: Dialog
    private lateinit var postImage: ImageView
    private var postText:String? = ""
    private  var imageBitmap: Bitmap?= null
    private val  requestCode = 100


    private lateinit var databaseReference:DatabaseReference
    private lateinit var postsImages:StorageReference

    private  var user:FirebaseUser?=null

    private lateinit var dbuser: User



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed);

        val  toolbar =  findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.selectedItemId = R.id.action_home;

        bottomNavigationView.setOnNavigationItemSelectedListener {
            if (it.getItemId() ==R.id.action_profile){
                val  intent = Intent(applicationContext, Profile::class.java)
                intent.putExtra("user", dbuser);
                startActivity(intent);

                true
            }
             false
        }

        feed_recyclerview = findViewById(R.id.feed_recyclerview)
        feed_recyclerview.setHasFixedSize(true)

        val  linearLayoutManager = LinearLayoutManager(this)

        feed_recyclerview.layoutManager = linearLayoutManager

        val  postList =  ArrayList<Post>()

        val  postListAdapter = PostListAdapter(this, postList);

        feed_recyclerview.setAdapter(postListAdapter);


        add_fab = findViewById(R.id.add_fab);
        add_fab.setOnClickListener {
            postImageD()
        }

        // get current user
        user = FirebaseAuth.getInstance().getCurrentUser();

        // Initialize the database reference "users"
        databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        // Initialize the storage reference "profiles"
        postsImages = FirebaseStorage.getInstance().getReference("posts");

        dbuser = intent.getExtras().getParcelable("user")


        //Initialize posts valueListener
        val  postListener = object : ValueEventListener {

            override fun  onDataChange( dataSnapshot:DataSnapshot) {
                val  posts = ArrayList<Post? >();
                // get posts
                for (post in dataSnapshot.getChildren()){
                    posts.add(post.getValue(Post::class.java))
                }
                // Set posts in the list
                postListAdapter.setposts(posts)

            }


            override  fun onCancelled( databaseError:   DatabaseError) {
                Toast.makeText(getApplicationContext(), "getting posts failed, check your connection!", Toast.LENGTH_LONG).show()

            }
        };

        //add values listener to posts reference
        databaseReference.addValueEventListener(postListener);

    }

    //Post an image dialog
    private fun  postImageD(){

        // Initialize profile post dialog
        postImageDialog = Dialog(this);
        postImageDialog.setTitle("Post");
        postImageDialog.setContentView(R.layout.post_image_dialog);

        //Initialize views
        postImage = postImageDialog.findViewById(R.id.post_image);
        val   postTextInput = postImageDialog.findViewById<EditText>(R.id.post_text_input)
        val  saveBtn = postImageDialog.findViewById<Button>(R.id.save_button);
        val  cancelBtn = postImageDialog.findViewById<Button>(R.id.cancel_button);
        val  progressBar = postImageDialog.findViewById<ProgressBar>(R.id.progress_bar);

        // when user click on image view (add image icon) this took him to his images gallery to chose an image
        postImage.setOnClickListener {
            val  intent =Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, requestCode);
        }

        //watch text inputs
        postTextInput.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                postText = p0.toString()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        // save button
        saveBtn.setOnClickListener {
            // show progress bar
            progressBar.setVisibility(View.VISIBLE);

            // save the new post to Firebase database
            val fpost = databaseReference.push();
            if (dbuser != null)
                fpost.setValue(Post(dbuser.name, postText, dbuser.imageUrl, ""));

            if (imageBitmap != null) {
                //upload the post image to storage anf give it the post unique key as a name
                val imageName = fpost.getKey() + ".jpg";
                val profileImageRef = postsImages.child(imageName);

                //bitmap to byte array
                val baos = ByteArrayOutputStream();
                imageBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                val data = baos.toByteArray();

                // Initialize the upload task
                val uploadTask = profileImageRef.putBytes(data);
                uploadTask.addOnFailureListener {
                    Toast.makeText(getApplicationContext(), "Upload of the profile image failed, Try Again!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }.addOnSuccessListener {
                    val result = it.getMetadata()!!.getReference()!!.getDownloadUrl();
                    result.addOnSuccessListener {
                        val photoStringUrl = it.toString();
                        // save the download url in post image field in database
                        fpost.child("postImage").setValue(photoStringUrl);

                        //hide progress bar
                        progressBar.setVisibility(View.GONE);
                        //close dialog
                        postImageDialog.dismiss();
                    }
                }
            } else {
                progressBar.setVisibility(View.GONE);
            }


            //close dialog
            cancelBtn.setOnClickListener{
                postText = null;
                imageBitmap = null;
                postImageDialog.dismiss();
                progressBar.setVisibility(View.GONE);
            }

            postImageDialog.show();
        }
    }

    override fun  onCreateOptionsMenu( menu:Menu):Boolean  {
        getMenuInflater().inflate(R.menu.feed_actionbar_menu, menu);
        return true
    }


    override fun  onOptionsItemSelected( item:MenuItem) :Boolean {
        when(item.getItemId()) {
             R.id.action_notifications->

                return true;

             R.id.action_favorite->

                return true;

            else->
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    // on activity of gallery result (to handle the choose image)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == this.requestCode) {
            if (data != null) {
                val selectedImage = data.getData();
                try {
                    //get image from media store
                    imageBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedImage);
                    // set image in image view of dialog
                    postImage.setImageBitmap(imageBitmap);
                } catch ( e:IOException) {
                    e.printStackTrace();
                }
            }
        }
    }
}
