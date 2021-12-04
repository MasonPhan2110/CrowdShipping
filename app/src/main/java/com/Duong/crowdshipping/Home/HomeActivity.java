package com.Duong.crowdshipping.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.Duong.crowdshipping.adapter.CustomExpandableListAdapter;
import com.Duong.crowdshipping.model.Cars;
import com.Duong.crowdshipping.Login.LoginActivity;
import com.Duong.crowdshipping.R;
import com.Duong.crowdshipping.fragment.AccountFragment;
import com.Duong.crowdshipping.fragment.ActivityFragment;
import com.Duong.crowdshipping.fragment.ChatFragment;
import com.Duong.crowdshipping.fragment.HomeFragment;
import com.Duong.crowdshipping.fragment.PayFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView navigation;
    ViewPager view_pager;
    ExpandableListView list_car;
    List<Cars> mCars;
    List<String> Cars;
    List<String> listTitle;
    ExpandableListAdapter listAdapter;
    HashMap<String, List<String>> expandListDetail;
    List<String> ID = new ArrayList<>();
    List<String> Name = new ArrayList<>();
    List<String> Plate = new ArrayList<>();
    List<String> Status = new ArrayList<>();
    String ID_car;
    FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
    private int[] selectitem = {
            R.drawable.ic_baseline_explore_24,
            R.drawable.ic_baseline_receipt_long_24,
            R.drawable.ic_baseline_account_balance_wallet_24,
            R.drawable.ic_baseline_chat_bubble_24,
            R.drawable.ic_baseline_account_circle_24
    };
    private int[] unselectitem={
            R.drawable.ic_outline_explore_24,
            R.drawable.ic_outline_receipt_long_24,
            R.drawable.ic_outline_account_balance_wallet_24,
            R.drawable.ic_baseline_chat_bubble_outline_24,
            R.drawable.ic_outline_account_circle_24
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        int flag = HomeActivity.this.getWindow().getDecorView().getSystemUiVisibility();
        flag |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        HomeActivity.this.getWindow().getDecorView().setSystemUiVisibility(flag);
        Window window = HomeActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(HomeActivity.this.getResources().getColor(R.color.white));
        window.setNavigationBarColor(HomeActivity.this.getResources().getColor(android.R.color.white));

        navigation = findViewById(R.id.navigation);
        view_pager = findViewById(R.id.view_pager);
        setupViewPager(view_pager);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        view_pager.addOnPageChangeListener(onPageChangeListener);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getCarInfor();
            }
        },100);



    }
    private void setupViewPager(ViewPager view_pager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Trang chủ");
        adapter.addFragment(new ActivityFragment(), "Hoạt động");
        adapter.addFragment(new PayFragment(),"Thanh toán");
        adapter.addFragment(new ChatFragment(),"Nhắn tin");
        adapter.addFragment(new AccountFragment(),"Tài khoản");
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
                    view_pager.setCurrentItem(0);
                    navigation.getMenu().findItem(R.id.home).setIcon(selectitem[0]);
                    navigation.getMenu().findItem(R.id.activity).setIcon(unselectitem[1]);
                    navigation.getMenu().findItem(R.id.pay).setIcon(unselectitem[2]);
                    navigation.getMenu().findItem(R.id.chat).setIcon(unselectitem[3]);
                    navigation.getMenu().findItem(R.id.account).setIcon(unselectitem[4]);
                    break;
                case R.id.activity:
                    view_pager.setCurrentItem(1);
                    navigation.getMenu().findItem(R.id.home).setIcon(unselectitem[0]);
                    navigation.getMenu().findItem(R.id.activity).setIcon(selectitem[1]);
                    navigation.getMenu().findItem(R.id.pay).setIcon(unselectitem[2]);
                    navigation.getMenu().findItem(R.id.chat).setIcon(unselectitem[3]);
                    navigation.getMenu().findItem(R.id.account).setIcon(unselectitem[4]);
                    break;
                case R.id.pay:
                    view_pager.setCurrentItem(2);
                    navigation.getMenu().findItem(R.id.home).setIcon(unselectitem[0]);
                    navigation.getMenu().findItem(R.id.activity).setIcon(unselectitem[1]);
                    navigation.getMenu().findItem(R.id.pay).setIcon(selectitem[2]);
                    navigation.getMenu().findItem(R.id.chat).setIcon(unselectitem[3]);
                    navigation.getMenu().findItem(R.id.account).setIcon(unselectitem[4]);
                    break;
                case R.id.chat:
                    view_pager.setCurrentItem(3);
                    navigation.getMenu().findItem(R.id.home).setIcon(unselectitem[0]);
                    navigation.getMenu().findItem(R.id.activity).setIcon(unselectitem[1]);
                    navigation.getMenu().findItem(R.id.pay).setIcon(unselectitem[2]);
                    navigation.getMenu().findItem(R.id.chat).setIcon(selectitem[3]);
                    navigation.getMenu().findItem(R.id.account).setIcon(unselectitem[4]);
                    break;
                case R.id.account:
                    view_pager.setCurrentItem(4);
                    navigation.getMenu().findItem(R.id.home).setIcon(unselectitem[0]);
                    navigation.getMenu().findItem(R.id.activity).setIcon(unselectitem[1]);
                    navigation.getMenu().findItem(R.id.pay).setIcon(unselectitem[2]);
                    navigation.getMenu().findItem(R.id.chat).setIcon(unselectitem[3]);
                    navigation.getMenu().findItem(R.id.account).setIcon(selectitem[4]);
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
                    navigation.getMenu().findItem(R.id.home).setChecked(true);
                    navigation.getMenu().findItem(R.id.home).setIcon(selectitem[0]);
                    navigation.getMenu().findItem(R.id.activity).setIcon(unselectitem[1]);
                    navigation.getMenu().findItem(R.id.pay).setIcon(unselectitem[2]);
                    navigation.getMenu().findItem(R.id.chat).setIcon(unselectitem[3]);
                    navigation.getMenu().findItem(R.id.account).setIcon(unselectitem[4]);
                    break;
                case 1:
                    navigation.getMenu().findItem(R.id.activity).setChecked(true);
                    navigation.getMenu().findItem(R.id.home).setIcon(unselectitem[0]);
                    navigation.getMenu().findItem(R.id.activity).setIcon(selectitem[1]);
                    navigation.getMenu().findItem(R.id.pay).setIcon(unselectitem[2]);
                    navigation.getMenu().findItem(R.id.chat).setIcon(unselectitem[3]);
                    navigation.getMenu().findItem(R.id.account).setIcon(unselectitem[4]);
                    break;
                case 2:
                    navigation.getMenu().findItem(R.id.pay).setChecked(true);
                    navigation.getMenu().findItem(R.id.home).setIcon(unselectitem[0]);
                    navigation.getMenu().findItem(R.id.activity).setIcon(unselectitem[1]);
                    navigation.getMenu().findItem(R.id.pay).setIcon(selectitem[2]);
                    navigation.getMenu().findItem(R.id.chat).setIcon(unselectitem[3]);
                    navigation.getMenu().findItem(R.id.account).setIcon(unselectitem[4]);
                    break;
                case 3:
                    navigation.getMenu().findItem(R.id.chat).setChecked(true);
                    navigation.getMenu().findItem(R.id.home).setIcon(unselectitem[0]);
                    navigation.getMenu().findItem(R.id.activity).setIcon(unselectitem[1]);
                    navigation.getMenu().findItem(R.id.pay).setIcon(unselectitem[2]);
                    navigation.getMenu().findItem(R.id.chat).setIcon(selectitem[3]);
                    navigation.getMenu().findItem(R.id.account).setIcon(unselectitem[4]);
                    break;
                case 4:
                    navigation.getMenu().findItem(R.id.account).setChecked(true);
                    navigation.getMenu().findItem(R.id.home).setIcon(unselectitem[0]);
                    navigation.getMenu().findItem(R.id.activity).setIcon(unselectitem[1]);
                    navigation.getMenu().findItem(R.id.pay).setIcon(unselectitem[2]);
                    navigation.getMenu().findItem(R.id.chat).setIcon(unselectitem[3]);
                    navigation.getMenu().findItem(R.id.account).setIcon(selectitem[4]);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private void getCarInfor() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_start_window,null);
        PopupWindow window = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        window.setOutsideTouchable(false);
        int layout_height = 1200;
        int layout_width = getResources().getDisplayMetrics().widthPixels - 50;
        window.setHeight(layout_height);
        window.setWidth(layout_width);
        window.setAnimationStyle(R.style.Animation);
        window.setBackgroundDrawable(getDrawable(R.drawable.background_popup_1));
        window.update(0, 0, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setOutsideTouchable(false);
//        window.setFocusable(true);
//        window.setTouchModal(false);
//        layout.
        window.showAtLocation(layout, Gravity.BOTTOM, 0, 500);
        View container = window.getContentView().getRootView();
        Log.d("AAA", String.valueOf(window.isOutsideTouchable()));
        if(container!= null){
            WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
            WindowManager.LayoutParams p = (WindowManager.LayoutParams)container.getLayoutParams();
            p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            p.dimAmount = 0.5f;
            if(wm != null) {
                wm.updateViewLayout(container, p);
            }
        }

        list_car = layout.findViewById(R.id.list_Car);

        expandListDetail = new HashMap<String, List<String>>();
        listTitle = new ArrayList<>();
        mCars = new ArrayList<>();
        Cars = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid()).child("Cars");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    mCars.add(dataSnapshot.getValue(Cars.class));
                }

                for (int i = 0; i < mCars.size(); i++) {
                    Name.add(mCars.get(i).getVehicle());
                    Plate.add(mCars.get(i).getPlate());
                    ID.add(mCars.get(i).getId());
                    Status.add(mCars.get(i).getStatus());
                    Cars.add(mCars.get(i).getVehicle() + " - " + mCars.get(i).getPlate());
                }
                Cars.add("Khác...");
                expandListDetail.put("Chọn xe", Cars);
                listTitle = new ArrayList<>(expandListDetail.keySet());
                listAdapter = new CustomExpandableListAdapter(getApplicationContext(), listTitle, expandListDetail);
                list_car.setAdapter(listAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        list_car.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                int dp = 50*(list_car.getAdapter().getCount());
                int px = (int) (dp * getResources().getDisplayMetrics().density);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        px
                );
                layoutParams.setMargins(0, (int) (15 * getResources().getDisplayMetrics().density), 0, 0);
                list_car.setLayoutParams(layoutParams);
            }
        });
        list_car.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                int dp = 50;
                int px = (int) (dp * getResources().getDisplayMetrics().density);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        px
                );
                layoutParams.setMargins(0, (int) (15 * getResources().getDisplayMetrics().density), 0, 0);
                list_car.setLayoutParams(layoutParams);
            }
        });
        list_car.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.d("AAA", String.valueOf(ID.size()));
                if(childPosition==ID.size()){

                }else{
                    ID_car = ID.get(childPosition);
                    SharedPreferences.Editor editor = getSharedPreferences("Car_Info",MODE_PRIVATE).edit();
                    editor.putString("ID_car",ID_car);
                    editor.putString("Name_car",Name.get(childPosition));
                    editor.putString("Plate_car",Plate.get(childPosition));
                    editor.putString("Status",Status.get(childPosition));
                    editor.apply();
                    window.dismiss();
                }
                return false;
            }
        });
    }
}