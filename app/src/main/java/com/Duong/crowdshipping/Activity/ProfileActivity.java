package com.Duong.crowdshipping.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.Duong.crowdshipping.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        int flags = ProfileActivity.this.getWindow().getDecorView().getSystemUiVisibility();
//        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//        view.setSystemUiVisibility(flags);
    }
}