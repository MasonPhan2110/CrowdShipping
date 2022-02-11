package com.Duong.crowdshipping.model;

import java.util.HashMap;

public class Users {
    private String username, email, phone,id, ava;
    private HashMap<String, Object> idImg;

    public Users(String username, String email, String phone, String id, HashMap<String, Object> idImg, String ava) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.id = id;
        this.idImg = idImg;
        this.ava = ava;
    }
    public Users(){

    }

    public String getAva() {
        return ava;
    }

    public void setAva(String ava) {
        this.ava = ava;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public HashMap<String, Object> getIdImg() {
        return idImg;
    }

    public void setIdImg(HashMap<String, Object> idImg) {
        this.idImg = idImg;
    }
}
