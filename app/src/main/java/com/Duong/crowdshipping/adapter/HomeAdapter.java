package com.Duong.crowdshipping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

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
            headerViewHolder.sliderView.startAutoCycle();
        }else if(holder instanceof ViewHolder){
            final ViewHolder itemHolder = (ViewHolder) holder;
            Post post = mPost.get(position-1);
            String img = post.getLinkImage().get("Image0").toString();
            holder.setIsRecyclable(false);
            Glide.with(mcontext).load(img).into(itemHolder.image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return viewType.Header;
        }
        return viewType.Normal;
    }

    @Override
    public int getItemCount() {
        return 0;
    }
    private class viewType{
        public static  final int Header =1;
        public static final int Normal = 2;
        public static final int Footer = 3;
    }
    public  class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView type,from,to,time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            type = itemView.findViewById(R.id.type);
            from = itemView.findViewById(R.id.from);
            to = itemView.findViewById(R.id.to);
            time = itemView.findViewById(R.id.time);

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
