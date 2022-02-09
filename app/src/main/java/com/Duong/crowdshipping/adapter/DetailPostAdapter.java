package com.Duong.crowdshipping.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.Duong.crowdshipping.R;
import com.Duong.crowdshipping.model.Post;
import com.Duong.crowdshipping.model.SliderData;
import com.Duong.crowdshipping.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public DetailPostAdapter(LinearLayout linearLayout, Context mContext, ArrayList<SliderData> sliderDataArrayList, Post post){
        this.mContext = mContext;
        this.linearLayout = linearLayout;
        this.sliderDataArrayList = sliderDataArrayList;
        this.post = post;
    }
    public void loadDetailPost(int width){
        inflater = LayoutInflater.from(mContext);
        SliderView sliderView;
        TextView textView,des, from, to, ship, username;
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
        from = body.findViewById(R.id.from);
        to = body.findViewById(R.id.to);
        ship = body.findViewById(R.id.ship);
        username = body.findViewById(R.id.username);
        des.setText(des.getText()+post.getDescription());
        from.setText("Từ: đường "+post.getAddressFrom().get("Streets")+", phường "+post.getAddressFrom().get("Wards")+", quận "+post.getAddressFrom().get("District")+", "+post.getAddressFrom().get("City"));
        to.setText("Đến: đường "+post.getAddressTo().get("Streets")+", phường " +post.getAddressTo().get("Wards")+", quận "+post.getAddressTo().get("District")+", "+post.getAddressTo().get("City"));
        ship.setText("Phí ship: "+post.getShip());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(post.getCreateID());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                username.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        linearLayout.addView(view);
        linearLayout.addView(body);
    }
}
