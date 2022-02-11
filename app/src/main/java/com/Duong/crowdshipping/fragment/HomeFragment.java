package com.Duong.crowdshipping.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.Duong.crowdshipping.Activity.HomeActivity;
import com.Duong.crowdshipping.Controller.EndlessRecycleViewScrollListener;
import com.Duong.crowdshipping.R;
import com.Duong.crowdshipping.adapter.HomeAdapter;
import com.Duong.crowdshipping.adapter.SliderAdapter;
import com.Duong.crowdshipping.model.Post;
import com.Duong.crowdshipping.model.SliderData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment implements HomeActivity.HomeActivityListener {
    private EndlessRecycleViewScrollListener scrollListener;
    String url1 = "https://thumbs.dreamstime.com/z/crowdshipping-smartphone-screen-refreshing-background-209089346.jpg";
    String url2 = "https://thumbs.dreamstime.com/b/crowdshipping-text-truck-driving-keyboard-key-conceptual-d-rendering-computer-209759394.jpg";
    String url3 = "https://slideplayer.com/slide/13242547/79/images/8/Crowdshipping+and+its+integration+with+Physical+Internet.jpg";
    LinearLayout explore_wrap, linearLayout;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recycle_view;
    SwipeRefreshLayout refresh;
    List<Post> mPost = new ArrayList<>();
    HomeAdapter homeAdapter;
    ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();
    ProgressBar pBar;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ProgressBar pgsBar = view.findViewById(R.id.pBar);
        sliderDataArrayList.add(new SliderData(url1));
        sliderDataArrayList.add(new SliderData(url2));
        sliderDataArrayList.add(new SliderData(url3));

        recycle_view = view.findViewById(R.id.recycle_view);
        recycle_view.setHasFixedSize(true);
        refresh = view.findViewById(R.id.refresh);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recycle_view.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecycleViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
            }
        };
        wallHome(pgsBar);
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
                            if(!user.getUid().equals(post.getCreateID())&&post.getShipper()==null){
                                mPost.add(post);
                            }
                        }
                        Collections.reverse(mPost);
                        recycle_view.getAdapter().notifyDataSetChanged();
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

    private void wallHome(ProgressBar pgsBar) {
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Post");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mPost.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    if(!user.getUid().equals(post.getCreateID())&&post.getShipper()==null){
                        mPost.add(post);
                    }
                }
                pgsBar.setVisibility(View.GONE);
                Collections.reverse(mPost);
                homeAdapter = new HomeAdapter(getContext(), mPost, sliderDataArrayList);
                recycle_view.setAdapter(homeAdapter);
                recycle_view.addOnScrollListener(scrollListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void loadNextDataFromApi(final int page) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Post");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Post> postList = new ArrayList<>();
                postList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Post post = dataSnapshot.getValue(Post.class);
                    if(!user.getUid().equals(post.getCreateID())&&post.getShipper()==null){
                        postList.add(post);
                    }
                }
                Collections.reverse(postList);
                for(int i =0;i<postList.size();i++){
                    mPost.add(postList.get(i));
                }
                homeAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void backpress() {
        if(linearLayoutManager.findFirstVisibleItemPosition() != 0)
        {
            RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(getContext()){
                @Override
                protected int getVerticalSnapPreference() {
                    return super.getVerticalSnapPreference();
                }
            };
            smoothScroller.setTargetPosition(0);
            linearLayoutManager.startSmoothScroll(smoothScroller);
        }else{
            getActivity().finishAffinity();
        }
    }
}