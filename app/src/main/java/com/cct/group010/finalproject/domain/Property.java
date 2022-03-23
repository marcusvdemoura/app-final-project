package com.cct.group010.finalproject.domain;

import java.util.List;

public class Property {
    private Integer id;


    private String name;
    private String address;

    private List<Room> roomList;

    private List<Reservation> reservationList;


    public Property(Integer id, String name, String address, List<Room> roomList, List<Reservation> reservationList) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.roomList = roomList;
        this.reservationList = reservationList;
    }

    public Property() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }
}
