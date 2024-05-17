package com.example.finalproject.model;

import com.google.android.gms.maps.model.Marker;

import java.io.Serializable;

public class Hotel implements Serializable {
    String hotelID, hotelName, hotelImage, hotelAddress, hotelDescription, hotelPhone, hotelGmail, adminID;
    Float PricePerNight, StarRating;
    boolean Liked;
    long timestamp;
    double distance;
    float averageRating; // Add this line

    public Hotel() {
    }

    public Hotel(String hotelID, String hotelName, String hotelImage, String hotelAddress, String hotelDescription, String hotelPhone, String hotelGmail, String adminID, Float pricePerNight, long timestamp, Float starRating, boolean liked) {
        this.hotelID = hotelID;
        this.hotelName = hotelName;
        this.hotelImage = hotelImage;
        this.hotelAddress = hotelAddress;
        this.hotelDescription = hotelDescription;
        this.hotelPhone = hotelPhone;
        this.hotelGmail = hotelGmail;
        this.adminID = adminID;
        this.timestamp = timestamp;
        PricePerNight = pricePerNight;
        StarRating = starRating;
        Liked = liked;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getHotelID() {
        return hotelID;
    }

    public void setHotelID(String hotelID) {
        this.hotelID = hotelID;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelImage() {
        return hotelImage;
    }

    public void setHotelImage(String hotelImage) {
        this.hotelImage = hotelImage;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public void setHotelAddress(String hotelAddress) {
        this.hotelAddress = hotelAddress;
    }

    public String getHotelDescription() {
        return hotelDescription;
    }

    public void setHotelDescription(String hotelDescription) {
        this.hotelDescription = hotelDescription;
    }

    public Float getPricePerNight() {
        return PricePerNight;
    }

    public void setPricePerNight(Float pricePerNight) {
        PricePerNight = pricePerNight;
    }

    public Float getStarRating() {
        return StarRating;
    }

    public void setStarRating(Float starRating) {
        StarRating = starRating;
    }

    public boolean isLiked() {
        return Liked;
    }

    public void setLiked(boolean liked) {
        Liked = liked;
    }

    public String getHotelPhone() {
        return hotelPhone;
    }

    public void setHotelPhone(String hotelPhone) {
        this.hotelPhone = hotelPhone;
    }

    public String getHotelGmail() {
        return hotelGmail;
    }

    public void setHotelGmail(String hotelGmail) {
        this.hotelGmail = hotelGmail;
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}