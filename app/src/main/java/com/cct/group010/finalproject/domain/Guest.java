package com.cct.group010.finalproject.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Guest {

    private Integer id;
    private String email;
    private String name;
    private String password;
    private List<Reservation> reservationList = new ArrayList<>();
    private Reservation nextReservation = new Reservation();

    public Guest(Integer id, String email, String name, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public Guest() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    public Reservation getNextReservation() {
        return nextReservation;
    }

    public void setNextReservation(Reservation nextReservation) {
        this.nextReservation = nextReservation;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", reservationList=" + reservationList +
                ", nextReservation=" + nextReservation.toString() +
                '}';
    }
}
