package com.cct.group010.finalproject.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reservation {


    private Integer id;
    private OTA ota = new OTA();
    private String originalBookingNumber;
    private Integer numberOfGuests;
    private String reservationStatus;


    private Property property;


    private List<Room> roomList = new ArrayList<>();


    private List<Guest> guestList = new ArrayList<>();

    private LocalDate checkin;
    private LocalDate checkout;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OTA getOta() {
        return ota;
    }

    public void setOta(OTA ota) {
        this.ota = ota;
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

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    public List<Guest> getGuestList() {
        return guestList;
    }

    public void setGuestList(List<Guest> guestList) {
        this.guestList = guestList;
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

    @Override
    public String toString() {
        return "Reservation{" +

                ", ota=" + ota.toString() +
                ", originalBookingNumber='" + originalBookingNumber + '\'' +
                ", numberOfGuests=" + numberOfGuests +
                ", reservationStatus='" + reservationStatus + '\'' +
                ", property=" + property +
                ", roomList=" + roomList +
                ", guestList=" + guestList +
                ", checkin=" + checkin +
                ", checkout=" + checkout +
                '}';
    }
}
