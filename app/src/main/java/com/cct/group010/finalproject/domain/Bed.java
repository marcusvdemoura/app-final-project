package com.cct.group010.finalproject.domain;

public class Bed {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String roomNumber;

    private Integer number;

    private Boolean isVacant;

    public Bed() {
    }

    public Bed(Integer id, String roomNumber, Integer number, Boolean isVacant) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.number = number;
        this.isVacant = isVacant;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Boolean getVacant() {
        return isVacant;
    }

    public void setVacant(Boolean vacant) {
        isVacant = vacant;
    }
}
