package com.cct.group010.finalproject.domain;

import java.util.List;

public class Property {
    private String id;


    private String name;
    private String address;


    public Property(String id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;

    }

    public Property() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    @Override
    public String toString() {
        return "Property{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
