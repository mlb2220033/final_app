package com.example.finalproject.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemsModel implements Serializable {
    private String title;
    private String type;
    private String address;
    private String description;
    private ArrayList<String> picUrl;
    private int price;
    private int bed;
    private int review;
    private boolean wifi;
    private double rating;

    public ItemsModel(String title, String type, String address, String description, ArrayList<String> picUrl, int price, int bed, int review, boolean wifi, double rating) {
        this.title = title;
        this.type = type;
        this.address = address;
        this.description = description;
        this.picUrl = picUrl;
        this.price = price;
        this.bed = bed;
        this.review = review;
        this.wifi = wifi;
        this.rating = rating;
    }

    public ItemsModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(ArrayList<String> picUrl) {
        this.picUrl = picUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getBed() {
        return bed;
    }

    public void setBed(int bed) {
        this.bed = bed;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
