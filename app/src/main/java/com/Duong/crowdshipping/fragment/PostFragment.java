package com.Duong.crowdshipping.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.Duong.crowdshipping.Activity.PostActivity;
import com.Duong.crowdshipping.R;

public class PostFragment extends Fragment {
    LinearLayout clothes,devices,office_devices;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        clothes = view.findViewById(R.id.clothes);
        devices = view.findViewById(R.id.devices);
        office_devices = view.findViewById(R.id.office_devices);
        clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItem("clothes");
            }
        });
        devices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItem("devices");
            }
        });
        office_devices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItem("office_devices");
            }
        });
        return view;
    }

    private void clickItem(String type) {
        Intent intent = new Intent(getContext(), PostActivity.class);
        intent.putExtra("type",type);
        startActivity(intent);
    }
}