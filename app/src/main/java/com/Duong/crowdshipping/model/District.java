package com.Duong.crowdshipping.model;

import java.util.List;

public class District {
    String id, name;
    List<wards> Wards;
    List<streets> Streets;

    public List<streets> getStreets() {
        return Streets;
    }

    public void setStreets(List<streets> streets) {
        Streets = streets;
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

    public List<wards> getWards() {
        return Wards;
    }

    public void setWards(List<wards> wards) {
        Wards = wards;
    }
}
