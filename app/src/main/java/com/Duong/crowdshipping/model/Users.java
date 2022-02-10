package com.Duong.crowdshipping.model;

public class Users {
    private String username, email, phone,id, idImg;

    public Users(String username, String email, String phone, String id, String idImg) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.id = id;
        this.idImg = idImg;
    }
    public Users(){

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
    public String getIdImg() {
        return idImg;
    }

    public void setIdImg(String idImg) {
        this.idImg = idImg;
    }
}
