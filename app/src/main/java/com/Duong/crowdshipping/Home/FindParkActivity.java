package com.Duong.crowdshipping.Home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.Duong.crowdshipping.R;
import com.Duong.crowdshipping.Extend.GetAddress;
import com.Duong.crowdshipping.model.LatLng;
import com.Duong.crowdshipping.model.Parks;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.model.Distance;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FindParkActivity extends AppCompatActivity implements OnMapReadyCallback {
    ImageButton return_current;
    //FindParkActivityListener mCallback;
    LinearLayout search_bar;
    Location currentLocation, oldLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    Polyline polyline;
    Marker marker1;
    String district;
    GoogleMap mMap;
    DatabaseReference reference;
    List<String> name = new ArrayList<>();
    List<String> id = new ArrayList<>();
    List<String> address = new ArrayList<>();
    List<MarkerOptions> mMarker = new ArrayList<>();
    FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
    String ID_car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_park);
        Window window = FindParkActivity.this.getWindow();
        window.getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
        return_current = findViewById(R.id.return_current);
        search_bar = findViewById(R.id.search_bar);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        fetchLastLocation();
        return_current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupportMapFragment mapFragment =
                        (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                if (mapFragment != null) {
                    mapFragment.getMapAsync(FindParkActivity.this::onMapReady);
                }
            }
        });
        search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        com.google.android.gms.maps.model.LatLng lng = new com.google.android.gms.maps.model.LatLng(21.04001257972256,105.80071399978721);
        MarkerOptions markerOptions = new MarkerOptions().position(lng);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(lng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lng, 15));
        mMap.addMarker(markerOptions);
        reference = FirebaseDatabase.getInstance().getReference("Parking lot").child("Hanoi");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                id.clear();
                name.clear();
                address.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Parks parks = dataSnapshot.getValue(Parks.class);
                    //Log.d("AAA",parks.getName());
                    id.add(parks.getID());
                    name.add(parks.getName());
                    address.add(parks.getAddress());
                }
                int i = 0;
                for (String ID : id){
                    Log.d("AAA",ID);
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Parking lot")
                            .child("Hanoi")
                            .child(ID)
                            .child("LatLng");
                    int finalI = i;
                    reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            LatLng lat_lng = snapshot.getValue(LatLng.class);
                            com.google.android.gms.maps.model.LatLng latLng = new com.google.android.gms.maps.model.LatLng(lat_lng.getLat(),lat_lng.getLng());
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(name.get(finalI) + "/" +ID).
                                    icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_location_on_24));
                            Marker marker = mMap.addMarker(markerOptions);
                            marker.setTag(id.get(finalI));
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                    i++;
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        //Log.d("MapFragment", String.valueOf(currentLocation.getLatitude()) + currentLocation.getLongitude());
//        NearbySearch nearbySearch = new NearbySearch(getApplicationContext());
//        if (district == null) {
//            PlacesSearchResult[] placesSearchResults = nearbySearch.run(new com.google.maps.model.LatLng(currentLocation.getLatitude(),currentLocation.getLongitude())).results;             double[] lat = new double[placesSearchResults.length];
//            double[] lng = new double[placesSearchResults.length];
//            for (int i = 0; i < placesSearchResults.length; i++) {
//                lat[i] = placesSearchResults[i].geometry.location.lat;
//                lng[i] = placesSearchResults[i].geometry.location.lng;
//                mMap.addMarker(new MarkerOptions().position(new LatLng(lat[i], lng[i])).
//                        title(placesSearchResults[i].name).
//                        icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_location_on_24)).
//                        position(new LatLng(lat[i], lng[i])));
//            }
//            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())).title("You are here.");
//            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())));
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 18));
//            mMap.addMarker(markerOptions);
//        } else {
//            List<GeocodingResult> result = new ArrayList<>();
//            com.google.maps.model.LatLng latLng1 = null;
//            String placeID = null;
//            String name = null;
//            String address = null;
//
//            result = nearbySearch.getLatlng(district);
//            for (int i = 0; i < result.size(); i++) {
//                latLng1 = result.get(i).geometry.location;
//                placeID = result.get(i).placeId;
//                address = result.get(i).formattedAddress;
//            }
//            if (placeID != null) {
//                PlaceDetails placeDetails = nearbySearch.name(placeID);
//                name = placeDetails.name;
//            }
//            PlacesSearchResult[] placesSearchResults = nearbySearch.run(latLng1).results;
//            double[] lat = new double[placesSearchResults.length];
//            double[] lng = new double[placesSearchResults.length];
//            for (int i = 0; i < placesSearchResults.length; i++) {
//                lat[i] = placesSearchResults[i].geometry.location.lat;
//                lng[i] = placesSearchResults[i].geometry.location.lng;
//                mMap.addMarker(new MarkerOptions().position(new LatLng(lat[i], lng[i])).
//                        title(placesSearchResults[i].name).
//                        icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_location_on_24)).
//                        position(new LatLng(lat[i], lng[i])));
//            }
//            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(latLng1.lat, latLng1.lng)).title((name == null) ? address : name);
//            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(latLng1.lat, latLng1.lng)));
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng1.lat, latLng1.lng), 15));
//            mMap.addMarker(markerOptions);
//        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (polyline != null) {
                    polyline.remove();
                }
                if (marker1 != null) {
                    marker1.remove();
                }
                List<com.google.maps.model.LatLng> test = new ArrayList<>();
                Distance distance = null;
                List<com.google.android.gms.maps.model.LatLng> corners = new ArrayList<>();
                com.google.android.gms.maps.model.LatLng origin = new com.google.android.gms.maps.model.LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                com.google.android.gms.maps.model.LatLng dest = marker.getPosition();
                //Get address
                //Log.d("MapFragment", marker.getTitle());
                GetAddress getAddress = new GetAddress(marker.getPosition(), getApplicationContext());
                List<Address> addresses = getAddress.run();
                //Log.d("MapFragment", addresses.get(0).getAddressLine(0));
                //Get Distance
                float[] result = new float[1];
                Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), marker.getPosition().latitude, marker.getPosition().longitude, result);
                popupDialog(marker.getTitle(),marker.getPosition(),marker.getTag());
                return false;
            }
        });
    }

    private void popupDialog(String name_park, com.google.android.gms.maps.model.LatLng position,Object id) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.fragment_item,null);
        PopupWindow window = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        int layout_height = 500;
        int layout_width = getResources().getDisplayMetrics().widthPixels - 50;
        window.setHeight(layout_height);
        window.setWidth(layout_width);
        window.setAnimationStyle(R.style.Animation);
        window.setBackgroundDrawable(getDrawable(R.drawable.background_popup_1));
        window.setOutsideTouchable(true);
        window.setFocusable(true);
        window.setTouchModal(false);
        window.update(0, 0, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.showAtLocation(layout, Gravity.BOTTOM, 0, 60);
        View container = window.getContentView().getRootView();
        if(container!= null){
            WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
            WindowManager.LayoutParams p = (WindowManager.LayoutParams)container.getLayoutParams();
            p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            p.dimAmount = 0;
            if(wm != null) {
                wm.updateViewLayout(container, p);
            }
        }
        TextView name, distance1;
        Button btn_book, btn_chat;
        btn_book = layout.findViewById(R.id.button_book_park);
        btn_chat = layout.findViewById(R.id.button_chat);
        name = layout.findViewById(R.id.name);
        distance1 = layout.findViewById(R.id.distance);
        String[] name_id = name_park.split("/");
        name.setText(name_id[0]);
        distance1.setText("0 km");
        String ID = id.toString();
        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FindParkActivity.this);
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(FindParkActivity.this);
                View view = layoutInflaterAndroid.inflate(R.layout.confirm_layout, null);
                builder.setView(view);
                builder.setCancelable(false);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                TextView btn_yes, btn_no;
                btn_yes = view.findViewById(R.id.btn_yes);
                btn_no = view.findViewById(R.id.btn_no);
                int width = getResources().getDisplayMetrics().widthPixels;
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        (width-200)/2,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                btn_yes.setLayoutParams(layoutParams);
                btn_no.setLayoutParams(layoutParams);
                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences pref = getSharedPreferences("Car_Info",MODE_PRIVATE);
                        ID_car = pref.getString("ID_car","");
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid())
                                .child("Cars")
                                .child(ID_car);
                        HashMap<String,Object> hasMap = new HashMap<>();
                        hasMap.put("status","pending");
                        ref.updateChildren(hasMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if(!task.isSuccessful()){
                                    Toast.makeText(FindParkActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Parking lot")
                                .child("Hanoi")
                                .child(ID)
                                .child("Cars")
                                .child("Pending")
                                .child(fuser.getUid())
                                .child(ID_car);
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("id",ID_car);
                        hashMap.put("vehicle",pref.getString("Name_car",""));
                        hashMap.put("plate",pref.getString("Plate_car",""));
                        reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(FindParkActivity.this, "Chờ xác nhận từ bãi đỗ xe", Toast.LENGTH_SHORT).show();
//                                    Uri gmmIntentUri = Uri.parse("google.navigation:q="+position.latitude+","+position.longitude);
//                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                                    mapIntent.setPackage("com.google.android.apps.maps");
//                                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
//                                        alertDialog.cancel();
//                                        startActivity(mapIntent);
//                                    }
                                }else{
                                    Toast.makeText(FindParkActivity.this, "Error when choose the Parking lot", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindParkActivity.this, MessageActivity.class);
                intent.putExtra("parkid", name_id[1]);
                startActivity(intent);
            }
        });
    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(FindParkActivity.this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Log.d("MapFragment", String.valueOf(location.getLatitude()) + location.getLongitude());
                }

                SupportMapFragment mapFragment =
                        (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                if (mapFragment != null) {
                    mapFragment.getMapAsync(FindParkActivity.this::onMapReady);
                }
            }
        });
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}