package com.Duong.crowdshipping.Extend;

import android.content.Context;
import android.util.Log;

import com.Duong.crowdshipping.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.RoadsApi;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.SnappedPoint;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.maps.errors.ApiException;

public class GetDirection {
    Context mcontext;
    LatLng origin,dest;
    com.google.maps.model.LatLng[] latLngList;
    public GetDirection(Context mcontext,LatLng origin,LatLng dest,com.google.maps.model.LatLng[] latLngList){
        this.mcontext = mcontext;
        this.origin = origin;
        this.dest = dest;
        this.latLngList = latLngList;
    }
    public DirectionsResult run(){
        DirectionsResult directionsResult = new DirectionsResult();
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(mcontext.getResources().getString(R.string.google_maps_key))
                .build();
        String originloc = String.valueOf(origin.latitude)+","+String.valueOf(origin.longitude);
        String destLoc = String.valueOf(dest.latitude)+","+String.valueOf(dest.longitude);
        try {
            directionsResult = DirectionsApi.getDirections(context,originloc,destLoc).mode(TravelMode.DRIVING).await();
        }catch (ApiException | IOException | InterruptedException e){
            e.printStackTrace();
        }finally {
            return directionsResult;
        }
    }
    public List<LatLng> snaptoroad(){
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(mcontext.getResources().getString(R.string.google_maps_key))
                .build();
        List<LatLng> list = new ArrayList<>();
        try {
            SnappedPoint[] snappedPoints = RoadsApi.snapToRoads(context,true, latLngList).await();
            for(int i=0;i<snappedPoints.length;i++){
                list.add(new LatLng(snappedPoints[i].location.lat,snappedPoints[i].location.lng));
                Log.d("MapFragment", "list: "+list.get(i));
            }
        } catch (ApiException | IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }
}
