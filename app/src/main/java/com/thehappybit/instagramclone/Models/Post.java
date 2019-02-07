package com.thehappybit.instagramclone.Models;

public class Post {

    private String userName;
    private String postText;
    private String userProfileImage;
    private String postImage;

    public Post(){

    }

    public Post(String userName, String postText, String userProfileImage, String postImage){
        this.userName = userName;
        this.postText = postText;
        this.userProfileImage = userProfileImage;
        this.postImage = postImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }
}
