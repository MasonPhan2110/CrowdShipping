package com.Duong.crowdshipping.Extend;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GetAddress {
    LatLng latLng;
    Context mcontext;
    public GetAddress(LatLng latLng,Context mcontext){
        this.latLng = latLng;
        this.mcontext = mcontext;
    }
    public List<Address> run(){
        Geocoder geocoder = new Geocoder(mcontext, Locale.getDefault());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return list;
        }
    }
}
