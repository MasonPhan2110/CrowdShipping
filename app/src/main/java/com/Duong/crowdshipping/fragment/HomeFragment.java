package com.Duong.crowdshipping.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.fragment.app.Fragment;

import com.Duong.crowdshipping.R;
import com.Duong.crowdshipping.adapter.HomeAdapter;
import com.Duong.crowdshipping.Home.FindParkActivity;

public class HomeFragment extends Fragment {
    LinearLayout linearLayout, find_park;
    Button park_now, book_park, park_month;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        linearLayout = view.findViewById(R.id.linear_layout);
        park_now = view.findViewById(R.id.park_now);
        park_month = view.findViewById(R.id.park_month);
        book_park = view.findViewById(R.id.book_park);
        int width = getResources().getDisplayMetrics().widthPixels;
        int size = (width - 30) / 2;
        park_now.setWidth(size);
        park_month.setWidth(size);
        book_park.setWidth(size);
        park_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FindParkActivity.class);
                startActivity(intent);
            }
        });

        wallhome();
        return view;
    }


    private void wallhome() {
        HomeAdapter homeAdapter = new HomeAdapter(getContext(), linearLayout);
        homeAdapter.load_sales();
    }

}