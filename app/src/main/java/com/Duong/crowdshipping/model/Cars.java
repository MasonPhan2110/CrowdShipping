package com.Duong.crowdshipping.model;

public class Cars {
    String plate, vehicle, id, status;

    public Cars(String plate, String vehicle, String id, String status) {
        this.plate = plate;
        this.vehicle = vehicle;
        this.id = id;
        this.status = status;
    }

    public Cars() {
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }
}
