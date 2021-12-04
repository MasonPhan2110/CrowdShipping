package com.Duong.crowdshipping.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.Duong.crowdshipping.Login.LoginActivity;
import com.Duong.crowdshipping.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {
    Button cus, trans;
    CircleImageView profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Window window = HomeActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(HomeActivity.this.getResources().getColor(R.color.green));
        window.setNavigationBarColor(HomeActivity.this.getResources().getColor(android.R.color.white));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        cus = findViewById(R.id.customer_btn);
        trans = findViewById(R.id.transporter_btn);
        profile = findViewById(R.id.profile_img);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        int dp = 10;
        int px = (int) (dp*getResources().getDisplayMetrics().density);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                width/2 - 3*dp,
                300
        );
        //layoutParams.setMargins(0,0,px,0);
        cus.setLayoutParams(layoutParams);
        //layoutParams.setMargins(0,0,0,0);
        trans.setLayoutParams(layoutParams);
    }
}