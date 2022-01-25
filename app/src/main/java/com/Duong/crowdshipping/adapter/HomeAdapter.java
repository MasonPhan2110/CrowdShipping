package com.Duong.crowdshipping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.Duong.crowdshipping.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeAdapter {
    Context mcontext;
    LayoutInflater inflater;
    LinearLayout linearLayout;
    DatabaseReference reference;
    public HomeAdapter(Context mcontext, LinearLayout linearLayout){
        this.mcontext = mcontext;
        this.linearLayout = linearLayout;
    }
    public void load_sales(){
        inflater = LayoutInflater.from(mcontext);
        reference = FirebaseDatabase.getInstance().getReference("Post");
        reference.child("clothes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("devices").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        for(int i = 0; i<30;i++){
            View view = inflater.inflate(R.layout.homeitem,linearLayout,false);
            ImageView sale1, sale2;
            TextView name_sale1, name_sale2, time1, time2;
            LinearLayout linear1 , linear2;
            sale1 = view.findViewById(R.id.sale1);
            sale2 = view.findViewById(R.id.sale2);
            name_sale1 = view.findViewById(R.id.name_sale1);
            name_sale2 = view.findViewById(R.id.name_sale2);
            time1 = view.findViewById(R.id.time1);
            time2 = view.findViewById(R.id.time2);
            linear1 = view.findViewById(R.id.linear1);
            linear2 = view.findViewById(R.id.linear2);

            final int height = mcontext.getResources().getDisplayMetrics().heightPixels;
            final int width = mcontext.getResources().getDisplayMetrics().widthPixels;
            float dp = 10;
            float density = mcontext.getResources().getDisplayMetrics().density;
            int pixel = (int)(dp * density);
            int size = (width - 3 * pixel)/2 ;
            linear1.getLayoutParams().width = size;
            linear2.getLayoutParams().width = size;
            sale1.getLayoutParams().height = size;
            sale2.getLayoutParams().height = size;
            linearLayout.addView(view);
        }
    }
}
