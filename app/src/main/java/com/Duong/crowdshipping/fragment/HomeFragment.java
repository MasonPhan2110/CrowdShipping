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
        book_park.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupDialog();
            }
        });
        wallhome();
        return view;
    }

    private void popupDialog() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_book_parking,null);
        PopupWindow window = new PopupWindow(layout,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        int layout_height = getResources().getDisplayMetrics().heightPixels - 100;
        window.setHeight(layout_height);
        window.setAnimationStyle(R.style.Animation);
        window.setBackgroundDrawable(getActivity().getDrawable(R.drawable.background_popup));
        window.setOutsideTouchable(true);
        window.setFocusable(true);
        window.setTouchModal(false);
        window.update(0, 0, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.showAtLocation(layout, Gravity.BOTTOM, 0, 0);
        View container = window.getContentView().getRootView();
        if(container!= null){
            WindowManager wm = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
            WindowManager.LayoutParams p = (WindowManager.LayoutParams)container.getLayoutParams();
            p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            p.dimAmount = 0.3f;
            if(wm != null) {
                wm.updateViewLayout(container, p);
            }
        }
        Button select;
        EditText district, id_number, car_cate, name,car_number;
        district = layout.findViewById(R.id.district);
        id_number = layout.findViewById(R.id.id_number);
        car_cate = layout.findViewById(R.id.car_cate);
        name = layout.findViewById(R.id.name);
        car_number= layout.findViewById(R.id.car_number);
        select = layout.findViewById(R.id.select_park);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MapFragment", district.getText().toString());
                Intent intent = new Intent(getActivity(), FindParkActivity.class);
                intent.putExtra("district",district.getText().toString());
                intent.putExtra("id_number",id_number.getText().toString());
                intent.putExtra("car_cate",car_cate.getText().toString());
                intent.putExtra("name",name.getText().toString());
                intent.putExtra("car_number",car_number.getText().toString());
                window.dismiss();
                startActivity(intent);
            }
        });
    }

    private void wallhome() {
        HomeAdapter homeAdapter = new HomeAdapter(getContext(), linearLayout);
        homeAdapter.load_sales();
    }

}