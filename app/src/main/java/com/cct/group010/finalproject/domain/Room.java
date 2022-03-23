package com.cct.group010.finalproject.domain;

public class Room {

    private Integer id;

    private String type;
    private String roomNumber;
    private String floor;
    private Integer numberBeds;
    private String rfidTag;

    public Room() {

    }

    public Room(Integer id, String type, String roomNumber, String floor, Integer numberBeds, String rfidTag) {
        this.id = id;
        this.type = type;
        this.roomNumber = roomNumber;
        this.floor = floor;
        this.numberBeds = numberBeds;
        this.rfidTag = rfidTag;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public Integer getNumberBeds() {
        return numberBeds;
    }

    public void setNumberBeds(Integer numberBeds) {
        this.numberBeds = numberBeds;
    }

    public String getRfidTag() {
        return rfidTag;
    }

    public void setRfidTag(String rfidTag) {
        this.rfidTag = rfidTag;
    }
}
