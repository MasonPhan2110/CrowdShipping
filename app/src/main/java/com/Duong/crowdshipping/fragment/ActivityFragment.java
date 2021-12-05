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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;


import com.Duong.crowdshipping.Home.HomeActivity;
import com.Duong.crowdshipping.R;

import java.util.ArrayList;
import java.util.List;


public class ActivityFragment extends Fragment {
    Button all_post, recieved, notRecieved;
    int i = 0;
    ViewPager view_pager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        all_post = view.findViewById(R.id.all_post);
        recieved = view.findViewById(R.id.recieved);
        notRecieved = view.findViewById(R.id.notRecieved);
        view_pager = view.findViewById(R.id.view_pager);
        setupViewPager(view_pager);
        setupbtn();
        all_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all_activity_click();
            }
        });
        recieved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                park_activity_click();
            }
        });
        notRecieved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notRecieved_click();
            }
        });
        view_pager.addOnPageChangeListener(onPageChangeListener);
        return view;
    }

    private void park_activity_click() {
        all_post.setBackgroundColor(Color.TRANSPARENT);
        all_post.setTextColor(getResources().getColor(R.color.mediumgray));
        recieved.setBackgroundResource(background_btn_activity);
        recieved.setTextColor(getResources().getColor(R.color.green));
        notRecieved.setBackgroundColor(Color.TRANSPARENT);
        notRecieved.setTextColor(getResources().getColor(R.color.mediumgray));
        view_pager.setCurrentItem(1);
        recieved.setClickable(false);
        all_post.setClickable(true);
        notRecieved.setClickable(true);
    }

    private void all_activity_click() {
        recieved.setBackgroundColor(Color.TRANSPARENT);
        recieved.setTextColor(getResources().getColor(R.color.mediumgray));
        all_post.setBackgroundResource(background_btn_activity);
        all_post.setTextColor(getResources().getColor(R.color.green));
        notRecieved.setBackgroundColor(Color.TRANSPARENT);
        notRecieved.setTextColor(getResources().getColor(R.color.mediumgray));
        view_pager.setCurrentItem(0);
        all_post.setClickable(false);
        recieved.setClickable(true);
        notRecieved.setClickable(true);
    }
    private void notRecieved_click(){
        recieved.setBackgroundColor(Color.TRANSPARENT);
        recieved.setTextColor(getResources().getColor(R.color.mediumgray));
        notRecieved.setBackgroundResource(background_btn_activity);
        notRecieved.setTextColor(getResources().getColor(R.color.green));
        all_post.setBackgroundColor(Color.TRANSPARENT);
        all_post.setTextColor(getResources().getColor(R.color.mediumgray));
        view_pager.setCurrentItem(2);
        all_post.setClickable(true);
        recieved.setClickable(true);
        notRecieved.setClickable(false);
    }

    private void setupbtn() {
        all_post.setBackgroundResource(background_btn_activity);
        all_post.setTextColor(getResources().getColor(R.color.green));
    }
    private void setupViewPager(ViewPager view_pager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFrag(new AllActivityFragment(), "Trang chủ");
        adapter.addFrag(new ParkActivityFragment(), "Quản lý tin");
        adapter.addFrag(new NotRecievedFragment(),"Đăng tin");
        view_pager.setAdapter(adapter);
    }
    static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add("");
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position){
                case 0:
                    recieved.setBackgroundColor(Color.TRANSPARENT);
                    recieved.setTextColor(getResources().getColor(R.color.mediumgray));
                    all_post.setBackgroundResource(background_btn_activity);
                    all_post.setTextColor(getResources().getColor(R.color.green));
                    notRecieved.setBackgroundColor(Color.TRANSPARENT);
                    notRecieved.setTextColor(getResources().getColor(R.color.mediumgray));
                    all_post.setClickable(false);
                    recieved.setClickable(true);
                    notRecieved.setClickable(true);
                    break;
                case 1:
                    all_post.setBackgroundColor(Color.TRANSPARENT);
                    all_post.setTextColor(getResources().getColor(R.color.mediumgray));
                    recieved.setBackgroundResource(background_btn_activity);
                    recieved.setTextColor(getResources().getColor(R.color.green));
                    notRecieved.setBackgroundColor(Color.TRANSPARENT);
                    notRecieved.setTextColor(getResources().getColor(R.color.mediumgray));
                    recieved.setClickable(false);
                    all_post.setClickable(true);
                    notRecieved.setClickable(true);
                    break;
                case 2:
                    recieved.setBackgroundColor(Color.TRANSPARENT);
                    recieved.setTextColor(getResources().getColor(R.color.mediumgray));
                    notRecieved.setBackgroundResource(background_btn_activity);
                    notRecieved.setTextColor(getResources().getColor(R.color.green));
                    all_post.setBackgroundColor(Color.TRANSPARENT);
                    all_post.setTextColor(getResources().getColor(R.color.mediumgray));
                    all_post.setClickable(true);
                    recieved.setClickable(true);
                    notRecieved.setClickable(false);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}