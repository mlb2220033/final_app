package com.example.finalproject.model;

public class Rating {
    private String uid;
    private String userName;
    private String comment;
    ;
    private float starRating;
    private String timeRating;
    public Rating() {
    }

    public Rating(String uid, String userName, String comment, float starRating, String timeRating) {
        this.uid = uid;
        this.userName = userName;
        this.comment = comment;
        this.starRating = starRating;
        this.timeRating = timeRating;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getStarRating() {
        return starRating;
    }

    public void setStarRating(float starRating) {
        this.starRating = starRating;
    }

    public String getTimeRating() {
        return timeRating;
    }

    public void setTimeRating(String timeRating) {
        this.timeRating = timeRating;
    }
}
