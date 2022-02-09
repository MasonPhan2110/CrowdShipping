package com.Duong.crowdshipping.fragment;

import static com.Duong.crowdshipping.R.drawable.background_btn_activity;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.Duong.crowdshipping.R;

import java.util.ArrayList;
import java.util.List;


public class ActivityFragment extends Fragment {
    Button all_post, sent, notsend, recieved, completed;
    int i = 0;
    ViewPager view_pager;
    HorizontalScrollView menuScroll;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        all_post = view.findViewById(R.id.all_post);
        sent = view.findViewById(R.id.sent);
        notsend = view.findViewById(R.id.notsend);
        recieved = view.findViewById(R.id.recieved);
        completed = view.findViewById(R.id.completed);
        view_pager = view.findViewById(R.id.view_pager);
        menuScroll = view.findViewById(R.id.menuScroll);
        setupViewPager(view_pager);
        setupbtn();
        all_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all_activity_click();
            }
        });
        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                park_activity_click();
            }
        });
        notsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notsend_click();
            }
        });
        recieved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recieved_click();
            }
        });
        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completed_click();
            }
        });
        view_pager.addOnPageChangeListener(onPageChangeListener);
        return view;
    }

    private void park_activity_click() {
        recieved.setBackgroundColor(Color.TRANSPARENT);
        recieved.setTextColor(getResources().getColor(R.color.mediumgray));
        completed.setBackgroundColor(Color.TRANSPARENT);
        completed.setTextColor(getResources().getColor(R.color.mediumgray));
        all_post.setBackgroundColor(Color.TRANSPARENT);
        all_post.setTextColor(getResources().getColor(R.color.mediumgray));
        sent.setBackgroundResource(background_btn_activity);
        sent.setTextColor(getResources().getColor(R.color.green));
        notsend.setBackgroundColor(Color.TRANSPARENT);
        notsend.setTextColor(getResources().getColor(R.color.mediumgray));
        view_pager.setCurrentItem(1);
        sent.setClickable(false);
        all_post.setClickable(true);
        notsend.setClickable(true);
        recieved.setClickable(true);
        completed.setClickable(true);
    }

    private void all_activity_click() {
        sent.setBackgroundColor(Color.TRANSPARENT);
        sent.setTextColor(getResources().getColor(R.color.mediumgray));
        all_post.setBackgroundResource(background_btn_activity);
        all_post.setTextColor(getResources().getColor(R.color.green));
        notsend.setBackgroundColor(Color.TRANSPARENT);
        notsend.setTextColor(getResources().getColor(R.color.mediumgray));
        recieved.setBackgroundColor(Color.TRANSPARENT);
        recieved.setTextColor(getResources().getColor(R.color.mediumgray));
        completed.setBackgroundColor(Color.TRANSPARENT);
        completed.setTextColor(getResources().getColor(R.color.mediumgray));
        view_pager.setCurrentItem(0);
        all_post.setClickable(false);
        sent.setClickable(true);
        notsend.setClickable(true);
        recieved.setClickable(true);
        completed.setClickable(true);
    }
    private void notsend_click(){
        recieved.setBackgroundColor(Color.TRANSPARENT);
        recieved.setTextColor(getResources().getColor(R.color.mediumgray));
        completed.setBackgroundColor(Color.TRANSPARENT);
        completed.setTextColor(getResources().getColor(R.color.mediumgray));
        sent.setBackgroundColor(Color.TRANSPARENT);
        sent.setTextColor(getResources().getColor(R.color.mediumgray));
        notsend.setBackgroundResource(background_btn_activity);
        notsend.setTextColor(getResources().getColor(R.color.green));
        all_post.setBackgroundColor(Color.TRANSPARENT);
        all_post.setTextColor(getResources().getColor(R.color.mediumgray));
        view_pager.setCurrentItem(2);
        all_post.setClickable(true);
        sent.setClickable(true);
        notsend.setClickable(false);
        recieved.setClickable(true);
        completed.setClickable(true);
    }
    private void recieved_click(){
        sent.setBackgroundColor(Color.TRANSPARENT);
        sent.setTextColor(getResources().getColor(R.color.mediumgray));
        notsend.setBackgroundColor(Color.TRANSPARENT);
        notsend.setTextColor(getResources().getColor(R.color.mediumgray));
        completed.setBackgroundColor(Color.TRANSPARENT);
        completed.setTextColor(getResources().getColor(R.color.mediumgray));
        recieved.setBackgroundResource(background_btn_activity);
        recieved.setTextColor(getResources().getColor(R.color.green));
        all_post.setBackgroundColor(Color.TRANSPARENT);
        all_post.setTextColor(getResources().getColor(R.color.mediumgray));
        view_pager.setCurrentItem(3);
        all_post.setClickable(true);
        sent.setClickable(true);
        notsend.setClickable(true);
        recieved.setClickable(false);
        completed.setClickable(true);
    }
    private void completed_click(){
        sent.setBackgroundColor(Color.TRANSPARENT);
        sent.setTextColor(getResources().getColor(R.color.mediumgray));
        notsend.setBackgroundColor(Color.TRANSPARENT);
        notsend.setTextColor(getResources().getColor(R.color.mediumgray));
        recieved.setBackgroundColor(Color.TRANSPARENT);
        recieved.setTextColor(getResources().getColor(R.color.mediumgray));
        completed.setBackgroundResource(background_btn_activity);
        completed.setTextColor(getResources().getColor(R.color.green));
        all_post.setBackgroundColor(Color.TRANSPARENT);
        all_post.setTextColor(getResources().getColor(R.color.mediumgray));
        view_pager.setCurrentItem(4);
        all_post.setClickable(true);
        sent.setClickable(true);
        notsend.setClickable(true);
        recieved.setClickable(true);
        completed.setClickable(false);
    }

    private void setupbtn() {
        all_post.setBackgroundResource(background_btn_activity);
        all_post.setTextColor(getResources().getColor(R.color.green));
    }
    private void setupViewPager(ViewPager view_pager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFrag(new AllActivityFragment(), "Trang chủ");
        adapter.addFrag(new PostSentFragment(), "Quản lý tin");
        adapter.addFrag(new NotSendFragment(),"Chưa gửi");
        adapter.addFrag(new NotSendFragment(),"Đã nhận");
        adapter.addFrag(new NotSendFragment(),"Đã hoàn thành");
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
                    sent.setBackgroundColor(Color.TRANSPARENT);
                    sent.setTextColor(getResources().getColor(R.color.mediumgray));
                    all_post.setBackgroundResource(background_btn_activity);
                    all_post.setTextColor(getResources().getColor(R.color.green));
                    notsend.setBackgroundColor(Color.TRANSPARENT);
                    notsend.setTextColor(getResources().getColor(R.color.mediumgray));
                    recieved.setBackgroundColor(Color.TRANSPARENT);
                    recieved.setTextColor(getResources().getColor(R.color.mediumgray));
                    completed.setBackgroundColor(Color.TRANSPARENT);
                    completed.setTextColor(getResources().getColor(R.color.mediumgray));
                    all_post.setClickable(false);
                    sent.setClickable(true);
                    notsend.setClickable(true);
                    recieved.setClickable(true);
                    completed.setClickable(true);
                    //menuScroll.smoothScrollTo(all_post.getLeft(),0);
                    ObjectAnimator animator=ObjectAnimator.ofInt(menuScroll, "scrollX",all_post.getLeft() );
                    animator.setDuration(800);
                    animator.start();
                    break;
                case 1:
                    all_post.setBackgroundColor(Color.TRANSPARENT);
                    all_post.setTextColor(getResources().getColor(R.color.mediumgray));
                    recieved.setBackgroundColor(Color.TRANSPARENT);
                    recieved.setTextColor(getResources().getColor(R.color.mediumgray));
                    completed.setBackgroundColor(Color.TRANSPARENT);
                    completed.setTextColor(getResources().getColor(R.color.mediumgray));
                    sent.setBackgroundResource(background_btn_activity);
                    sent.setTextColor(getResources().getColor(R.color.green));
                    notsend.setBackgroundColor(Color.TRANSPARENT);
                    notsend.setTextColor(getResources().getColor(R.color.mediumgray));
                    sent.setClickable(false);
                    all_post.setClickable(true);
                    notsend.setClickable(true);
                    recieved.setClickable(true);
                    completed.setClickable(true);
                    //menuScroll.smoothScrollTo(sent.getLeft(),0);
                    ObjectAnimator animator1=ObjectAnimator.ofInt(menuScroll, "scrollX",sent.getLeft() );
                    animator1.setDuration(800);
                    animator1.start();
                    break;
                case 2:
                    recieved.setBackgroundColor(Color.TRANSPARENT);
                    recieved.setTextColor(getResources().getColor(R.color.mediumgray));
                    completed.setBackgroundColor(Color.TRANSPARENT);
                    completed.setTextColor(getResources().getColor(R.color.mediumgray));
                    sent.setBackgroundColor(Color.TRANSPARENT);
                    sent.setTextColor(getResources().getColor(R.color.mediumgray));
                    notsend.setBackgroundResource(background_btn_activity);
                    notsend.setTextColor(getResources().getColor(R.color.green));
                    all_post.setBackgroundColor(Color.TRANSPARENT);
                    all_post.setTextColor(getResources().getColor(R.color.mediumgray));
                    all_post.setClickable(true);
                    sent.setClickable(true);
                    notsend.setClickable(false);
                    recieved.setClickable(true);
                    completed.setClickable(true);
                    //menuScroll.smoothScrollTo(notsend.getLeft(),0);
                    ObjectAnimator animator2=ObjectAnimator.ofInt(menuScroll, "scrollX",notsend.getLeft() );
                    animator2.setDuration(800);
                    animator2.start();
                    break;
                case 3:
                    notsend.setBackgroundColor(Color.TRANSPARENT);
                    notsend.setTextColor(getResources().getColor(R.color.mediumgray));
                    completed.setBackgroundColor(Color.TRANSPARENT);
                    completed.setTextColor(getResources().getColor(R.color.mediumgray));
                    sent.setBackgroundColor(Color.TRANSPARENT);
                    sent.setTextColor(getResources().getColor(R.color.mediumgray));
                    recieved.setBackgroundResource(background_btn_activity);
                    recieved.setTextColor(getResources().getColor(R.color.green));
                    all_post.setBackgroundColor(Color.TRANSPARENT);
                    all_post.setTextColor(getResources().getColor(R.color.mediumgray));
                    all_post.setClickable(true);
                    sent.setClickable(true);
                    recieved.setClickable(false);
                    notsend.setClickable(true);
                    completed.setClickable(true);
                    //menuScroll.smoothScrollTo(recieved.getLeft(),0);
                    ObjectAnimator animator3=ObjectAnimator.ofInt(menuScroll, "scrollX",recieved.getLeft() );
                    animator3.setDuration(800);
                    animator3.start();
                    break;
                case 4:
                    completed.setBackgroundResource(background_btn_activity);
                    completed.setTextColor(getResources().getColor(R.color.green));
                    recieved.setBackgroundColor(Color.TRANSPARENT);
                    recieved.setTextColor(getResources().getColor(R.color.mediumgray));
                    sent.setBackgroundColor(Color.TRANSPARENT);
                    sent.setTextColor(getResources().getColor(R.color.mediumgray));
                    notsend.setBackgroundColor(Color.TRANSPARENT);
                    notsend.setTextColor(getResources().getColor(R.color.mediumgray));
                    all_post.setBackgroundColor(Color.TRANSPARENT);
                    all_post.setTextColor(getResources().getColor(R.color.mediumgray));
                    all_post.setClickable(true);
                    sent.setClickable(true);
                    completed.setClickable(false);
                    recieved.setClickable(true);
                    notsend.setClickable(true);
                    //menuScroll.smoothScrollTo(completed.getLeft(),0);
                    ObjectAnimator animator4=ObjectAnimator.ofInt(menuScroll, "scrollX",completed.getLeft() );
                    animator4.setDuration(800);
                    animator4.start();
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}