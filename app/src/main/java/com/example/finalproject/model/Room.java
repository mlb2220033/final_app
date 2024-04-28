package com.example.finalproject.model;

public class Room {
    String roomID, roomName, roomImage, roomArea,roomBed;
    Float roomPrice;
    int roomPerson;

    public Room() {
    }

    public Room(String roomID, String roomName, String roomImage, String roomArea, String roomBed, Float roomPrice, int roomPerson) {
        this.roomID = roomID;
        this.roomName = roomName;
        this.roomImage = roomImage;
        this.roomArea = roomArea;
        this.roomBed = roomBed;
        this.roomPrice = roomPrice;
        this.roomPerson = roomPerson;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }

    public String getRoomArea() {
        return roomArea;
    }

    public void setRoomArea(String roomArea) {
        this.roomArea = roomArea;
    }

    public String getRoomBed() {
        return roomBed;
    }

    public void setRoomBed(String roomBed) {
        this.roomBed = roomBed;
    }

    public Float getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(Float roomPrice) {
        this.roomPrice = roomPrice;
    }

    public int getRoomPerson() {
        return roomPerson;
    }

    public void setRoomPerson(int roomPerson) {
        this.roomPerson = roomPerson;
    }
}
