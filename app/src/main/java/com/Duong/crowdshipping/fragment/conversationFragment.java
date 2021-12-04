package com.Duong.crowdshipping.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Duong.crowdshipping.R;
import com.Duong.crowdshipping.adapter.ParkAdapter;
import com.Duong.crowdshipping.model.Chatlist;
import com.Duong.crowdshipping.model.Parks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class conversationFragment extends Fragment {
    private RecyclerView recyclerView;
    private ParkAdapter parkAdapter;
    private List<Parks> mParks;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    private List<Chatlist> mchatList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mchatList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mchatList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chatlist chatlist = dataSnapshot.getValue(Chatlist.class);
                    mchatList.add(chatlist);
                }
                chatList();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
    private void chatList() {
        mParks = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Parking lot").child("Hanoi");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mParks.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Parks parks = dataSnapshot.getValue(Parks.class);
                    for (Chatlist chatlist : mchatList){
                        if (parks.getID().equals(chatlist.getId())){
                            mParks.add(parks);
                        }
                    }
                }
                parkAdapter = new ParkAdapter(getContext(),mParks, true);
                recyclerView.setAdapter(parkAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}