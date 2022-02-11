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
import com.Duong.crowdshipping.model.Noti;

import java.util.List;

public class NotiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<Noti> mNoti;
    public NotiAdapter(Context mContext, List<Noti> mNoti){
        this.mContext = mContext;
        this.mNoti = mNoti;

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_noti, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mNoti.size();
    }
    public  class ViewHolder extends RecyclerView.ViewHolder{
        ImageView post_profile;
        TextView noti;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post_profile = itemView.findViewById(R.id.post_profile);
            noti = itemView.findViewById(R.id.noti);
        }
    }
}
