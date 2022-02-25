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

import com.Duong.crowdshipping.R;
import com.Duong.crowdshipping.adapter.DetailPostAdapter;
import com.Duong.crowdshipping.model.Post;
import com.Duong.crowdshipping.model.SliderData;
import com.Duong.crowdshipping.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.geojson.Point;
import com.mapbox.search.MapboxSearchSdk;
import com.mapbox.search.ResponseInfo;
import com.mapbox.search.SearchEngine;
import com.mapbox.search.SearchOptions;
import com.mapbox.search.SearchRequestTask;
import com.mapbox.search.SearchSelectionCallback;
import com.mapbox.search.result.SearchResult;
import com.mapbox.search.result.SearchSuggestion;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DetailPostActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();
    HashMap<String, Object> linkImage;
    String type;
    Toolbar toolbar;
    Post post;
    TextView getPost;
    String Type;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private SearchEngine searchEngine;
    private SearchRequestTask searchRequestTask;
    private List<Point> targetPoint = new ArrayList<>();

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
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getPost = findViewById(R.id.getPost);
//        getPost.bringToFront();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Intent intent = getIntent();
        post = (Post) intent.getSerializableExtra("Post");
        Type = intent.getStringExtra("Type");
        for(Object i:post.getLinkImage().values()){
            Log.d("getValues", "onCreate: "+i.toString());
            sliderDataArrayList.add(new SliderData(i.toString()));
        }
        wallDetailPost(pgsBar);
        getPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPostclick(Type);
            }
        });
        if(Type.equals("Sent")){
            if(post.getStatus().equals("3")){
                getPost.setVisibility(View.VISIBLE);
                getPost.setText("Xác nhận");
            }else if(post.getStatus().equals("0")){
                getPost.setVisibility(View.VISIBLE);
                getPost.setText("Sửa đơn hàng");
            }else{
                getPost.setVisibility(View.GONE);
            }

        }else if(Type.equals("Receive")){
            getPost.setText("Đã nhận đơn");
        }else if(Type.equals("Complete")){
            getPost.setVisibility(View.GONE);
        }
        try {
            MapboxSearchSdk.initialize(getApplication(),
                    getString(R.string.mapbox_access_token),
                    LocationEngineProvider.getBestLocationEngine(this));

        }catch (Exception e){
            Log.d("Already initialized", e.toString());
        }
        searchEngine = MapboxSearchSdk.getSearchEngine();

        final SearchOptions options = new SearchOptions.Builder()
                .limit(5)
                .build();

        searchRequestTask = searchEngine.search(post.getAddressTo().get("Address")+", đường "+post.getAddressTo().get("Streets")
                +", phường " +post.getAddressTo().get("Wards")+", quận "+post.getAddressTo().get("District")+
                ", "+post.getAddressTo().get("City"), options, searchCallback);
        searchRequestTask = searchEngine.search(post.getAddressFrom().get("Address")+", đường "+post.getAddressFrom().get("Streets")
                +", phường " +post.getAddressFrom().get("Wards")+", quận "+post.getAddressFrom().get("District")+
                ", "+post.getAddressFrom().get("City"), options, searchCallback);
    }

    private void getPostclick(String type) {
            if(post.getStatus().equals("3")){
                new AlertDialog.Builder(DetailPostActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Thông báo")
                        .setMessage("Bạn xác nhận đơn hàng đã chuyển thành công?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Post").child(post.getPostID());
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("Status","4");
                                reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        new AlertDialog.Builder(DetailPostActivity.this)
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .setTitle("Thông báo")
                                                .setMessage("Đơn hàng đã được chuyển thành công.")
                                                .setPositiveButton("Ok", null)
                                                .show();
                                        getPost.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }).setNegativeButton("Close", null)
                        .show();
            }else{
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
                        }else{
                            getPost.setText("Đã nhận đơn");
                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Post").child(post.getPostID());
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("Shipper", user.getUid());
                            hashMap.put("Status","1");
                            reference1.updateChildren(hashMap);
                            String msg = "Đơn hàng đã được nhận bởi "+users.getUsername()+"." +"\nSố điện thoại của tôi là: "+users.getPhone();
                            sendMessage(user.getUid(),post.getCreateID(),msg);
                            msg="Đơn hàng là "+post.getDescription() + ".\nTừ: "+post.getAddressFrom().get("Address")+", đường "+post.getAddressFrom().get("Streets")+", phường "+post.getAddressFrom().get("Wards")+
                                    ", quận "+post.getAddressFrom().get("District")+",thành phố "+post.getAddressFrom().get("City") +".\nĐến: "+post.getAddressTo().get("Address")+", đường "+post.getAddressTo().get("Streets")+", phường "+post.getAddressTo().get("Wards")+
                                    ", quận "+post.getAddressTo().get("District")+",thành phố "+post.getAddressTo().get("City") + ".\nSố điện thoại người gửi: "+post.getPhoneFrom() +".\nSố điện thoại người nhận: "+post.getPhoneTo();
                            sendMessage(post.getCreateID(),user.getUid(),msg);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                            String currentDateandTime = sdf.format(new Date());
                            reference1 = FirebaseDatabase.getInstance().getReference("Notification").child(post.getCreateID());
                            HashMap<String, Object> hashMap1 = new HashMap<>();
                            DatabaseReference pushedPostRef  = reference1.push();
                            hashMap1.put("MSG", "Đơn hàng của bạn đã được nhận");
                            hashMap1.put("PostID", post.getPostID());
                            hashMap1.put("Time", currentDateandTime);
                            hashMap1.put("NotiID", pushedPostRef.getKey());
                            hashMap1.put("isseen", false);
                            hashMap1.put("isread", false);
                            pushedPostRef.setValue(hashMap1);
                            Intent intent = new Intent(DetailPostActivity.this, MapActivity.class);
                            intent.putExtra("Post", (Serializable) post);
                            intent.putExtra("TargetTo", targetPoint.get(1));
                            intent.putExtra("TargetFrom", targetPoint.get(0));
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

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
    private void sendMessage(String sender, String reciever, String msg) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", reciever);
        hashMap.put("message", msg);
        hashMap.put("isseen", false);
        reference.child("Chats").push().setValue(hashMap);
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(user.getUid())
                .child(post.getCreateID());
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    chatRef.child("id").setValue(post.getCreateID());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(post.getCreateID())
                .child(user.getUid());
        chatRefReceiver.child("id").setValue(user.getUid());
        final String message = msg;

    }
    private final SearchSelectionCallback searchCallback = new SearchSelectionCallback() {
        @Override
        public void onSuggestions(@NonNull List<? extends SearchSuggestion> suggestions, @NonNull ResponseInfo responseInfo) {
            if (suggestions.isEmpty()) {
                Log.i("SearchApiExample", "No suggestions found");
            } else {
                Log.i("SearchApiExample", "Search suggestions: " + suggestions + "\nSelecting first...");
                searchRequestTask = searchEngine.select(suggestions.get(0), this);
            }
        }
        @Override
        public void onCategoryResult(@NonNull SearchSuggestion searchSuggestion, @NonNull List<? extends SearchResult> list, @NonNull ResponseInfo responseInfo) {
            Log.i("SearchApiExample", "Category search results: " + list);
        }

        @Override
        public void onResult(@NonNull SearchSuggestion suggestion, @NonNull SearchResult result, @NonNull ResponseInfo info) {
            targetPoint.add(result.getCoordinate());
            Log.i("SearchApiExample", "Search result: " + targetPoint);
        }

        @Override
        public void onError(@NonNull Exception e) {
            Log.i("SearchApiExample", "Search error: ", e);
        }
    };
    @Override
    protected void onDestroy() {
        searchRequestTask.cancel();
        super.onDestroy();
    }
}