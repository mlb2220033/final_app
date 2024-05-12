package com.example.finalproject.model;

import java.io.Serializable;

public class BookingHistory implements Serializable {

    public String hotel_id;
    public String type_room;
    public Float cost; //Giá này là giá của phòng
    public Long time_stamp; //Ghi nhận thời gian booking
    public Long check_in;
    public Long check_out;
    public Integer room_count; //SL phòng đã đặt
    public Float day_count; //SL ngày đã đặt
    public String status; //Trạng thái: Paid hoặc

    public BookingHistory() {
    }

    public BookingHistory(String hotel_id,
                          String type_room,
                          Float cost,
                          Long time_stamp,
                          Long check_in,
                          Long check_out,
                          Integer room_count,
                          Float day_count,
                          String status) {
        this.hotel_id = hotel_id;
        this.type_room = type_room;
        this.cost = cost;
        this.time_stamp = time_stamp;
        this.check_in = check_in;
        this.check_out = check_out;
        this.room_count = room_count;
        this.day_count = day_count;
        this.status = status;
    }

    public String getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(String hotel_id) {
        this.hotel_id = hotel_id;
    }

    public String getType_room() {
        return type_room;
    }

    public void setType_room(String type_room) {
        this.type_room = type_room;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(Long time_stamp) {
        this.time_stamp = time_stamp;
    }

    public Long getCheck_in() {
        return check_in;
    }

    public void setCheck_in(Long check_in) {
        this.check_in = check_in;
    }

    public Long getCheck_out() {
        return check_out;
    }

    public void setCheck_out(Long check_out) {
        this.check_out = check_out;
    }

    public Integer getRoom_count() {
        return room_count;
    }

    public void setRoom_count(Integer room_count) {
        this.room_count = room_count;
    }

    public Float getDay_count() {
        return day_count;
    }

    public void setDay_count(Float day_count) {
        this.day_count = day_count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
