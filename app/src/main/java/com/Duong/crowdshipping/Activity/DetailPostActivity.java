package com.Duong.crowdshipping.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.Duong.crowdshipping.R;

public class DetailPostActivity extends AppCompatActivity {
    LinearLayout linearLayout;
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
    }
}