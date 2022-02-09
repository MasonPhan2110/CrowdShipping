package com.Duong.crowdshipping.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.Duong.crowdshipping.R;
import com.Duong.crowdshipping.adapter.PostSentAdapter;

import java.util.ArrayList;
import java.util.List;


public class PostSentFragment extends Fragment {
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_park_activity, container, false);
        recyclerView = view.findViewById(R.id.recycle_view);
        return view;
    }

    private void setup_fragment() {

    }
}