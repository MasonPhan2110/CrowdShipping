package com.Duong.crowdshipping.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.Duong.crowdshipping.R;

public class PostActivity extends AppCompatActivity {

    int typeID;
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
            typeID = extras.getInt("typeID");
        }
        Log.d("typeID", String.valueOf(typeID));
    }
}