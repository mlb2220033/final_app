package com.example.finalproject.model;

import java.io.Serializable;

public class BankingBrand implements Serializable {

    public String bin_code;
    public String img_url;
    public String brand_name;

    public BankingBrand() {
    }

    public BankingBrand(String bin_code, String img_url, String brand_name) {
        this.bin_code = bin_code;
        this.img_url = img_url;
        this.brand_name = brand_name;
    }

    public String getBin_code() {
        return bin_code;
    }

    public void setBin_code(String bin_code) {
        this.bin_code = bin_code;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }
}
