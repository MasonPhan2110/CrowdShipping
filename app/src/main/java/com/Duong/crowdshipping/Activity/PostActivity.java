package com.Duong.crowdshipping.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.Duong.crowdshipping.R;
import com.Duong.crowdshipping.adapter.HomeAdapter;
import com.Duong.crowdshipping.adapter.PostAdapter;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    LinearLayout  linearLayout;
    String type;
    List item = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.green));

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            type = extras.getString("type"," ");
        }
        Log.d("typeID", String.valueOf(type == "clothes"));
        linearLayout = findViewById(R.id.linear_layout);
        if(type.equals("clothes")){
            item.add("Quần áo");
            item.add("Đồng hồ");
            item.add("Giày dép");
            item.add("Túi xách");
        }else if(type.equals("devices")){
            item.add("Điện thoại");
            item.add("Máy tính bảng");
        }else{
            item.add("Máy in");
            item.add("Máy in");
            item.add("Máy in");
            item.add("Máy in");
        }
        loadItem(item);
    }

    private void loadItem(List item) {
        PostAdapter postAdapter = new PostAdapter(PostActivity.this, linearLayout, item, type);
        postAdapter.load();
    }
}