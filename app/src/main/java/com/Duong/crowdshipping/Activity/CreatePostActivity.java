package com.Duong.crowdshipping.Activity;

import static android.os.Build.VERSION_CODES.P;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Duong.crowdshipping.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CreatePostActivity extends AppCompatActivity {
    String type, item;
    LayoutInflater inflater;
    LinearLayout linear_layout;
    TextView post;
    EditText addressFrom, addressTo, phoneFrom, phoneTo;
    Button get_image;
    List<Uri> imagePath = new ArrayList<>();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    String userid = firebaseUser.getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.green));

        post = findViewById(R.id.post);
        linear_layout = findViewById(R.id.linear_layout);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            type = extras.getString("type"," ");
            item = extras.getString("item"," ");
        }
        inflater = LayoutInflater.from(CreatePostActivity.this);
        String[] payshipType = {"Người chuyển trả", "Người nhận trả"};
        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            if(data.getClipData() != null) {
                                int count = data.getClipData().getItemCount();
                                for(int i = 0; i < count; i++){
                                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                                    imagePath.add(imageUri);
                                    Log.d("AAAAAAAA",imagePath.toString());
                                }
                            }else{
                                imagePath.add(data.getData());
                                Log.d("AAAAAAAA",imagePath.toString());
                            }
                        }
                    }
                });
        if(type.equals("clothes")){
           switch (item){
               case "Quần áo":
                   View view = inflater.inflate(R.layout.layout_clothes_watch,linear_layout,false);
                   String[] clothesType = {"Đồng hồ cơ", "Đồng hồ điện tử","Đồng hồ thông minh"};
                   Spinner spinner = view.findViewById((R.id.drop_down_watch));
                   Spinner spinner_ship_cost_clothes = view.findViewById(R.id.drop_down_ship_cost);
                   ArrayAdapter clothesAdapter = new ArrayAdapter(CreatePostActivity.this,android.R.layout.simple_spinner_item,clothesType);
                   ArrayAdapter shipCostAdapter = new ArrayAdapter(CreatePostActivity.this,android.R.layout.simple_spinner_item,clothesType);
                   spinner.setAdapter(clothesAdapter);
                   spinner_ship_cost_clothes.setAdapter(shipCostAdapter);
                   linear_layout.addView(view);
                   break;
               case "Đồng hồ":
                   View view1 = inflater.inflate(R.layout.layout_clothes_watch,linear_layout,false);
                   String[] watchType = {"Đồng hồ cơ", "Đồng hồ điện tử","Đồng hồ thông minh"};
                   Spinner spinner_watch = view1.findViewById((R.id.drop_down_watch));
                   Spinner spinner_ship_cost_watch = view1.findViewById(R.id.drop_down_ship_cost);
                   addressFrom = view1.findViewById(R.id.addressFrom);
                   addressTo = view1.findViewById(R.id.addressTo);
                   phoneFrom = view1.findViewById(R.id.phoneFrom);
                   phoneTo = view1.findViewById(R.id.phoneTo);
                   get_image = view1.findViewById(R.id.btn_get_img);
                   ArrayAdapter watchAdapter = new ArrayAdapter(CreatePostActivity.this,android.R.layout.simple_spinner_item,watchType);
                   ArrayAdapter shipCostAdapter1 = new ArrayAdapter(CreatePostActivity.this,android.R.layout.simple_spinner_item,payshipType);
                   spinner_watch.setAdapter(watchAdapter);
                   spinner_ship_cost_watch.setAdapter(shipCostAdapter1);
                   post.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           if(addressFrom.getText().toString().matches("")
                                   || addressTo.getText().toString().matches("")
                                   || phoneFrom.getText().toString().matches("")
                                   || phoneTo.getText().toString().matches(""))
                           {
                               Toast.makeText(CreatePostActivity.this,"Error",Toast.LENGTH_SHORT).show();
                           }else {
                               postClick(type,
                                       item,
                                       addressFrom.getText().toString(),
                                       addressTo.getText().toString(),
                                       phoneFrom.getText().toString(),
                                       phoneTo.getText().toString(),
                                       spinner_watch.getSelectedItem().toString(),
                                       spinner_ship_cost_watch.getSelectedItem().toString()
                                       );
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
                   View view2 = inflater.inflate(R.layout.layout_clothes_watch,linear_layout,false);
                   String[] shoesType = {"Giày nam", "Giày nữ","Giày cao gót"};
                   Spinner spinner_ship_cost_shoes = view2.findViewById(R.id.drop_down_ship_cost);
                   Spinner spinner2 = view2.findViewById((R.id.drop_down_watch));
                   ArrayAdapter shoesAdapter = new ArrayAdapter(CreatePostActivity.this,android.R.layout.simple_spinner_item,shoesType);
                   ArrayAdapter shipCostAdapter2 = new ArrayAdapter(CreatePostActivity.this,android.R.layout.simple_spinner_item,payshipType);
                   spinner2.setAdapter(shoesAdapter);
                   spinner_ship_cost_shoes.setAdapter(shipCostAdapter2);
                   linear_layout.addView(view2);
                   break;
               case "Túi xách":
                   Log.d("Testtttttttttttttt", item);
                   break;
               default:
                   break;
           }
        }

    }

    private void getImage(ActivityResultLauncher<Intent> someActivityResultLauncher) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intent);
    }

    private void postClick(String type, String item, String addressFrom,String addressTo,String phoneFrom,String phoneTo,String spinnerType,String spinnerShip) {
        //Toast.makeText(CreatePostActivity.this,type+item,Toast.LENGTH_SHORT).show();
        ProgressDialog progressDialog
                = new ProgressDialog(this);
        progressDialog.setTitle("Uploading "+ imagePath.size()+" image ...");
        progressDialog.show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Post").child(type).child(item);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("CreateID",userid);
        hashMap.put("AddressFrom",addressFrom);
        hashMap.put("AddressTo",addressTo);
        hashMap.put("phoneFrom",phoneFrom);
        hashMap.put("phoneTo",phoneTo);
        hashMap.put("Type",spinnerType);
        hashMap.put("Ship",spinnerShip);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        for(int i =0; i<imagePath.size();i++){
            StorageReference storageReference = storage.getReferenceFromUrl("gs://crowdshipping-387fa.appspot.com").child("images/"
                    + UUID.randomUUID().toString());
            storageReference.putFile(imagePath.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(CreatePostActivity.this,
                                    "Failed " + e.getMessage(),
                                    Toast.LENGTH_SHORT)
                            .show();
                    Log.d("AAAAAAAAAAAA", e.getMessage());
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress
                            = (100.0
                            * snapshot.getBytesTransferred()
                            / snapshot.getTotalByteCount());
                    progressDialog.setMessage(
                            "Uploaded "
                                    + (int)progress + "%");
                }
            });
        }
    }
}