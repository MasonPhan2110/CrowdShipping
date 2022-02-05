package com.Duong.crowdshipping.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.Duong.crowdshipping.R;
import com.Duong.crowdshipping.adapter.DetailPostAdapter;
import com.Duong.crowdshipping.model.SliderData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DetailPostActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();
    HashMap<String, Object> linkImage;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        int flag = DetailPostActivity.this.getWindow().getDecorView().getSystemUiVisibility();
        flag |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        DetailPostActivity.this.getWindow().getDecorView().setSystemUiVisibility(flag);
        Window window = DetailPostActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(DetailPostActivity.this.getResources().getColor(R.color.green));
        window.setNavigationBarColor(DetailPostActivity.this.getResources().getColor(android.R.color.white));
        linearLayout = findViewById(R.id.linearLayout);
        ProgressBar pgsBar = findViewById(R.id.pBar);
        Intent intent = getIntent();
        linkImage = (HashMap<String, Object>) intent.getSerializableExtra("ImageLink");
        type = intent.getStringExtra("Type");
        for(Object i:linkImage.values()){
            Log.d("getValues", "onCreate: "+i.toString());
            sliderDataArrayList.add(new SliderData(i.toString()));
        }
        wallDetailPost(pgsBar);
    }

    private void wallDetailPost(ProgressBar pgsBar) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        DetailPostAdapter detailPostAdapter = new DetailPostAdapter(linearLayout, DetailPostActivity.this, sliderDataArrayList, type);
        detailPostAdapter.loadDetailPost(width);
        pgsBar.setVisibility(View.GONE);
    }
}