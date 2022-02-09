package com.Duong.crowdshipping.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Duong.crowdshipping.fragment.NotiFragment;
import com.Duong.crowdshipping.R;
import com.Duong.crowdshipping.fragment.AccountFragment;
import com.Duong.crowdshipping.fragment.ActivityFragment;
import com.Duong.crowdshipping.fragment.HomeFragment;
import com.Duong.crowdshipping.fragment.PostFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView navigation;
    ViewPager view_pager;
    TextView title;
    LinearLayout searchBox;
    ImageView message;
    MenuItem menuItem;
    HomeActivityListener homeActivityBackPress;
    private int[] selectitem = {
            R.drawable.ic_round_home_24,
            R.drawable.ic_round_article_24,
            R.drawable.ic_round_border_color_24,
            R.drawable.ic_baseline_notifications_active_24,
            R.drawable.ic_round_menu_open_24
    };
    private int[] unselectitem={
            R.drawable.ic_outline_home_24,
            R.drawable.ic_outline_article_24,
            R.drawable.ic_outline_border_color_24,
            R.drawable.ic_round_notifications_none_24,
            R.drawable.ic_round_menu_24
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        int flag = HomeActivity.this.getWindow().getDecorView().getSystemUiVisibility();
//        flag |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        HomeActivity.this.getWindow().getDecorView().setSystemUiVisibility(flag);
        Window window = HomeActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(HomeActivity.this.getResources().getColor(R.color.green));
        window.setNavigationBarColor(HomeActivity.this.getResources().getColor(android.R.color.white));

        navigation = findViewById(R.id.navigation);
        view_pager = findViewById(R.id.view_pager);
        setupViewPager(view_pager);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        view_pager.addOnPageChangeListener(onPageChangeListener);

        title = findViewById(R.id.title);
        searchBox = findViewById(R.id.search_box);
        message = findViewById(R.id.message);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ConversationActivity.class);
                startActivity(intent);
            }
        });
    }
    private void setupViewPager(ViewPager view_pager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Trang chủ");
        adapter.addFragment(new ActivityFragment(), "Quản lý tin");
        adapter.addFragment(new PostFragment(),"Đăng tin");
        adapter.addFragment(new NotiFragment(),"Thông báo");
        adapter.addFragment(new AccountFragment(),"Thêm");
        view_pager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> fragments;
        private final ArrayList<String> titles;
        private ArrayList<Drawable> drawables;

        ViewPagerAdapter(FragmentManager fm){
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }
        @NonNull
        @Override
        public CharSequence getPageTitle(int position){
            return null;
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =  new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.home:
                    searchBox.setVisibility(View.VISIBLE);
                    title.setVisibility(View.GONE);
                    title.setText("Trang chủ");
                    view_pager.setCurrentItem(0);
                    navigation.getMenu().findItem(R.id.home).setIcon(selectitem[0]);
                    navigation.getMenu().findItem(R.id.activity).setIcon(unselectitem[1]);
                    navigation.getMenu().findItem(R.id.create_post).setIcon(unselectitem[2]);
                    navigation.getMenu().findItem(R.id.noti).setIcon(unselectitem[3]);
                    navigation.getMenu().findItem(R.id.menu).setIcon(unselectitem[4]);
                    break;
                case R.id.activity:
                    searchBox.setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);
                    title.setText("Quản lý tin");
                    view_pager.setCurrentItem(1);
                    navigation.getMenu().findItem(R.id.home).setIcon(unselectitem[0]);
                    navigation.getMenu().findItem(R.id.activity).setIcon(selectitem[1]);
                    navigation.getMenu().findItem(R.id.create_post).setIcon(unselectitem[2]);
                    navigation.getMenu().findItem(R.id.noti).setIcon(unselectitem[3]);
                    navigation.getMenu().findItem(R.id.menu).setIcon(unselectitem[4]);
                    break;
                case R.id.create_post:
                    searchBox.setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);
                    title.setText("Đăng tin");
                    view_pager.setCurrentItem(2);
                    navigation.getMenu().findItem(R.id.home).setIcon(unselectitem[0]);
                    navigation.getMenu().findItem(R.id.activity).setIcon(unselectitem[1]);
                    navigation.getMenu().findItem(R.id.create_post).setIcon(selectitem[2]);
                    navigation.getMenu().findItem(R.id.noti).setIcon(unselectitem[3]);
                    navigation.getMenu().findItem(R.id.menu).setIcon(unselectitem[4]);
                    break;
                case R.id.noti:
                    searchBox.setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);
                    title.setText("Thông báo");
                    view_pager.setCurrentItem(3);
                    navigation.getMenu().findItem(R.id.home).setIcon(unselectitem[0]);
                    navigation.getMenu().findItem(R.id.activity).setIcon(unselectitem[1]);
                    navigation.getMenu().findItem(R.id.create_post).setIcon(unselectitem[2]);
                    navigation.getMenu().findItem(R.id.noti).setIcon(selectitem[3]);
                    navigation.getMenu().findItem(R.id.menu).setIcon(unselectitem[4]);
                    break;
                case R.id.menu:
                    searchBox.setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);
                    title.setText("Thêm");
                    view_pager.setCurrentItem(4);
                    navigation.getMenu().findItem(R.id.home).setIcon(unselectitem[0]);
                    navigation.getMenu().findItem(R.id.activity).setIcon(unselectitem[1]);
                    navigation.getMenu().findItem(R.id.create_post).setIcon(unselectitem[2]);
                    navigation.getMenu().findItem(R.id.noti).setIcon(unselectitem[3]);
                    navigation.getMenu().findItem(R.id.menu).setIcon(selectitem[4]);
                    break;
            }
            return true;
        }
    };
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position){
                case 0:
                    searchBox.setVisibility(View.VISIBLE);
                    title.setVisibility(View.GONE);
                    title.setText("Trang chủ");
                    navigation.getMenu().findItem(R.id.home).setChecked(true);
                    navigation.getMenu().findItem(R.id.home).setIcon(selectitem[0]);
                    navigation.getMenu().findItem(R.id.activity).setIcon(unselectitem[1]);
                    navigation.getMenu().findItem(R.id.create_post).setIcon(unselectitem[2]);
                    navigation.getMenu().findItem(R.id.noti).setIcon(unselectitem[3]);
                    navigation.getMenu().findItem(R.id.menu).setIcon(unselectitem[4]);
                    break;
                case 1:
                    searchBox.setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);
                    title.setText("Quản lý tin");
                    navigation.getMenu().findItem(R.id.activity).setChecked(true);
                    navigation.getMenu().findItem(R.id.home).setIcon(unselectitem[0]);
                    navigation.getMenu().findItem(R.id.activity).setIcon(selectitem[1]);
                    navigation.getMenu().findItem(R.id.create_post).setIcon(unselectitem[2]);
                    navigation.getMenu().findItem(R.id.noti).setIcon(unselectitem[3]);
                    navigation.getMenu().findItem(R.id.menu).setIcon(unselectitem[4]);
                    break;
                case 2:
                    searchBox.setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);
                    title.setText("Đăng tin");
                    navigation.getMenu().findItem(R.id.activity).setChecked(true);
                    navigation.getMenu().findItem(R.id.home).setIcon(unselectitem[0]);
                    navigation.getMenu().findItem(R.id.activity).setIcon(unselectitem[1]);
                    navigation.getMenu().findItem(R.id.create_post).setIcon(selectitem[2]);
                    navigation.getMenu().findItem(R.id.noti).setIcon(unselectitem[3]);
                    navigation.getMenu().findItem(R.id.menu).setIcon(unselectitem[4]);
                    break;
                case 3:
                    searchBox.setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);
                    title.setText("Thông báo");
                    navigation.getMenu().findItem(R.id.noti).setChecked(true);
                    navigation.getMenu().findItem(R.id.home).setIcon(unselectitem[0]);
                    navigation.getMenu().findItem(R.id.activity).setIcon(unselectitem[1]);
                    navigation.getMenu().findItem(R.id.create_post).setIcon(unselectitem[2]);
                    navigation.getMenu().findItem(R.id.noti).setIcon(selectitem[3]);
                    navigation.getMenu().findItem(R.id.menu).setIcon(unselectitem[4]);
                    break;
                case 4:
                    searchBox.setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);
                    title.setText("Thêm");
                    navigation.getMenu().findItem(R.id.menu).setChecked(true);
                    navigation.getMenu().findItem(R.id.home).setIcon(unselectitem[0]);
                    navigation.getMenu().findItem(R.id.activity).setIcon(unselectitem[1]);
                    navigation.getMenu().findItem(R.id.create_post).setIcon(unselectitem[2]);
                    navigation.getMenu().findItem(R.id.noti).setIcon(unselectitem[3]);
                    navigation.getMenu().findItem(R.id.menu).setIcon(selectitem[4]);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if(fragment instanceof HomeActivityListener){
            homeActivityBackPress = (HomeActivityListener) fragment;
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        homeActivityBackPress = null;
    }

    public interface HomeActivityListener{
        void backpress();
    }
    void backpress(){
        homeActivityBackPress.backpress();
    }
}