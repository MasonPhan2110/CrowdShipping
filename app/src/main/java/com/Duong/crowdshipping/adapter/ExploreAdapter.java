package com.Duong.crowdshipping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Duong.crowdshipping.R;

public class ExploreAdapter {
    Context mcontext;
    LayoutInflater inflater;
    LinearLayout linearLayout;
    public ExploreAdapter(Context mcontext, LinearLayout linearLayout){
        this.mcontext = mcontext;
        this.linearLayout = linearLayout;
    }
    public void loadItem(){
        inflater = LayoutInflater.from(mcontext);
        for(int i = 0; i<30;i++){
            View view = inflater.inflate(R.layout.item_explore,linearLayout,false);
            ImageView image;
            TextView title;
            image = view.findViewById(R.id.image);
            title = view.findViewById(R.id.title);

            linearLayout.addView(view);
        }
    }
}
