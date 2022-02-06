package com.Duong.crowdshipping.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Duong.crowdshipping.R;
import com.Duong.crowdshipping.model.Post;
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
    Post post;
    public DetailPostAdapter(LinearLayout linearLayout, Context mContext, ArrayList<SliderData> sliderDataArrayList, Post post){
        this.mContext = mContext;
        this.linearLayout = linearLayout;
        this.sliderDataArrayList = sliderDataArrayList;
        this.post = post;
    }
    public void loadDetailPost(int width){
        inflater = LayoutInflater.from(mContext);
        SliderView sliderView;
        TextView textView,des;
        View view = inflater.inflate(R.layout.item_post_home_header, linearLayout, false);
        sliderView = view.findViewById(R.id.slider);
        sliderView.getLayoutParams().height = width;
        textView = view.findViewById(R.id.text);
        textView.setText("Loại mặt hàng: "+post.getType().split("-")[1]);
        if(sliderDataArrayList.size()!=0){
            SliderAdapter adapter = new SliderAdapter( sliderDataArrayList);
            sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
            sliderView.setSliderAdapter(adapter);
            sliderView.setInfiniteAdapterEnabled(false);
            sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        }
        View body = inflater.inflate(R.layout.body_detail_post, linearLayout, false);
        des = body.findViewById(R.id.description);
        des.setText(des.getText()+post.getDescription());
        linearLayout.addView(view);
        linearLayout.addView(body);
    }
}
