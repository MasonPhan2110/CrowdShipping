package com.Duong.crowdshipping.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Duong.crowdshipping.Activity.CreatePostActivity;
import com.Duong.crowdshipping.Activity.PostActivity;
import com.Duong.crowdshipping.R;

import java.util.List;

public class PostAdapter {
    Context mcontext;
    LayoutInflater inflater;
    LinearLayout linearLayout;
    List item;
    String type;
    public PostAdapter(Context mcontext, LinearLayout linearLayout, List item, String type){
        this.mcontext = mcontext;
        this.linearLayout = linearLayout;
        this.item = item;
        this.type = type;
    }
    public void load(){
        inflater = LayoutInflater.from(mcontext);
        for(int i = 0; i<item.size();i++){
            View view = inflater.inflate(R.layout.item_post,linearLayout,false);
            TextView textView = view.findViewById(R.id.title);
            RelativeLayout relative_layout = view.findViewById(R.id.relative_layout);
            textView.setText(item.get(i).toString());
            int finalI = i;
            relative_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mcontext, CreatePostActivity.class);
                    intent.putExtra("type", type);
                    intent.putExtra("item", item.get(finalI).toString());
                    mcontext.startActivity(intent);
                }
            });

            linearLayout.addView(view);
        }
    }
}
