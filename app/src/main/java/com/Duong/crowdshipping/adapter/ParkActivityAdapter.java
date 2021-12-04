package com.Duong.crowdshipping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Duong.crowdshipping.R;

import java.util.List;

public class ParkActivityAdapter {
    Context mcontext;
    LayoutInflater inflater;
    LinearLayout linearLayout;
    String header;
    List<String> list;
    private Boolean collapse = true;

    public ParkActivityAdapter(Context mcontext, LinearLayout linearLayout, String header, List<String> list) {
        this.mcontext = mcontext;
        this.linearLayout = linearLayout;
        this.header = header;
        this.list = list;
    }

    public void load() {
        inflater = LayoutInflater.from(mcontext);
        RelativeLayout rootView;
        TextView header_title;
        ImageView img_arrow;
        View viewheader = inflater.inflate(R.layout.section_header, linearLayout, false);
        header_title = viewheader.findViewById(R.id.header_title);
        img_arrow = viewheader.findViewById(R.id.img_arrow);
        rootView = viewheader.findViewById(R.id.rootView);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapse = !collapse;
                img_arrow.setImageResource(
                        collapse ? R.drawable.ic_baseline_keyboard_arrow_down_24 : R.drawable.ic_baseline_keyboard_arrow_up_24
                );
                if (!collapse) {

                    if(list.size() == 0){
                        TextView itemview;
                        View view = inflater.inflate(R.layout.section_item, linearLayout, false);
                        itemview = view.findViewById(R.id.item);
                        itemview.setText("Có vẻ bạn chưa đặt chỗ đỗ xe nào");
                        linearLayout.addView(view,  1);
                    }else {
                        for (String item : list) {
                            TextView itemview;
                            View view = inflater.inflate(R.layout.section_item, linearLayout, false);
                            itemview = view.findViewById(R.id.item);
                            itemview.setText(item);
                            linearLayout.addView(view, 1);
                        }
                    }
                } else {
                    if(list.size() == 0){
                        linearLayout.removeViewAt( 1);
                    }else {
                        for (int i = 0; i < list.size(); i++) {
                            linearLayout.removeViewAt(1);
                        }
                    }
                }
            }
        });
        header_title.setText(header);

        linearLayout.addView(viewheader);
    }
}
