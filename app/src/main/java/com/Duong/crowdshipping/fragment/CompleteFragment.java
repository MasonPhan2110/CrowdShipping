package com.Duong.crowdshipping.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Duong.crowdshipping.R;
import com.Duong.crowdshipping.adapter.SentPostAdapter;
import com.Duong.crowdshipping.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CompleteFragment extends Fragment {

    List<Post> mPost = new ArrayList<>();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    SentPostAdapter postSentAdapter;
    RecyclerView recycleView;
    SwipeRefreshLayout refresh;
    LinearLayoutManager linearLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_complete, container, false);
        recycleView = view.findViewById(R.id.recycle_view);
        refresh = view.findViewById(R.id.refresh);

        recycleView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recycleView.setLayoutManager(linearLayoutManager);
        loadPost();
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPost.clear();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Post");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                            Post post = dataSnapshot.getValue(Post.class);
                            if(user.getUid().equals(post.getShipper()) && post.getStatus().equals("3")){
                                mPost.add(post);
                            }
                        }
                        Collections.reverse(mPost);
                        recycleView.getAdapter().notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                refresh.setRefreshing(false);
            }
        });
        return view;
    }

    private void loadPost() {
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Post");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mPost.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    if(user.getUid().equals(post.getShipper())||user.getUid().equals(post.getCreateID()) && post.getStatus().equals("3")){
                        mPost.add(post);
                    }
                }
                Collections.reverse(mPost);
                postSentAdapter = new SentPostAdapter(getContext(), mPost, "Complete" );
                recycleView.setAdapter(postSentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}