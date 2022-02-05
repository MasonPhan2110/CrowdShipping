package com.Duong.crowdshipping.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Duong.crowdshipping.R;
import com.Duong.crowdshipping.model.SliderData;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class DetailPostAdapter {
    LinearLayout linearLayout;
    Context mContext;
    LayoutInflater inflater;
    ArrayList<SliderData> sliderDataArrayList;
    String type;
    public DetailPostAdapter(LinearLayout linearLayout, Context mContext, ArrayList<SliderData> sliderDataArrayList, String type){
        this.mContext = mContext;
        this.linearLayout = linearLayout;
        this.sliderDataArrayList = sliderDataArrayList;
        this.type = type;
    }
    public void loadDetailPost(int width){
        inflater = LayoutInflater.from(mContext);
        SliderView sliderView;
        TextView textView;
        View view = inflater.inflate(R.layout.item_post_home_header, linearLayout, false);
        sliderView = view.findViewById(R.id.slider);
        sliderView.getLayoutParams().height = width;
        textView = view.findViewById(R.id.text);
        textView.setText("Loại mặt hàng: "+type.split("-")[1]);
        if(sliderDataArrayList.size()!=0){
            SliderAdapter adapter = new SliderAdapter( sliderDataArrayList);
            sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
            sliderView.setSliderAdapter(adapter);
            sliderView.setInfiniteAdapterEnabled(false);
            sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
            if(sliderDataArrayList.size() ==1){
//                sliderView.setOffscreenPageLimit(0);
            }
        }
        linearLayout.addView(view);
    }
}
