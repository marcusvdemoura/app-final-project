package com.cct.group010.finalproject.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reservation {


    private String id;

    private String originalBookingNumber;

    private OTA ota;

    private String reservationStatus;


    private Property property;


    private Room room = new Room();


    private LocalDate checkin;
    private LocalDate checkout;

    private int numberOfGuests;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalBookingNumber() {
        return originalBookingNumber;
    }

    public void setOriginalBookingNumber(String originalBookingNumber) {
        this.originalBookingNumber = originalBookingNumber;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }


    public LocalDate getCheckin() {
        return checkin;
    }

    public void setCheckin(LocalDate checkin) {
        this.checkin = checkin;
    }

    public LocalDate getCheckout() {
        return checkout;
    }

    public void setCheckout(LocalDate checkout) {
        this.checkout = checkout;
    }

    public OTA getOta() {
        return ota;
    }

    public void setOta(OTA ota) {
        this.ota = ota;
    }

    @Override
    public String toString() {
        return "Reservation{" + "Id=" + id +
                ", originalBookingNumber='" + originalBookingNumber + '\'' +
                ", numberOfGuests=" + numberOfGuests +
                ", reservationStatus='" + reservationStatus + '\'' +
                ", property=" + property +
                ", room=" + room +
                ", checkin=" + checkin +
                ", checkout=" + checkout +
                '}';
    }
}
