package com.Duong.crowdshipping.Extend;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.Nullable;

import com.Duong.crowdshipping.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.Unit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetDistance  {
    Context mcontext;
    Handler mainHandler = new Handler();
    public GetDistance(Context mcontext){
        this.mcontext = mcontext;
    }
    public Float run(String from, String to) throws InterruptedException, ApiException, IOException {
        String API_KEY = "AIzaSyDLH-Rpep4GMrXNFYFRVFQXMnf3ESV5lQI";
        GeoApiContext.Builder builder = new GeoApiContext.Builder();
        builder.apiKey(API_KEY);

        GeoApiContext geoApiContext = builder.build();
        DistanceMatrix results1 = DistanceMatrixApi.getDistanceMatrix(geoApiContext,
                new String[]{"số 2 ngõ 11 đường 800A Nghĩa Đô Cầu Giấy Hà Nội"}, new String[]{"Số 1 Đại Cồ Việt"}).units(Unit.METRIC).await();
        DistanceMatrixRow[] distanceMatrixRows = results1.rows;


        String str =  Arrays.toString(distanceMatrixRows[0].elements);
        String result = str.substring(str.indexOf("=") + 1, str.indexOf("km"));
        Float distance = Float.parseFloat(result);
        Log.d("Distanceeeeeeeeeeeeeee", String.valueOf(distance));
        return distance;
    }
}
