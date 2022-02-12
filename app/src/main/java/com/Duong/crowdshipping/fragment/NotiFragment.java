package com.Duong.crowdshipping.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.Duong.crowdshipping.R;
import com.Duong.crowdshipping.adapter.HomeAdapter;
import com.Duong.crowdshipping.adapter.NotiAdapter;
import com.Duong.crowdshipping.model.Noti;
import com.Duong.crowdshipping.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class NotiFragment extends Fragment {
    RecyclerView recycle_view;
    SwipeRefreshLayout refresh;
    LinearLayoutManager linearLayoutManager;
    List<Noti> mNoti = new ArrayList<>();
    NotiAdapter notiAdapter;
    FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noti, container, false);
        recycle_view = view.findViewById(R.id.recycle_view);
        refresh = view.findViewById(R.id.refresh);

        recycle_view = view.findViewById(R.id.recycle_view);
        recycle_view.setHasFixedSize(true);
        refresh = view.findViewById(R.id.refresh);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recycle_view.setLayoutManager(linearLayoutManager);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mNoti.clear();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Post");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                            Noti noti = dataSnapshot.getValue(Noti.class);
                            mNoti.add(noti);
                        }
                        Collections.reverse(mNoti);
                        recycle_view.getAdapter().notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                refresh.setRefreshing(false);
            }
        });
        loadNoti();
        return view;
    }

    private void loadNoti() {
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Notification").child(fuser.getUid());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mNoti.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Noti noti = dataSnapshot.getValue(Noti.class);
                    mNoti.add(noti);

                }
                Collections.reverse(mNoti);
                notiAdapter = new NotiAdapter(getContext(), mNoti);
                recycle_view.setAdapter(notiAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}