package com.Duong.crowdshipping.model;

import java.util.List;

public class District {
    String id, name;
    List<wards> wards;
    List<streets> streets;

    public List<streets> getStreets() {
        return streets;
    }

    public void setStreets(List<streets> streets) {
        this.streets = streets;
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
        return wards;
    }

    public void setWards(List<wards> wards) {
        this.wards = wards;
    }
}
