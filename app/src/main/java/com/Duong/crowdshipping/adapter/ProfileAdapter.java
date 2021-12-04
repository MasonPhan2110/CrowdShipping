package com.Duong.crowdshipping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Duong.crowdshipping.R;

public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    public ProfileAdapter(Context mContext){
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 1){
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_header_profile, parent, false);
            return new HeaderViewHolder(view);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_profile,parent,false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return viewtype.Header;
        }
        return viewtype.Item;
    }
    private class viewtype{
        public static final int Header = 1;
        public static final int Item = 2;
    }
    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
