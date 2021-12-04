package com.Duong.crowdshipping.fragment;

import static com.Duong.crowdshipping.R.drawable.background_btn_activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;


import com.Duong.crowdshipping.R;


public class ActivityFragment extends Fragment {
    Button all_activity, park_activity;
    FragmentContainerView fragment;
    int i = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        all_activity = view.findViewById(R.id.all_activity);
        park_activity = view.findViewById(R.id.park_activity);
        fragment = view.findViewById(R.id.nav_host_fragment);
        setupbtn();
        all_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all_activity_click();
            }
        });
        park_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                park_activity_click(v);
            }
        });
        return view;
    }

    private void park_activity_click(View v) {
        all_activity.setBackgroundColor(Color.TRANSPARENT);
        all_activity.setTextColor(getResources().getColor(R.color.mediumgray));
        park_activity.setBackgroundResource(background_btn_activity);
        park_activity.setTextColor(getResources().getColor(R.color.green));
        NavDirections action = AllActivityFragmentDirections.actionAllActivityFragmentToParkActivityFragment();
        NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        navController.navigate(action);
        park_activity.setClickable(false);
        all_activity.setClickable(true);
    }

    private void all_activity_click() {
        park_activity.setBackgroundColor(Color.TRANSPARENT);
        park_activity.setTextColor(getResources().getColor(R.color.mediumgray));
        all_activity.setBackgroundResource(background_btn_activity);
        all_activity.setTextColor(getResources().getColor(R.color.green));
        NavDirections action = ParkActivityFragmentDirections.actionParkActivityFragmentToAllActivityFragment();
        NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        navController.navigate(action);
        all_activity.setClickable(false);
        park_activity.setClickable(true);
    }

    private void setupbtn() {
        all_activity.setBackgroundResource(background_btn_activity);
        all_activity.setTextColor(getResources().getColor(R.color.green));
    }
}