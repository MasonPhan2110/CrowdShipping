package com.Duong.crowdshipping.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Duong.crowdshipping.Activity.DetailPostActivity;
import com.Duong.crowdshipping.R;
import com.Duong.crowdshipping.model.Noti;
import com.Duong.crowdshipping.model.Post;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
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

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Noti noti = mNoti.get(position);
        Log.d("noticount", "onDataChange: "+noti.getSeen());
//        if(!noti.getSeen()){
//            viewHolder.relativeLayout.setBackgroundColor(R.color.lightgray);
//        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Post").child(noti.getPostID());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                Glide.with(mContext).load(post.getLinkImage().get("Image0")).into(viewHolder.post_profile);
                viewHolder.noti.setText(noti.getMSG()+" từ phường "+post.getAddressFrom().get("Wards")+", quận "+post.getAddressFrom().get("District")+", "+post.getAddressFrom().get("City")
                +" đến phường "+post.getAddressTo().get("Wards")+", quận "+post.getAddressTo().get("District")+", "+post.getAddressTo().get("City"));
                viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Notification").child(post.getCreateID()).child(noti.getNotiID());
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen",true);
                        reference1.updateChildren(hashMap);
                        Intent intent = new Intent(mContext, DetailPostActivity.class);
                        intent.putExtra("Post", post);
                        intent.putExtra("Type","Sent");
                        mContext.startActivity(intent);
                        Toast.makeText(mContext, "click", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mNoti.size();
    }
    public  class ViewHolder extends RecyclerView.ViewHolder{
        ImageView post_profile;
        TextView noti;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post_profile = itemView.findViewById(R.id.post_profile);
            noti = itemView.findViewById(R.id.noti);
            relativeLayout = itemView.findViewById(R.id.relativelayout);
        }
    }
}
