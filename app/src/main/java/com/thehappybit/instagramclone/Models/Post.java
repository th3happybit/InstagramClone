package com.thehappybit.instagramclone.Models;

public class Post {

    private String postTitle;
    private String userName;
    private String postText;
    private String commentText;
    private String profileImage;
    private String postImage;

    public Post(String userName, String postText, String profileImage, String postImage) {
        this.userName = userName;
        this.postText = postText;
        this.profileImage = profileImage;
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

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }
}
