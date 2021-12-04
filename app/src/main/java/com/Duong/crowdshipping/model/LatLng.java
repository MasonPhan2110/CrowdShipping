package com.Duong.crowdshipping.model;

public class LatLng {
    Double Lat, Lng;
    public LatLng(Double Lat, Double Lng){
        this.Lat = Lat;
        this.Lng = Lng;
    }
    public LatLng(){

    }

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public Double getLng() {
        return Lng;
    }

    public void setLng(Double lng) {
        Lng = lng;
    }
}
