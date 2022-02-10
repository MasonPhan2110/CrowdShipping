package com.Duong.crowdshipping.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Duong.crowdshipping.R;
import com.Duong.crowdshipping.adapter.DetailPostAdapter;
import com.Duong.crowdshipping.model.Post;
import com.Duong.crowdshipping.model.SliderData;
import com.Duong.crowdshipping.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DetailPostActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();
    HashMap<String, Object> linkImage;
    String type;
    Toolbar toolbar;
    Post post;
    TextView getPost;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

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
        toolbar = findViewById(R.id.toolbar);
        getPost = findViewById(R.id.getPost);
//        getPost.bringToFront();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setMinimumHeight(toolbar.getHeight());
        Intent intent = getIntent();
        post = (Post) intent.getSerializableExtra("Post");
        for(Object i:post.getLinkImage().values()){
            Log.d("getValues", "onCreate: "+i.toString());
            sliderDataArrayList.add(new SliderData(i.toString()));
        }
        wallDetailPost(pgsBar);
        getPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPostclick();
            }
        });
    }

    private void getPostclick() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                if(users.getIdImg()==null){
                    new AlertDialog.Builder(DetailPostActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Thông báo")
                            .setMessage("Bạn chưa cập nhật ảnh chứng minh thư nhân dân (căn cước công dân). Vui lòng cập nhật ảnh trong Profile để có thể nhận đơn.")
                            .setPositiveButton("Ok", null)
                            .show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void wallDetailPost(ProgressBar pgsBar) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        DetailPostAdapter detailPostAdapter = new DetailPostAdapter(linearLayout, DetailPostActivity.this, sliderDataArrayList, post);
        detailPostAdapter.loadDetailPost(width);
        pgsBar.setVisibility(View.GONE);
    }
}