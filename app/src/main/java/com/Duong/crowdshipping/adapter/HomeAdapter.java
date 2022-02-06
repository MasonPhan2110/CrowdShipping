package com.Duong.crowdshipping.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Duong.crowdshipping.Activity.DetailPostActivity;
import com.Duong.crowdshipping.R;
import com.Duong.crowdshipping.model.Post;
import com.Duong.crowdshipping.model.SliderData;
import com.android.volley.Header;
import com.bumptech.glide.Glide;
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

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mcontext;
    private List<Post> mPost;
    ArrayList<SliderData> sliderDataArrayList;

    public HomeAdapter(Context mcontext, List<Post> mPost, ArrayList<SliderData> sliderDataArrayList){
        this.mcontext = mcontext;
        this.mPost = mPost;
        this.sliderDataArrayList = sliderDataArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 1){
            View view = LayoutInflater.from(mcontext).inflate(R.layout.item_post_home_header, parent, false);
            return new HeaderViewHolder(view);
        }else{
            View view = LayoutInflater.from(mcontext).inflate(R.layout.item_post_home, parent, false);
            return  new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeaderViewHolder){
            final HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            SliderAdapter adapter = new SliderAdapter( sliderDataArrayList);
            headerViewHolder.sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
            headerViewHolder.sliderView.setSliderAdapter(adapter);
            headerViewHolder.sliderView.setScrollTimeInSec(3);
            headerViewHolder.sliderView.setAutoCycle(true);
            headerViewHolder.sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINDEPTHTRANSFORMATION);
            headerViewHolder.sliderView.setIndicatorAnimation(IndicatorAnimationType.SWAP);
            headerViewHolder.sliderView.startAutoCycle();
        }else if(holder instanceof ViewHolder){
            final ViewHolder itemHolder = (ViewHolder) holder;
            Post post = mPost.get(position-1);
            String img = post.getLinkImage().get("Image0").toString();
            holder.setIsRecyclable(false);
            String[] type = post.getType().split("-");
            String[] time = post.getTime().split("_");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            String[] currentTime = currentDateandTime.split("_");
            String timeSubtract = " ";
            if(time[0] == currentTime[0]){
                SimpleDateFormat dateFormat = new SimpleDateFormat("HHmmss");
                try {
                    Date datePost = dateFormat.parse(time[1]);
                    Date datenow = dateFormat.parse(currentTime[1]);
                    long diff = datenow.getTime() - datePost.getTime();
                    int timeinSecond = (int) (diff/1000);
                    int hours, minutes, seconds;
                    hours = timeinSecond / 3600;
                    timeinSecond = timeinSecond - (hours * 3600);
                    minutes = timeinSecond / 60;
                    timeinSecond = timeinSecond - (minutes * 60);
                    seconds = timeinSecond;
                    if(hours == 0){
                        if(minutes == 0){
                            timeSubtract="Vừa xong";
                        }else{
                            timeSubtract = minutes + " m";
                        }
                    }else{
                        timeSubtract = hours + " h";
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else{
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                try{
                    Date datePost = dateFormat.parse(time[0]);
                    Date datenow = dateFormat.parse(currentTime[0]);
                    Log.d("Timenow", datenow+"-"+datePost);
                    long diff = datenow.getTime() - datePost.getTime();
                    int timeinSecond = (int) (diff/1000);
                    int hours, day;
                    hours = timeinSecond / 3600;
                    day = hours/24;
                    if(day<=1){
                        timeSubtract = day+" day";
                    }
                    else{
                        timeSubtract = day+" days";
                    }
                }catch(ParseException e) {
                    e.printStackTrace();
                }
            }
            Glide.with(mcontext).load(img).into(itemHolder.image);
            itemHolder.from.setText("Từ: "+post.getAddressFrom());
            itemHolder.to.setText("Đến: " +post.getAddressTo());
            itemHolder.type.setText("Loại hàng hóa: "+type[1]);
            itemHolder.time.setText(timeSubtract);
            itemHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mcontext, DetailPostActivity.class);
                    intent.putExtra("ImageLink", post.getLinkImage());
                    intent.putExtra("Type", post.getType());
                    intent.putExtra("Post", (Serializable) post);
                    mcontext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(position == 0){
            Log.d("MPOST", String.valueOf(position));
            return viewType.Header;
        }
        return viewType.Normal;
    }

    @Override
    public int getItemCount() {
        return mPost.size()+1;
    }
    private class viewType{
        public static  final int Header =1;
        public static final int Normal = 2;
        public static final int Footer = 3;
    }
    public  class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView type,from,to,time;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            type = itemView.findViewById(R.id.type);
            from = itemView.findViewById(R.id.from);
            to = itemView.findViewById(R.id.to);
            time = itemView.findViewById(R.id.time);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
    public class HeaderViewHolder extends  RecyclerView.ViewHolder{
        SliderView sliderView;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            sliderView = itemView.findViewById(R.id.slider);
        }
    }
}
