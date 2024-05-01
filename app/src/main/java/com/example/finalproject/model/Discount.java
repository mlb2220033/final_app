package com.example.finalproject.model;

public class Discount {
    String id, code;
    Float cost;

    public Discount() {
    }

    public Discount(String id, String code, Float cost) {
        this.id = id;
        this.code = code;
        this.cost = cost;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public Float getCost() {
        return cost;
    }
}
