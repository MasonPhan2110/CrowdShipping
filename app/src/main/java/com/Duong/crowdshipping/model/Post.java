package com.Duong.crowdshipping.model;

import java.util.HashMap;

public class Post {
    private String CreateID;
    private HashMap<String, Object> linkImage;
    private String AddressFrom;
    private String AddressTo;
    private String phoneFrom;
    private String phoneTo;
    private String Type;
    private String Description;
    private String Ship;
    private String Time;

    public Post(String CreateID,HashMap<String, Object> linkImage,String AddressFrom,String AddressTo,String phoneFrom,String phoneTo,String Type,String Description,String Ship,String Time){
        this.CreateID= CreateID;
        this.linkImage= linkImage;
        this.AddressFrom= AddressFrom;
        this.AddressTo= AddressTo;
        this.phoneFrom= phoneFrom;
        this.phoneTo= phoneTo;
        this.Type= Type;
        this.Description= Description;
        this.Ship= Ship;
        this.Time= Time;
    }
    public Post() {
    }

    public String getCreateID() {
        return CreateID;
    }

    public void setCreateID(String createID) {
        CreateID = createID;
    }

    public HashMap<String, Object> getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(HashMap<String, Object> linkImage) {
        this.linkImage = linkImage;
    }

    public String getAddressFrom() {
        return AddressFrom;
    }

    public void setAddressFrom(String addressFrom) {
        AddressFrom = addressFrom;
    }

    public String getAddressTo() {
        return AddressTo;
    }

    public void setAddressTo(String addressTo) {
        AddressTo = addressTo;
    }

    public String getPhoneFrom() {
        return phoneFrom;
    }

    public void setPhoneFrom(String phoneFrom) {
        this.phoneFrom = phoneFrom;
    }

    public String getPhoneTo() {
        return phoneTo;
    }

    public void setPhoneTo(String phoneTo) {
        this.phoneTo = phoneTo;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getShip() {
        return Ship;
    }

    public void setShip(String ship) {
        Ship = ship;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
