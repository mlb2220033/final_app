package com.example.finalproject.model;

public class Hotel {
    String hotelID, hotelName, hotelImage, hotelAddress, hotelPhone, hotelGmail;
    Float PricePerNight, StarRating;
    boolean Liked;

    public Hotel() {
    }

    public Hotel(String hotelID, String hotelName, String hotelImage, String hotelAddress, Float pricePerNight, Float starRating, boolean liked) {
        this.hotelID = hotelID;
        this.hotelName = hotelName;
        this.hotelImage = hotelImage;
        this.hotelAddress = hotelAddress;
        PricePerNight = pricePerNight;
        StarRating = starRating;
        Liked = liked;
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
}
