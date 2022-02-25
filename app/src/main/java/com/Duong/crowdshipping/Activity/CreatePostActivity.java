package com.Duong.crowdshipping.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Duong.crowdshipping.Extend.GetAddress;
import com.Duong.crowdshipping.Extend.GetDistance;
import com.Duong.crowdshipping.R;
import com.Duong.crowdshipping.adapter.SliderAdapter;
import com.Duong.crowdshipping.model.City;
import com.Duong.crowdshipping.model.District;
import com.Duong.crowdshipping.model.SliderData;
import com.Duong.crowdshipping.model.Users;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import com.mapbox.navigation.base.options.NavigationOptions;
import com.mapbox.navigation.core.MapboxNavigation;
import com.mapbox.navigation.core.MapboxNavigationProvider;
import com.mapbox.navigation.core.trip.session.LocationMatcherResult;
import com.mapbox.navigation.core.trip.session.LocationObserver;
import com.mapbox.search.ResponseInfo;
import com.mapbox.search.SearchCallback;
import com.mapbox.search.result.SearchResult;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import com.google.android.gms.maps.model.LatLng;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class CreatePostActivity extends AppCompatActivity {
    String type, item;
    LayoutInflater inflater;
    LinearLayout linear_layout,fromWrap;
    RelativeLayout autoAddressWrap;
    TextView post,editAddress,autoAddress, sumFee;
    EditText addressFrom, addressTo, phoneFrom, phoneTo;
    Button get_image;
    List<Uri> imagePath = new ArrayList<>();
    HashMap<String, Object> imageURI = new HashMap<>();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    String userid = firebaseUser.getUid();
    String jsonFileString = null;
    List<City> city = new ArrayList<>();
    String[] cityName, districtName, wardsName, streetName;
    String curentLocation, address, street,ward,district,cit;
    Boolean useAutoAddress = true;
    SliderView sliderView;
    ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();


    LocationObserver locationObserver = new LocationObserver() {
        Boolean isFirst = true;
        @Override
        public void onNewRawLocation(@NonNull Location location) {

        }

        @Override
        public void onNewLocationMatcherResult(@NonNull LocationMatcherResult locationMatcherResult) {
            if(isFirst){
                Location enhanceLocation = locationMatcherResult.getEnhancedLocation();
                Log.d("CurrLocation", locationMatcherResult.getEnhancedLocation().toString());
                LatLng latLng = new LatLng(enhanceLocation.getLatitude(),enhanceLocation.getLongitude());
                GetAddress getAddress = new GetAddress(latLng, CreatePostActivity.this);
                List<Address> addresses = getAddress.run();
                for(int i = 0;i<addresses.size();i++){
                    autoAddress.setText(addresses.get(i).getAddressLine(0));

                    curentLocation = addresses.get(i).getAddressLine(0);
                    String str = "";
                    int index = curentLocation.indexOf("Đường");
                    if(index>0){
                        address = curentLocation.split("Đường")[0];
                        str = curentLocation.split("Đường")[1];
                    }else{
                        address = curentLocation.split("-")[0];
                        str = curentLocation.split("-")[1];
                    }
                    Log.d("autoAddress", addresses.get(i).getAddressLine(0));

                    String[] arrStr = str.split(",",5);
                    for(int j=0;j< arrStr.length;j++){
                        Log.i("SearchApiExampleCreatePost", "Reverse geocoding results: "+arrStr[j]);
                    }
                    street = arrStr[0];
                    ward = arrStr[1];
                    district = arrStr[2];
                    cit = arrStr[3];
                    MapboxNavigationProvider.destroy();
                    mapboxNavigation.unregisterLocationObserver(locationObserver);
                }

//                reverseGeocoding = MapboxSearchSdk.getReverseGeocodingSearchEngine();
//                final ReverseGeoOptions options = new ReverseGeoOptions.Builder(Point.fromLngLat(enhanceLocation.getLongitude(),enhanceLocation.getLatitude()))
//                        .limit(1)
//                        .build();
//                searchRequestTask = reverseGeocoding.search(options, searchCallback);
                isFirst = false;
            }

        }
    };
    MapboxNavigation mapboxNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.green));

        initNavigation();

        post = findViewById(R.id.post);
        linear_layout = findViewById(R.id.linear_layout);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            type = extras.getString("type", " ");
            item = extras.getString("item", " ");
        }
        inflater = LayoutInflater.from(CreatePostActivity.this);
        String[] payshipType = {"Người chuyển trả", "Người nhận trả"};

        jsonFileString = loadJson();
        Gson gson = new Gson();
        Type listCityType = new TypeToken<List<City>>() {
        }.getType();
        Type listDistrictType = new TypeToken<List<District>>() {
        }.getType();
        city = gson.fromJson(jsonFileString, listCityType);
        cityName = new String[city.size()];
        for (int i = 0; i < city.size(); i++) {
            //districts = city.get(i).getDistricts();
            Log.i("DataTest", "> Item " + i + "\n" + city.get(i).getDistricts().get(0).getName());
            cityName[i] = city.get(i).getName();
        }

        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            if (data.getClipData() != null) {
                                int count = data.getClipData().getItemCount();
                                for (int i = 0; i < count; i++) {
                                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                                    imagePath.add(imageUri);
                                    Log.d("AAAAAAAA", imagePath.toString());
                                }
                            } else {
                                imagePath.add(data.getData());
                                Log.d("AAAAAAAA", imagePath.toString());
                            }
                            new AlertDialog.Builder(CreatePostActivity.this)
                                    .setTitle("Thông báo")
                                    .setMessage("Bạn đã gửi lên " + imagePath.size() + " ảnh")

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.yes, null)
                                    .show();
                        }
                    }
                });
        if (type.equals("clothes")) {
            switch (item) {
                case "Quần áo":
                    View view = inflater.inflate(R.layout.layout_clothes_clothes, linear_layout, false);
                    String[] clothesType = {"Váy", "Quần áo nam", "Quàn áo nữ"};

                    Spinner spinner = view.findViewById((R.id.drop_down_clothes));
                    Spinner spinner_ship_cost_clothes = view.findViewById(R.id.drop_down_ship_cost);
                    addressFrom = view.findViewById(R.id.addressFrom);
                    addressTo = view.findViewById(R.id.addressTo);
                    phoneFrom = view.findViewById(R.id.phoneTo);
                    get_image = view.findViewById(R.id.btn_get_img);
                    CheckBox checkbox_clothes_fragile = view.findViewById(R.id.checkbox_fragile);
                    ArrayAdapter clothesAdapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, clothesType);
                    ArrayAdapter shipCostAdapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, clothesType);
                    spinner.setAdapter(clothesAdapter);
                    spinner_ship_cost_clothes.setAdapter(shipCostAdapter);
                    Spinner spinner_clothes_cityFrom = view.findViewById(R.id.dropdown_cityFrom);
                    Spinner spinner_clothes_cityTo = view.findViewById(R.id.dropdown_cityTo);
                    Spinner spinner_clothes_districtFrom = view.findViewById(R.id.dropdown_districtFrom);
                    Spinner spinner_clothes_districtTo = view.findViewById(R.id.dropdown_districtTo);
                    Spinner spinner_clothes_wardsFrom = view.findViewById(R.id.dropdown_wardsFrom);
                    Spinner spinner_clothes_wardsTo = view.findViewById(R.id.dropdown_wardsTo);
                    Spinner spinner_clothes_streetFrom = view.findViewById(R.id.dropdown_streetFrom);
                    Spinner spinner_clothes_streetTo = view.findViewById(R.id.dropdown_streetTo);

                    ArrayAdapter spinner_clothes_city_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, cityName);
                    spinner_clothes_cityFrom.setAdapter(spinner_clothes_city_adapter);
                    spinner_clothes_cityTo.setAdapter(spinner_clothes_city_adapter);

                    spinner_clothes_cityFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            for (int a = 0; a < city.size(); a++) {
                                if (spinner_clothes_cityFrom.getSelectedItem().toString() == city.get(a).getName()) {
                                    districtName = new String[city.get(a).getDistricts().size()];
                                    for (int j = 0; j < city.get(a).getDistricts().size(); j++) {
                                        districtName[j] = city.get(a).getDistricts().get(j).getName();
                                    }
                                    ArrayAdapter spinner_watch_district_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, districtName);
                                    spinner_clothes_districtFrom.setAdapter(spinner_watch_district_adapter);
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spinner_clothes_districtFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            for (int a = 0; a < city.size(); a++) {
                                if (spinner_clothes_cityFrom.getSelectedItem().toString() == city.get(a).getName()) {
                                    for (int b = 0; b < city.get(a).getDistricts().size(); b++) {
                                        if (spinner_clothes_districtFrom.getSelectedItem().toString() == city.get(a).getDistricts().get(b).getName()) {
                                            wardsName = new String[city.get(a).getDistricts().get(b).getWards().size()];
                                            streetName = new String[city.get(a).getDistricts().get(b).getStreets().size()];
                                            for (int c = 0; c < city.get(a).getDistricts().get(b).getWards().size(); c++) {
                                                wardsName[c] = city.get(a).getDistricts().get(b).getWards().get(c).getName();
                                            }
                                            for (int c = 0; c < city.get(a).getDistricts().get(b).getStreets().size(); c++) {
                                                streetName[c] = city.get(a).getDistricts().get(b).getStreets().get(c).getName();
                                            }
                                            ArrayAdapter spinner_watch_wards_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, wardsName);
                                            spinner_clothes_wardsFrom.setAdapter(spinner_watch_wards_adapter);
                                            ArrayAdapter spinner_watch_street_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, streetName);
                                            spinner_clothes_streetFrom.setAdapter(spinner_watch_street_adapter);
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spinner_clothes_cityTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Log.d("CitySelected", city.get(i).getName());
                            for (int a = 0; a < city.size(); a++) {
                                if (spinner_clothes_cityTo.getSelectedItem().toString() == city.get(a).getName()) {
                                    districtName = new String[city.get(a).getDistricts().size()];
                                    for (int j = 0; j < city.get(a).getDistricts().size(); j++) {
                                        districtName[j] = city.get(a).getDistricts().get(j).getName();
                                    }
                                    ArrayAdapter spinner_watch_district_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, districtName);
                                    spinner_clothes_districtTo.setAdapter(spinner_watch_district_adapter);
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spinner_clothes_districtTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            for (int a = 0; a < city.size(); a++) {
                                if (spinner_clothes_cityTo.getSelectedItem().toString() == city.get(a).getName()) {
                                    for (int b = 0; b < city.get(a).getDistricts().size(); b++) {
                                        if (spinner_clothes_districtTo.getSelectedItem().toString() == city.get(a).getDistricts().get(b).getName()) {
                                            wardsName = new String[city.get(a).getDistricts().get(b).getWards().size()];
                                            streetName = new String[city.get(a).getDistricts().get(b).getStreets().size()];
                                            for (int c = 0; c < city.get(a).getDistricts().get(b).getWards().size(); c++) {
                                                wardsName[c] = city.get(a).getDistricts().get(b).getWards().get(c).getName();
                                            }
                                            for (int c = 0; c < city.get(a).getDistricts().get(b).getStreets().size(); c++) {
                                                streetName[c] = city.get(a).getDistricts().get(b).getStreets().get(c).getName();
                                            }
                                            ArrayAdapter spinner_watch_wards_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, wardsName);
                                            spinner_clothes_wardsTo.setAdapter(spinner_watch_wards_adapter);
                                            ArrayAdapter spinner_watch_street_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, streetName);
                                            spinner_clothes_streetTo.setAdapter(spinner_watch_street_adapter);
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    post.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (addressFrom.getText().toString().matches("")
                                    || addressTo.getText().toString().matches("")
                                    || phoneFrom.getText().toString().matches("")
                                    || phoneTo.getText().toString().matches("")) {
                                Toast.makeText(CreatePostActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            } else {
                                postClick(type,
                                        item,
                                        addressFrom.getText().toString(),
                                        addressTo.getText().toString(),
                                        phoneFrom.getText().toString(),
                                        phoneTo.getText().toString(),
                                        spinner.getSelectedItem().toString(),
                                        spinner_ship_cost_clothes.getSelectedItem().toString(),
                                        spinner_clothes_cityFrom.getSelectedItem().toString(),
                                        spinner_clothes_cityTo.getSelectedItem().toString(),
                                        spinner_clothes_districtFrom.getSelectedItem().toString(),
                                        spinner_clothes_districtTo.getSelectedItem().toString(),
                                        spinner_clothes_wardsFrom.getSelectedItem().toString(),
                                        spinner_clothes_wardsTo.getSelectedItem().toString(),
                                        spinner_clothes_streetFrom.getSelectedItem().toString(),
                                        spinner_clothes_streetTo.getSelectedItem().toString(),
                                        checkbox_clothes_fragile.isChecked());
                            }
                        }
                    });
                    get_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getImage(someActivityResultLauncher);
                        }
                    });
                    linear_layout.addView(view);
                    break;
                case "Đồng hồ":
                    View view1 = inflater.inflate(R.layout.layout_clothes_watch, linear_layout, false);
                    String[] watchType = {"Đồng hồ cơ", "Đồng hồ điện tử", "Đồng hồ thông minh"};
                    Spinner spinner_watch_cityFrom = view1.findViewById(R.id.dropdown_cityFrom);
                    Spinner spinner_watch_cityTo = view1.findViewById(R.id.dropdown_cityTo);
                    Spinner spinner_watch_districtFrom = view1.findViewById(R.id.dropdown_districtFrom);
                    Spinner spinner_watch_districtTo = view1.findViewById(R.id.dropdown_districtTo);
                    Spinner spinner_watch_wardsFrom = view1.findViewById(R.id.dropdown_wardsFrom);
                    Spinner spinner_watch_wardsTo = view1.findViewById(R.id.dropdown_wardsTo);
                    Spinner spinner_watch_streetFrom = view1.findViewById(R.id.dropdown_streetFrom);
                    Spinner spinner_watch_streetTo = view1.findViewById(R.id.dropdown_streetTo);

                    Spinner spinner_watch = view1.findViewById((R.id.drop_down_watch));
                    Spinner spinner_ship_cost_watch = view1.findViewById(R.id.drop_down_ship_cost);

                    CheckBox checkbox_watch_fragile = view1.findViewById(R.id.checkbox_fragile);
                    addressFrom = view1.findViewById(R.id.addressFrom);
                    addressTo = view1.findViewById(R.id.addressTo);
                    phoneFrom = view1.findViewById(R.id.phoneFrom);
                    phoneTo = view1.findViewById(R.id.phoneTo);
                    get_image = view1.findViewById(R.id.btn_get_img);
                    fromWrap = view1.findViewById(R.id.fromWrap);
                    autoAddress = view1.findViewById(R.id.autoAddress);
                    editAddress = view1.findViewById(R.id.editAddress);
                    autoAddressWrap = view1.findViewById(R.id.autoAddressWrap);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Users user = snapshot.getValue(Users.class);
                            phoneFrom.setText(user.getPhone());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    autoAddress.setText(curentLocation);
                    editAddress.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(useAutoAddress){
                                fromWrap.setVisibility(View.VISIBLE);
                                autoAddress.setVisibility(View.GONE);
                                editAddress.setText("Cancel");
                                useAutoAddress= false;
                            }else{
                                fromWrap.setVisibility(View.GONE);
                                autoAddress.setVisibility(View.VISIBLE);
                                editAddress.setText("Sửa");
                                useAutoAddress=true;
                            }

                        }
                    });


                    ArrayAdapter watchAdapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, watchType);
                    ArrayAdapter shipCostAdapter1 = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, payshipType);
                    ArrayAdapter spinner_watch_city_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, cityName);
                    spinner_watch.setAdapter(watchAdapter);
                    spinner_ship_cost_watch.setAdapter(shipCostAdapter1);
                    spinner_watch_cityFrom.setAdapter(spinner_watch_city_adapter);
                    spinner_watch_cityTo.setAdapter(spinner_watch_city_adapter);

                    spinner_watch_cityFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            for (int a = 0; a < city.size(); a++) {
                                if (spinner_watch_cityFrom.getSelectedItem().toString() == city.get(a).getName()) {
                                    districtName = new String[city.get(a).getDistricts().size()];
                                    for (int j = 0; j < city.get(a).getDistricts().size(); j++) {
                                        districtName[j] = city.get(a).getDistricts().get(j).getName();
                                    }
                                    ArrayAdapter spinner_watch_district_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, districtName);
                                    spinner_watch_districtFrom.setAdapter(spinner_watch_district_adapter);
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spinner_watch_districtFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            for (int a = 0; a < city.size(); a++) {
                                if (spinner_watch_cityFrom.getSelectedItem().toString() == city.get(a).getName()) {
                                    for (int b = 0; b < city.get(a).getDistricts().size(); b++) {
                                        if (spinner_watch_districtFrom.getSelectedItem().toString() == city.get(a).getDistricts().get(b).getName()) {
                                            wardsName = new String[city.get(a).getDistricts().get(b).getWards().size()];
                                            streetName = new String[city.get(a).getDistricts().get(b).getStreets().size()];
                                            for (int c = 0; c < city.get(a).getDistricts().get(b).getWards().size(); c++) {
                                                wardsName[c] = city.get(a).getDistricts().get(b).getWards().get(c).getName();
                                            }
                                            for (int c = 0; c < city.get(a).getDistricts().get(b).getStreets().size(); c++) {
                                                streetName[c] = city.get(a).getDistricts().get(b).getStreets().get(c).getName();
                                            }
                                            ArrayAdapter spinner_watch_wards_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, wardsName);
                                            spinner_watch_wardsFrom.setAdapter(spinner_watch_wards_adapter);
                                            ArrayAdapter spinner_watch_street_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, streetName);
                                            spinner_watch_streetFrom.setAdapter(spinner_watch_street_adapter);
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spinner_watch_cityTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Log.d("CitySelected", city.get(i).getName());
                            for (int a = 0; a < city.size(); a++) {
                                if (spinner_watch_cityTo.getSelectedItem().toString() == city.get(a).getName()) {
                                    districtName = new String[city.get(a).getDistricts().size()];
                                    for (int j = 0; j < city.get(a).getDistricts().size(); j++) {
                                        districtName[j] = city.get(a).getDistricts().get(j).getName();
                                    }
                                    ArrayAdapter spinner_watch_district_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, districtName);
                                    spinner_watch_districtTo.setAdapter(spinner_watch_district_adapter);
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spinner_watch_districtTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            for (int a = 0; a < city.size(); a++) {
                                if (spinner_watch_cityTo.getSelectedItem().toString() == city.get(a).getName()) {
                                    for (int b = 0; b < city.get(a).getDistricts().size(); b++) {
                                        if (spinner_watch_districtTo.getSelectedItem().toString() == city.get(a).getDistricts().get(b).getName()) {
                                            wardsName = new String[city.get(a).getDistricts().get(b).getWards().size()];
                                            streetName = new String[city.get(a).getDistricts().get(b).getStreets().size()];
                                            for (int c = 0; c < city.get(a).getDistricts().get(b).getWards().size(); c++) {
                                                wardsName[c] = city.get(a).getDistricts().get(b).getWards().get(c).getName();
                                            }
                                            for (int c = 0; c < city.get(a).getDistricts().get(b).getStreets().size(); c++) {
                                                streetName[c] = city.get(a).getDistricts().get(b).getStreets().get(c).getName();
                                            }
                                            ArrayAdapter spinner_watch_wards_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, wardsName);
                                            spinner_watch_wardsTo.setAdapter(spinner_watch_wards_adapter);
                                            ArrayAdapter spinner_watch_street_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, streetName);
                                            spinner_watch_streetTo.setAdapter(spinner_watch_street_adapter);
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
//                   ArrayAdapter spinner_watch_district_adapter = new ArrayAdapter(CreatePostActivity.this,android.R.layout.simple_spinner_item, districtName);
//                   spinner_watch_districtFrom.setAdapter(spinner_watch_district_adapter);
//                   spinner_watch_districtTo.setAdapter(spinner_watch_district_adapter);
                    post.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!useAutoAddress&&addressFrom.getText().toString().matches("")
                                    || addressTo.getText().toString().matches("")
                                    || phoneFrom.getText().toString().matches("")
                                    || phoneTo.getText().toString().matches("")) {
                                Toast.makeText(CreatePostActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            } else {
                                if(useAutoAddress){
                                    postClick(type,
                                            item,
                                            address,
                                            addressTo.getText().toString(),
                                            phoneFrom.getText().toString(),
                                            phoneTo.getText().toString(),
                                            spinner_watch.getSelectedItem().toString(),
                                            spinner_ship_cost_watch.getSelectedItem().toString(),
                                            cit,
                                            spinner_watch_cityTo.getSelectedItem().toString(),
                                            district,
                                            spinner_watch_districtTo.getSelectedItem().toString(),
                                            ward,
                                            spinner_watch_wardsTo.getSelectedItem().toString(),
                                            street,
                                            spinner_watch_streetTo.getSelectedItem().toString(), checkbox_watch_fragile.isChecked());
                                }else{
                                    postClick(type,
                                            item,
                                            addressFrom.getText().toString(),
                                            addressTo.getText().toString(),
                                            phoneFrom.getText().toString(),
                                            phoneTo.getText().toString(),
                                            spinner_watch.getSelectedItem().toString(),
                                            spinner_ship_cost_watch.getSelectedItem().toString(),
                                            spinner_watch_cityFrom.getSelectedItem().toString(),
                                            spinner_watch_cityTo.getSelectedItem().toString(),
                                            spinner_watch_districtFrom.getSelectedItem().toString(),
                                            spinner_watch_districtTo.getSelectedItem().toString(),
                                            spinner_watch_wardsFrom.getSelectedItem().toString(),
                                            spinner_watch_wardsTo.getSelectedItem().toString(),
                                            spinner_watch_streetFrom.getSelectedItem().toString(),
                                            spinner_watch_streetTo.getSelectedItem().toString(), checkbox_watch_fragile.isChecked());
                                }

                            }
                        }
                    });
                    get_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getImage(someActivityResultLauncher);
                        }
                    });
                    linear_layout.addView(view1);
                    break;
                case "Giày dép":
                    Log.d("Testtttttttttttttt", item);
                    View view2 = inflater.inflate(R.layout.layout_clothes_shoes, linear_layout, false);
                    String[] shoesType = {"Giày nam", "Giày nữ", "Giày cao gót"};
                    Spinner spinner_ship_cost_shoes = view2.findViewById(R.id.drop_down_ship_cost);
                    Spinner spinner2 = view2.findViewById((R.id.drop_down_shoes));
                    addressFrom = view2.findViewById(R.id.addressFrom);
                    addressTo = view2.findViewById(R.id.addressTo);
                    phoneFrom = view2.findViewById(R.id.phoneFrom);
                    phoneTo = view2.findViewById(R.id.phoneTo);
                    get_image = view2.findViewById(R.id.btn_get_img);

                    CheckBox checkbox_shoes_fragile = view2.findViewById(R.id.checkbox_fragile);

                    ArrayAdapter shoesAdapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, shoesType);
                    ArrayAdapter shipCostAdapter2 = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, payshipType);
                    spinner2.setAdapter(shoesAdapter);
                    spinner_ship_cost_shoes.setAdapter(shipCostAdapter2);
                    Spinner spinner_shoes_cityFrom = view2.findViewById(R.id.dropdown_cityFrom);
                    Spinner spinner_shoes_cityTo = view2.findViewById(R.id.dropdown_cityTo);
                    Spinner spinner_shoes_districtFrom = view2.findViewById(R.id.dropdown_districtFrom);
                    Spinner spinner_shoes_districtTo = view2.findViewById(R.id.dropdown_districtTo);
                    Spinner spinner_shoes_wardsFrom = view2.findViewById(R.id.dropdown_wardsFrom);
                    Spinner spinner_shoes_wardsTo = view2.findViewById(R.id.dropdown_wardsTo);
                    Spinner spinner_shoes_streetFrom = view2.findViewById(R.id.dropdown_streetFrom);
                    Spinner spinner_shoes_streetTo = view2.findViewById(R.id.dropdown_streetTo);

                    ArrayAdapter spinner_shoes_city_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, cityName);
                    spinner_shoes_cityFrom.setAdapter(spinner_shoes_city_adapter);
                    spinner_shoes_cityTo.setAdapter(spinner_shoes_city_adapter);

                    spinner_shoes_cityFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            for (int a = 0; a < city.size(); a++) {
                                if (spinner_shoes_cityFrom.getSelectedItem().toString() == city.get(a).getName()) {
                                    districtName = new String[city.get(a).getDistricts().size()];
                                    for (int j = 0; j < city.get(a).getDistricts().size(); j++) {
                                        districtName[j] = city.get(a).getDistricts().get(j).getName();
                                    }
                                    ArrayAdapter spinner_watch_district_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, districtName);
                                    spinner_shoes_districtFrom.setAdapter(spinner_watch_district_adapter);
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spinner_shoes_districtFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            for (int a = 0; a < city.size(); a++) {
                                if (spinner_shoes_cityFrom.getSelectedItem().toString() == city.get(a).getName()) {
                                    for (int b = 0; b < city.get(a).getDistricts().size(); b++) {
                                        if (spinner_shoes_districtFrom.getSelectedItem().toString() == city.get(a).getDistricts().get(b).getName()) {
                                            wardsName = new String[city.get(a).getDistricts().get(b).getWards().size()];
                                            streetName = new String[city.get(a).getDistricts().get(b).getStreets().size()];
                                            for (int c = 0; c < city.get(a).getDistricts().get(b).getWards().size(); c++) {
                                                wardsName[c] = city.get(a).getDistricts().get(b).getWards().get(c).getName();
                                            }
                                            for (int c = 0; c < city.get(a).getDistricts().get(b).getStreets().size(); c++) {
                                                streetName[c] = city.get(a).getDistricts().get(b).getStreets().get(c).getName();
                                            }
                                            ArrayAdapter spinner_watch_wards_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, wardsName);
                                            spinner_shoes_wardsFrom.setAdapter(spinner_watch_wards_adapter);
                                            ArrayAdapter spinner_watch_street_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, streetName);
                                            spinner_shoes_streetFrom.setAdapter(spinner_watch_street_adapter);
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spinner_shoes_cityTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Log.d("CitySelected", city.get(i).getName());
                            for (int a = 0; a < city.size(); a++) {
                                if (spinner_shoes_cityTo.getSelectedItem().toString() == city.get(a).getName()) {
                                    districtName = new String[city.get(a).getDistricts().size()];
                                    for (int j = 0; j < city.get(a).getDistricts().size(); j++) {
                                        districtName[j] = city.get(a).getDistricts().get(j).getName();
                                    }
                                    ArrayAdapter spinner_watch_district_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, districtName);
                                    spinner_shoes_districtTo.setAdapter(spinner_watch_district_adapter);
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spinner_shoes_districtTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            for (int a = 0; a < city.size(); a++) {
                                if (spinner_shoes_cityTo.getSelectedItem().toString() == city.get(a).getName()) {
                                    for (int b = 0; b < city.get(a).getDistricts().size(); b++) {
                                        if (spinner_shoes_districtTo.getSelectedItem().toString() == city.get(a).getDistricts().get(b).getName()) {
                                            wardsName = new String[city.get(a).getDistricts().get(b).getWards().size()];
                                            streetName = new String[city.get(a).getDistricts().get(b).getStreets().size()];
                                            for (int c = 0; c < city.get(a).getDistricts().get(b).getWards().size(); c++) {
                                                wardsName[c] = city.get(a).getDistricts().get(b).getWards().get(c).getName();
                                            }
                                            for (int c = 0; c < city.get(a).getDistricts().get(b).getStreets().size(); c++) {
                                                streetName[c] = city.get(a).getDistricts().get(b).getStreets().get(c).getName();
                                            }
                                            ArrayAdapter spinner_watch_wards_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, wardsName);
                                            spinner_shoes_wardsTo.setAdapter(spinner_watch_wards_adapter);
                                            ArrayAdapter spinner_watch_street_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, streetName);
                                            spinner_shoes_streetTo.setAdapter(spinner_watch_street_adapter);
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    post.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (addressFrom.getText().toString().matches("")
                                    || addressTo.getText().toString().matches("")
                                    || phoneFrom.getText().toString().matches("")
                                    || phoneTo.getText().toString().matches("")) {
                                Toast.makeText(CreatePostActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            } else {
                                postClick(type,
                                        item,
                                        addressFrom.getText().toString(),
                                        addressTo.getText().toString(),
                                        phoneFrom.getText().toString(),
                                        phoneTo.getText().toString(),
                                        spinner2.getSelectedItem().toString(),
                                        spinner_ship_cost_shoes.getSelectedItem().toString(),
                                        spinner_shoes_cityFrom.getSelectedItem().toString(),
                                        spinner_shoes_cityTo.getSelectedItem().toString(),
                                        spinner_shoes_districtFrom.getSelectedItem().toString(),
                                        spinner_shoes_districtTo.getSelectedItem().toString(),
                                        spinner_shoes_wardsFrom.getSelectedItem().toString(),
                                        spinner_shoes_wardsTo.getSelectedItem().toString(),
                                        spinner_shoes_streetFrom.getSelectedItem().toString(),
                                        spinner_shoes_streetTo.getSelectedItem().toString(),
                                        checkbox_shoes_fragile.isChecked());
                            }
                        }
                    });
                    get_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getImage(someActivityResultLauncher);
                        }
                    });
                    linear_layout.addView(view2);
                    break;
                case "Túi xách":
                    Log.d("Testtttttttttttttt", item);
                    break;
                default:
                    break;
            }
        } else if (type.equals("devices")) {
            switch (item) {
                case "Điện thoại":
                    View view = inflater.inflate(R.layout.layout_clothes_clothes, linear_layout, false);
                    String[] clothesType = {"Váy", "Quần áo nam", "Quàn áo nữ"};

                    Spinner spinner = view.findViewById((R.id.drop_down_clothes));
                    Spinner spinner_ship_cost_clothes = view.findViewById(R.id.drop_down_ship_cost);
                    addressFrom = view.findViewById(R.id.addressFrom);
                    addressTo = view.findViewById(R.id.addressTo);
                    phoneFrom = view.findViewById(R.id.phoneTo);
                    get_image = view.findViewById(R.id.btn_get_img);
                    CheckBox checkbox_clothes_fragile = view.findViewById(R.id.checkbox_fragile);
                    ArrayAdapter clothesAdapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, clothesType);
                    ArrayAdapter shipCostAdapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, clothesType);
                    spinner.setAdapter(clothesAdapter);
                    spinner_ship_cost_clothes.setAdapter(shipCostAdapter);
                    Spinner spinner_clothes_cityFrom = view.findViewById(R.id.dropdown_cityFrom);
                    Spinner spinner_clothes_cityTo = view.findViewById(R.id.dropdown_cityTo);
                    Spinner spinner_clothes_districtFrom = view.findViewById(R.id.dropdown_districtFrom);
                    Spinner spinner_clothes_districtTo = view.findViewById(R.id.dropdown_districtTo);
                    Spinner spinner_clothes_wardsFrom = view.findViewById(R.id.dropdown_wardsFrom);
                    Spinner spinner_clothes_wardsTo = view.findViewById(R.id.dropdown_wardsTo);
                    Spinner spinner_clothes_streetFrom = view.findViewById(R.id.dropdown_streetFrom);
                    Spinner spinner_clothes_streetTo = view.findViewById(R.id.dropdown_streetTo);

                    ArrayAdapter spinner_clothes_city_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, cityName);
                    spinner_clothes_cityFrom.setAdapter(spinner_clothes_city_adapter);
                    spinner_clothes_cityTo.setAdapter(spinner_clothes_city_adapter);

                    spinner_clothes_cityFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            for (int a = 0; a < city.size(); a++) {
                                if (spinner_clothes_cityFrom.getSelectedItem().toString() == city.get(a).getName()) {
                                    districtName = new String[city.get(a).getDistricts().size()];
                                    for (int j = 0; j < city.get(a).getDistricts().size(); j++) {
                                        districtName[j] = city.get(a).getDistricts().get(j).getName();
                                    }
                                    ArrayAdapter spinner_watch_district_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, districtName);
                                    spinner_clothes_districtFrom.setAdapter(spinner_watch_district_adapter);
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spinner_clothes_districtFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            for (int a = 0; a < city.size(); a++) {
                                if (spinner_clothes_cityFrom.getSelectedItem().toString() == city.get(a).getName()) {
                                    for (int b = 0; b < city.get(a).getDistricts().size(); b++) {
                                        if (spinner_clothes_districtFrom.getSelectedItem().toString() == city.get(a).getDistricts().get(b).getName()) {
                                            wardsName = new String[city.get(a).getDistricts().get(b).getWards().size()];
                                            streetName = new String[city.get(a).getDistricts().get(b).getStreets().size()];
                                            for (int c = 0; c < city.get(a).getDistricts().get(b).getWards().size(); c++) {
                                                wardsName[c] = city.get(a).getDistricts().get(b).getWards().get(c).getName();
                                            }
                                            for (int c = 0; c < city.get(a).getDistricts().get(b).getStreets().size(); c++) {
                                                streetName[c] = city.get(a).getDistricts().get(b).getStreets().get(c).getName();
                                            }
                                            ArrayAdapter spinner_watch_wards_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, wardsName);
                                            spinner_clothes_wardsFrom.setAdapter(spinner_watch_wards_adapter);
                                            ArrayAdapter spinner_watch_street_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, streetName);
                                            spinner_clothes_streetFrom.setAdapter(spinner_watch_street_adapter);
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spinner_clothes_cityTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Log.d("CitySelected", city.get(i).getName());
                            for (int a = 0; a < city.size(); a++) {
                                if (spinner_clothes_cityTo.getSelectedItem().toString() == city.get(a).getName()) {
                                    districtName = new String[city.get(a).getDistricts().size()];
                                    for (int j = 0; j < city.get(a).getDistricts().size(); j++) {
                                        districtName[j] = city.get(a).getDistricts().get(j).getName();
                                    }
                                    ArrayAdapter spinner_watch_district_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, districtName);
                                    spinner_clothes_districtTo.setAdapter(spinner_watch_district_adapter);
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spinner_clothes_districtTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            for (int a = 0; a < city.size(); a++) {
                                if (spinner_clothes_cityTo.getSelectedItem().toString() == city.get(a).getName()) {
                                    for (int b = 0; b < city.get(a).getDistricts().size(); b++) {
                                        if (spinner_clothes_districtTo.getSelectedItem().toString() == city.get(a).getDistricts().get(b).getName()) {
                                            wardsName = new String[city.get(a).getDistricts().get(b).getWards().size()];
                                            streetName = new String[city.get(a).getDistricts().get(b).getStreets().size()];
                                            for (int c = 0; c < city.get(a).getDistricts().get(b).getWards().size(); c++) {
                                                wardsName[c] = city.get(a).getDistricts().get(b).getWards().get(c).getName();
                                            }
                                            for (int c = 0; c < city.get(a).getDistricts().get(b).getStreets().size(); c++) {
                                                streetName[c] = city.get(a).getDistricts().get(b).getStreets().get(c).getName();
                                            }
                                            ArrayAdapter spinner_watch_wards_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, wardsName);
                                            spinner_clothes_wardsTo.setAdapter(spinner_watch_wards_adapter);
                                            ArrayAdapter spinner_watch_street_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, streetName);
                                            spinner_clothes_streetTo.setAdapter(spinner_watch_street_adapter);
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    post.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (addressFrom.getText().toString().matches("")
                                    || addressTo.getText().toString().matches("")
                                    || phoneFrom.getText().toString().matches("")
                                    || phoneTo.getText().toString().matches("")) {
                                Toast.makeText(CreatePostActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            } else {
                                postClick(type,
                                        item,
                                        addressFrom.getText().toString(),
                                        addressTo.getText().toString(),
                                        phoneFrom.getText().toString(),
                                        phoneTo.getText().toString(),
                                        spinner.getSelectedItem().toString(),
                                        spinner_ship_cost_clothes.getSelectedItem().toString(),
                                        spinner_clothes_cityFrom.getSelectedItem().toString(),
                                        spinner_clothes_cityTo.getSelectedItem().toString(),
                                        spinner_clothes_districtFrom.getSelectedItem().toString(),
                                        spinner_clothes_districtTo.getSelectedItem().toString(),
                                        spinner_clothes_wardsFrom.getSelectedItem().toString(),
                                        spinner_clothes_wardsTo.getSelectedItem().toString(),
                                        spinner_clothes_streetFrom.getSelectedItem().toString(),
                                        spinner_clothes_streetTo.getSelectedItem().toString(),
                                        checkbox_clothes_fragile.isChecked());
                            }
                        }
                    });
                    get_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getImage(someActivityResultLauncher);
                        }
                    });
                    linear_layout.addView(view);
                    break;
                case "Máy tính bảng":
                    View view1 = inflater.inflate(R.layout.layout_clothes_watch, linear_layout, false);
                    String[] watchType = {"Đồng hồ cơ", "Đồng hồ điện tử", "Đồng hồ thông minh"};
                    Spinner spinner_watch_cityFrom = view1.findViewById(R.id.dropdown_cityFrom);
                    Spinner spinner_watch_cityTo = view1.findViewById(R.id.dropdown_cityTo);
                    Spinner spinner_watch_districtFrom = view1.findViewById(R.id.dropdown_districtFrom);
                    Spinner spinner_watch_districtTo = view1.findViewById(R.id.dropdown_districtTo);
                    Spinner spinner_watch_wardsFrom = view1.findViewById(R.id.dropdown_wardsFrom);
                    Spinner spinner_watch_wardsTo = view1.findViewById(R.id.dropdown_wardsTo);
                    Spinner spinner_watch_streetFrom = view1.findViewById(R.id.dropdown_streetFrom);
                    Spinner spinner_watch_streetTo = view1.findViewById(R.id.dropdown_streetTo);

                    Spinner spinner_watch = view1.findViewById((R.id.drop_down_watch));
                    Spinner spinner_ship_cost_watch = view1.findViewById(R.id.drop_down_ship_cost);
                    addressFrom = view1.findViewById(R.id.addressFrom);
                    addressTo = view1.findViewById(R.id.addressTo);
                    phoneFrom = view1.findViewById(R.id.phoneFrom);
                    phoneTo = view1.findViewById(R.id.phoneTo);
                    get_image = view1.findViewById(R.id.btn_get_img);
                    CheckBox checkbox_watch_fragile = view1.findViewById(R.id.checkbox_fragile);


                    ArrayAdapter watchAdapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, watchType);
                    ArrayAdapter shipCostAdapter1 = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, payshipType);
                    ArrayAdapter spinner_watch_city_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, cityName);

                    spinner_watch.setAdapter(watchAdapter);
                    spinner_ship_cost_watch.setAdapter(shipCostAdapter1);
                    spinner_watch_cityFrom.setAdapter(spinner_watch_city_adapter);
                    spinner_watch_cityTo.setAdapter(spinner_watch_city_adapter);

                    spinner_watch_cityFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            for (int a = 0; a < city.size(); a++) {
                                if (spinner_watch_cityFrom.getSelectedItem().toString() == city.get(a).getName()) {
                                    districtName = new String[city.get(a).getDistricts().size()];
                                    for (int j = 0; j < city.get(a).getDistricts().size(); j++) {
                                        districtName[j] = city.get(a).getDistricts().get(j).getName();
                                    }
                                    ArrayAdapter spinner_watch_district_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, districtName);
                                    spinner_watch_districtFrom.setAdapter(spinner_watch_district_adapter);
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spinner_watch_districtFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            for (int a = 0; a < city.size(); a++) {
                                if (spinner_watch_cityFrom.getSelectedItem().toString() == city.get(a).getName()) {
                                    for (int b = 0; b < city.get(a).getDistricts().size(); b++) {
                                        if (spinner_watch_districtFrom.getSelectedItem().toString() == city.get(a).getDistricts().get(b).getName()) {
                                            wardsName = new String[city.get(a).getDistricts().get(b).getWards().size()];
                                            streetName = new String[city.get(a).getDistricts().get(b).getStreets().size()];
                                            for (int c = 0; c < city.get(a).getDistricts().get(b).getWards().size(); c++) {
                                                wardsName[c] = city.get(a).getDistricts().get(b).getWards().get(c).getName();
                                            }
                                            for (int c = 0; c < city.get(a).getDistricts().get(b).getStreets().size(); c++) {
                                                streetName[c] = city.get(a).getDistricts().get(b).getStreets().get(c).getName();
                                            }
                                            ArrayAdapter spinner_watch_wards_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, wardsName);
                                            spinner_watch_wardsFrom.setAdapter(spinner_watch_wards_adapter);
                                            ArrayAdapter spinner_watch_street_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, streetName);
                                            spinner_watch_streetFrom.setAdapter(spinner_watch_street_adapter);
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spinner_watch_cityTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Log.d("CitySelected", city.get(i).getName());
                            for (int a = 0; a < city.size(); a++) {
                                if (spinner_watch_cityTo.getSelectedItem().toString() == city.get(a).getName()) {
                                    districtName = new String[city.get(a).getDistricts().size()];
                                    for (int j = 0; j < city.get(a).getDistricts().size(); j++) {
                                        districtName[j] = city.get(a).getDistricts().get(j).getName();
                                    }
                                    ArrayAdapter spinner_watch_district_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, districtName);
                                    spinner_watch_districtTo.setAdapter(spinner_watch_district_adapter);
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spinner_watch_districtTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            for (int a = 0; a < city.size(); a++) {
                                if (spinner_watch_cityTo.getSelectedItem().toString() == city.get(a).getName()) {
                                    for (int b = 0; b < city.get(a).getDistricts().size(); b++) {
                                        if (spinner_watch_districtTo.getSelectedItem().toString() == city.get(a).getDistricts().get(b).getName()) {
                                            wardsName = new String[city.get(a).getDistricts().get(b).getWards().size()];
                                            streetName = new String[city.get(a).getDistricts().get(b).getStreets().size()];
                                            for (int c = 0; c < city.get(a).getDistricts().get(b).getWards().size(); c++) {
                                                wardsName[c] = city.get(a).getDistricts().get(b).getWards().get(c).getName();
                                            }
                                            for (int c = 0; c < city.get(a).getDistricts().get(b).getStreets().size(); c++) {
                                                streetName[c] = city.get(a).getDistricts().get(b).getStreets().get(c).getName();
                                            }
                                            ArrayAdapter spinner_watch_wards_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, wardsName);
                                            spinner_watch_wardsTo.setAdapter(spinner_watch_wards_adapter);
                                            ArrayAdapter spinner_watch_street_adapter = new ArrayAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_item, streetName);
                                            spinner_watch_streetTo.setAdapter(spinner_watch_street_adapter);
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
//                   ArrayAdapter spinner_watch_district_adapter = new ArrayAdapter(CreatePostActivity.this,android.R.layout.simple_spinner_item, districtName);
//                   spinner_watch_districtFrom.setAdapter(spinner_watch_district_adapter);
//                   spinner_watch_districtTo.setAdapter(spinner_watch_district_adapter);
                    post.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (addressFrom.getText().toString().matches("")
                                    || addressTo.getText().toString().matches("")
                                    || phoneFrom.getText().toString().matches("")
                                    || phoneTo.getText().toString().matches("")) {
                                Toast.makeText(CreatePostActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            } else {
                                postClick(type,
                                        item,
                                        addressFrom.getText().toString(),
                                        addressTo.getText().toString(),
                                        phoneFrom.getText().toString(),
                                        phoneTo.getText().toString(),
                                        spinner_watch.getSelectedItem().toString(),
                                        spinner_ship_cost_watch.getSelectedItem().toString(),
                                        spinner_watch_cityFrom.getSelectedItem().toString(),
                                        spinner_watch_cityTo.getSelectedItem().toString(),
                                        spinner_watch_districtFrom.getSelectedItem().toString(),
                                        spinner_watch_districtTo.getSelectedItem().toString(),
                                        spinner_watch_wardsFrom.getSelectedItem().toString(),
                                        spinner_watch_wardsTo.getSelectedItem().toString(),
                                        spinner_watch_streetFrom.getSelectedItem().toString(),
                                        spinner_watch_streetTo.getSelectedItem().toString(),
                                        checkbox_watch_fragile.isChecked());
                            }
                        }
                    });
                    get_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getImage(someActivityResultLauncher);
                        }
                    });
                    linear_layout.addView(view1);
                    break;
                default:
                    break;
            }
        }

    }

    private String loadJson() {
        String json = null;
        try {
            InputStream is = getAssets().open("local.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    private void getImage(ActivityResultLauncher<Intent> someActivityResultLauncher) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intent);
    }

    private void postClick(String type, String item, String addressFrom, String addressTo, String phoneFrom, String phoneTo, String spinnerType, String spinnerShip,
                           String cityFrom, String cityTo, String districtFrom, String districtTo, String wardsFrom, String wardsTo, String streetFrom, String streetTo, Boolean fragile) {
        //Toast.makeText(CreatePostActivity.this,type+item,Toast.LENGTH_SHORT).show();
        final BottomSheetDialog dialog = new BottomSheetDialog(this,R.style.MaterialDialogSheet);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        View view = inflater.inflate(R.layout.popup_confirm_post,null);
        TextView postCon, typeCon, addressFromCon, addressToCon, distance,shipFee,shipPayment,fragileCon;
        postCon = view.findViewById(R.id.post);
        typeCon = view.findViewById(R.id.type);
        addressFromCon = view.findViewById(R.id.from);
        addressToCon = view.findViewById(R.id.to);
        distance = view.findViewById(R.id.distance);
        shipFee = view.findViewById(R.id.shipFee);
        shipPayment = view.findViewById(R.id.shipPayment);
        fragileCon = view.findViewById(R.id.fragile);
        sumFee = view.findViewById(R.id.sumFee);
        sliderView = view.findViewById(R.id.slider);
        for(int i = 0;i<imagePath.size();i++){
            sliderDataArrayList.add(new SliderData(imagePath.get(i).toString()));
        }
        SliderAdapter adapter = new SliderAdapter( sliderDataArrayList);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setInfiniteAdapterEnabled(false);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        typeCon.setText(typeCon.getText().toString()+spinnerType);
        addressFromCon.setText(addressFromCon.getText().toString()+addressFrom+" Đường "+ streetFrom + ", "+wardsFrom+", "+districtFrom+", "+cityFrom);
        addressToCon.setText(addressToCon.getText().toString()+addressTo+" Đường "+ streetTo + ", "+wardsTo+", "+districtTo+", "+cityTo);
        shipPayment.setText(shipPayment.getText().toString()+spinnerShip);
        if(fragile){
            fragileCon.setVisibility(View.VISIBLE);
        }else{
            fragileCon.setVisibility(View.GONE);
        }
        GetDistance getDistance = new GetDistance(CreatePostActivity.this);
        try {
            float distances = getDistance.run();
            distance.setText(distance.getText().toString()+(distances)+" km");
            if(distances<1){
                shipFee.setText(shipFee.getText().toString()+"15,000 vnđ");
                if(fragile){
                    sumFee.setText(sumFee.getText().toString()+"20,000 vnđ");
                }else{
                    sumFee.setText(sumFee.getText().toString()+"15,000 vnđ");
                }
            }else{
                int n = (int) distances;
                editFeeString(String.valueOf(120000000000l));
                shipFee.setText(shipFee.getText().toString()+editFeeString(String.valueOf((long)15000*(n+1)))+" vnđ");
                if(fragile){
                    sumFee.setText(sumFee.getText().toString()+editFeeString(String.valueOf((long)15000*(n+1)+5000))+"vnđ");
                }else{
                    sumFee.setText(sumFee.getText().toString()+editFeeString(String.valueOf((long)15000*(n+1)))+" vnđ");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setContentView(view);
        dialog.show();
        postCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                final ProgressDialog pd = new ProgressDialog(CreatePostActivity.this);
                pd.setMessage("Uploading...");
                pd.show();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                for (int i = 0; i < imagePath.size(); i++) {
                    StorageReference storageReference = storage.getReferenceFromUrl("gs://crowdshipping-387fa.appspot.com").child("images/"
                            + UUID.randomUUID().toString());
                    int finalI = i;
                    storageReference.putFile((imagePath.get(i))).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @NonNull
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return storageReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadURI = task.getResult();
                                String mURI = downloadURI.toString();
                                imageURI.put("Image" + finalI, mURI);
                                HashMap<String, String> AddressFrom = new HashMap<>();
                                AddressFrom.put("City", cityFrom);
                                AddressFrom.put("District", districtFrom);
                                AddressFrom.put("Wards", wardsFrom);
                                AddressFrom.put("Streets", streetFrom);
                                AddressFrom.put("Address", addressFrom);
                                HashMap<String, String> AddressTo = new HashMap<>();
                                AddressTo.put("City", cityTo);
                                AddressTo.put("District", districtTo);
                                AddressTo.put("Wards", wardsTo);
                                AddressTo.put("Streets", streetTo);
                                AddressTo.put("Address", addressTo);

                                if (finalI + 1 == imagePath.size()) {
                                    postToDatabase(pd, type, item, AddressFrom, AddressTo, phoneFrom, phoneTo, spinnerType, spinnerShip, fragile);
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    });
                }
            }
        });
    }

    private void postToDatabase(ProgressDialog pd, String type, String item, HashMap<String, String> addressFrom, HashMap<String, String> addressTo, String phoneFrom, String phoneTo, String spinnerType, String spinnerShip, Boolean fragile) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Post");
        DatabaseReference pushedPostRef = reference.push();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("PostID", pushedPostRef.getKey());
        hashMap.put("CreateID", userid);
        hashMap.put("AddressFrom", addressFrom);
        hashMap.put("AddressTo", addressTo);
        hashMap.put("phoneFrom", phoneFrom);
        hashMap.put("phoneTo", phoneTo);
        hashMap.put("Type", type + "-" + item);
        hashMap.put("Description", spinnerType);
        hashMap.put("Ship", spinnerShip);
        hashMap.put("linkImage", imageURI);
        hashMap.put("Time", currentDateandTime);
        hashMap.put("Status", "0");
        hashMap.put("Fragile", fragile);
        pushedPostRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                new AlertDialog.Builder(CreatePostActivity.this)
                        .setTitle("Thông báo")
                        .setMessage("Tạo đơn hàng thành công")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(CreatePostActivity.this, HomeActivity.class);
                                CreatePostActivity.this.finish();
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        });
    }

    private void initNavigation() {
        if (MapboxNavigationProvider.isCreated()) {
            mapboxNavigation = MapboxNavigationProvider.retrieve();
            Log.d("CurrLocation", "isCreated");
        } else {
            Log.d("CurrLocation", "notCreated");
            mapboxNavigation = MapboxNavigationProvider.create(
                    new NavigationOptions.Builder(getApplication())
                            .accessToken(getString(R.string.mapbox_access_token))
                            // comment out the location engine setting block to disable simulation
//                            .locationEngine(replayLocationEngine)
                            .build()
            );
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mapboxNavigation.startTripSession();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapboxNavigation.registerLocationObserver(locationObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MapboxNavigationProvider.destroy();
        mapboxNavigation.unregisterLocationObserver(locationObserver);
    }
    private String editFeeString(String fee){
        StringBuilder stringBuilder = new StringBuilder(fee);
        int count =0;
        if(stringBuilder.length()%3==0){
            count = stringBuilder.length()/3-1;
        }else{
            count = stringBuilder.length()/3;
        }
        int[] pos = new int[count];
        int i = 1;
        while( i <= count){
            pos[i-1] = stringBuilder.length()-i*3;
            i++;
        }

        for(int j =0; j<count;j++){

            stringBuilder.insert(pos[j],",");
        }
        Log.d("Countttttt", String.valueOf(stringBuilder));
        return String.valueOf(stringBuilder);
    }
}