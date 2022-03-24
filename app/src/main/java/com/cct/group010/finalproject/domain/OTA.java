package com.cct.group010.finalproject.domain;

import java.util.List;

public class OTA {
    private String id;
    private String name;
    private String website;

    public OTA(Integer id, String name, String website) {
        this.name = name;
        this.website = website;
    }



    public OTA() {
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return "OTA{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}
