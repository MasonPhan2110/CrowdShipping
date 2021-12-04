package com.Duong.crowdshipping.Extend;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetDistance {
    LatLng latLngend;
    Context mcontext;
    public GetDistance(LatLng latLngend,Context mcontext){
        this.latLngend = latLngend;
        this.mcontext = mcontext;
    }
    public List<Address> run() {
        Geocoder geocoder = new Geocoder(mcontext, Locale.getDefault());
        List<Address> list = null;
        try {
            list = geocoder.getFromLocation(latLngend.latitude,latLngend.longitude,1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}
