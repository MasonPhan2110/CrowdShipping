package com.Duong.crowdshipping.model;

public class Parks {
    String Address, ID, Name;
    public Parks(String Address, String ID, String Name) {
        this.Address = Address;
        this.ID = ID;
        this.Name = Name;
    }
    public Parks(){
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
