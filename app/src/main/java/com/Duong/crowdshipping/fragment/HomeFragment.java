package com.Duong.crowdshipping.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.fragment.app.Fragment;

import com.Duong.crowdshipping.R;
import com.Duong.crowdshipping.adapter.ExploreAdapter;
import com.Duong.crowdshipping.adapter.HomeAdapter;
import com.Duong.crowdshipping.Home.FindParkActivity;
import com.Duong.crowdshipping.adapter.SliderAdapter;
import com.Duong.crowdshipping.model.SliderData;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    String url1 = "https://thumbs.dreamstime.com/z/crowdshipping-smartphone-screen-refreshing-background-209089346.jpg";
    String url2 = "https://thumbs.dreamstime.com/b/crowdshipping-text-truck-driving-keyboard-key-conceptual-d-rendering-computer-209759394.jpg";
    String url3 = "https://slideplayer.com/slide/13242547/79/images/8/Crowdshipping+and+its+integration+with+Physical+Internet.jpg";
    LinearLayout explore_wrap, linearLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();
        SliderView sliderView = view.findViewById(R.id.slider);
        sliderDataArrayList.add(new SliderData(url1));
        sliderDataArrayList.add(new SliderData(url2));
        sliderDataArrayList.add(new SliderData(url3));
        SliderAdapter adapter = new SliderAdapter( sliderDataArrayList);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
        linearLayout = view.findViewById(R.id.linear_layout);
        explore_wrap = view.findViewById(R.id.explore_wrap);
        setupExplore();
        wallHome();
        return view;
    }

    private void setupExplore() {
        ExploreAdapter exploreAdapter = new ExploreAdapter(getContext(), explore_wrap);
        exploreAdapter.loadItem();
    }
    private void wallHome() {
        HomeAdapter homeAdapter = new HomeAdapter(getContext(), linearLayout);
        homeAdapter.load_sales();
    }

}