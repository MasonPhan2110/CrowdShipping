package com.Duong.crowdshipping.model;

import java.io.Serializable;
import java.util.HashMap;

public class Post implements Serializable {
    private String CreateID;
    private HashMap<String, Object> linkImage;
    private HashMap<String,String> AddressFrom;
    private HashMap<String,String> AddressTo;
    private String phoneFrom;
    private String phoneTo;
    private String Type;
    private String Description;
    private String Ship;
    private String Time;
    private String PostID;
    private String Shipper;
    private String Status;
    private Boolean Fragile;

    public Post(String Shipper, String Status,String CreateID,HashMap<String, Object> linkImage,
                HashMap<String,String> AddressFrom,HashMap<String,String> AddressTo,String phoneFrom,String phoneTo,
                String Type,String Description,String Ship,String Time, String PostID, Boolean Fragile){
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
        this.PostID = PostID;
        this.Shipper = Shipper;
        this.Status = Status;
        this.Fragile = Fragile;
    }
    public Post() {
    }

    public Boolean getFragile() {
        return Fragile;
    }

    public void setFragile(Boolean fragile) {
        Fragile = fragile;
    }

    public String getShipper() {
        return Shipper;
    }

    public void setShipper(String shipper) {
        Shipper = shipper;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPostID() {
        return PostID;
    }

    public void setPostID(String postID) {
        PostID = postID;
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

    public HashMap<String,String> getAddressFrom() {
        return AddressFrom;
    }

    public void setAddressFrom(HashMap<String,String> addressFrom) {
        AddressFrom = addressFrom;
    }

    public HashMap<String,String> getAddressTo() {
        return AddressTo;
    }

    public void setAddressTo(HashMap<String,String> addressTo) {
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
