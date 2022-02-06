package com.Duong.crowdshipping.model;

import java.util.List;

public class City {
    String id, code,name;
    List<District> Districts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<District> getDistricts() {
        return Districts;
    }

    public void setDistricts(List<District> districts) {
        Districts = districts;
    }
}
