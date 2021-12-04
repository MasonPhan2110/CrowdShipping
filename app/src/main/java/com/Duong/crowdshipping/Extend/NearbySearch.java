package com.Duong.crowdshipping.Extend;

import android.content.Context;

import com.Duong.crowdshipping.R;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NearbySearch {
    Context mcontext;

    public NearbySearch(Context mcontext) {
        this.mcontext = mcontext;

    }

    public PlacesSearchResponse run(LatLng location) {

        PlacesSearchResponse request = new PlacesSearchResponse();
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(mcontext.getResources().getString(R.string.google_maps_key))
                .build();
        try {
            request = PlacesApi.nearbySearchQuery(context, location)
                    .radius(12000)
                    .keyword("Bãi đỗ xe")
                    .language("vi")
                    .type(PlaceType.PARKING)
                    .await();
        } catch (ApiException | IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            return request;
        }
    }

    public List<GeocodingResult> getLatlng(String address) {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(mcontext.getResources().getString(R.string.google_maps_key))
                .build();
        List<GeocodingResult> result = new ArrayList<>();
        try {
            GeocodingResult[] results = GeocodingApi.geocode(context, address)
                    .language("vi")
                    .await();
            for (int i = 0; i < results.length; i++) {
                result.add(results[i]);
            }
        } catch (ApiException | IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    public PlaceDetails name(String placeID) {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(mcontext.getResources().getString(R.string.google_maps_key))
                .build();
        PlaceDetails placeDetails = null;
        try {
            placeDetails = PlacesApi.placeDetails(context, placeID).language("vi").await();
        } catch (ApiException | IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            return placeDetails;
        }
    }
}
