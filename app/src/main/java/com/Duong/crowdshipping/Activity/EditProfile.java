package com.Duong.crowdshipping.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.Duong.crowdshipping.Login.LoginActivity;
import com.Duong.crowdshipping.R;
import com.Duong.crowdshipping.model.Users;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {
    TextView  profile_name, edit,frontText,backText,postIDImg;
    EditText name, phone, email;
    FirebaseUser fuser;
    DatabaseReference reference;
    View back;
    ImageView front,backImg;
    List<Uri> imagePath = new ArrayList<>();
    HashMap<String, Object> imageURI = new HashMap<>();
    CircleImageView profile_img;
    Button showpicture, changepicture;
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Window window = EditProfile.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(EditProfile.this.getResources().getColor(R.color.green));
        window.setNavigationBarColor(EditProfile.this.getResources().getColor(android.R.color.white));

        //Text View
        edit = findViewById(R.id.edit);
        profile_name = findViewById(R.id.profile_name);
        frontText = findViewById(R.id.frontText);
        backText = findViewById(R.id.backText);
        postIDImg = findViewById(R.id.postIDImg);
        //Edit Text
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        //view
        back = findViewById(R.id.back);
        setEnabled(false);
        //imageview
        front = findViewById(R.id.front);
        backImg = findViewById(R.id.after);
        profile_img = findViewById(R.id.profile_img);

        ProgressBar pgsBar = findViewById(R.id.pBar);
        //Firebase
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                profile_name.setText(user.getUsername());
                name.setText(user.getUsername());
                phone.setText(user.getPhone());
                email.setText(user.getEmail());
                if(user.getAva()== null){
                    profile_img.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Glide.with(getApplicationContext()).load(user.getAva()).into(profile_img);
                }
                if(user.getIdImg() == null){
                    front.setVisibility(View.GONE);
                    backImg.setVisibility(View.GONE);
                    frontText.setVisibility(View.GONE);
                    backText.setVisibility(View.GONE);
                    postIDImg.setVisibility(View.VISIBLE);
                }else{
                    front.setVisibility(View.VISIBLE);
                    backImg.setVisibility(View.VISIBLE);
                    frontText.setVisibility(View.VISIBLE);
                    backText.setVisibility(View.VISIBLE);
                    postIDImg.setVisibility(View.GONE);
                    Glide.with(EditProfile.this).load(user.getIdImg().get("Image0")).into(front);
                    Glide.with(EditProfile.this).load(user.getIdImg().get("Image1")).into(backImg);
                }
                pgsBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //set click
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnabled(!name.isEnabled());
                if (name.isEnabled()) {
                    edit.setText("Lưu");
                } else {
                    reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("email", email.getText().toString());
                    hashMap.put("phone", phone.getText().toString());
                    hashMap.put("username", name.getText().toString());
                    reference.updateChildren(hashMap);
                    edit.setText("Sửa");
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfile.super.onBackPressed();
            }
        });
        ActivityResultLauncher<Intent> getIDImg = registerForActivityResult(
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

                            }
                            final ProgressDialog pd = new ProgressDialog(EditProfile.this);
                            pd.setMessage("Uploading...");
                            pd.show();
                            Log.d("AAAAAAAA", String.valueOf(imagePath.size()));
                            if(imagePath.size() !=2){
                                new AlertDialog.Builder(EditProfile.this)
                                        .setTitle("Thông báo")
                                        .setMessage("Bạn cần gửi ảnh cả mặt trước và sau của CMTND/CCCD")

                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        // A null listener allows the button to dismiss the dialog and take no further action.
                                        .setNegativeButton(android.R.string.yes, null)
                                        .show();
                            }else{
                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                for(int i =0; i<2;i++) {
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
                                                if(finalI+1==2){
                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                                                    HashMap<String, Object> hashMap = new HashMap<>();
                                                    hashMap.put("idImg",imageURI);
                                                    reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            pd.dismiss();
                                                            new AlertDialog.Builder(EditProfile.this)
                                                                    .setTitle("Thông báo")
                                                                    .setMessage("Bạn đã gửi ảnh thành công")
                                                                    .setNegativeButton(android.R.string.yes, null)
                                                                    .show();
                                                        }
                                                    });
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
                        }
                    }
                });
        ActivityResultLauncher<Intent> getAva = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            final ProgressDialog pd = new ProgressDialog(EditProfile.this);
                            pd.setMessage("Uploading...");
                            pd.show();
                            // There are no request codes
                            Intent data = result.getData();
                            Log.d("Imagesize", "onActivityResult: "+result.getData());
                            if(data.getClipData() == null) {
                                Uri imagepath = data.getData();
                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                StorageReference storageReference = storage.getReferenceFromUrl("gs://crowdshipping-387fa.appspot.com").child("images/"
                                        + UUID.randomUUID().toString());
                                storageReference.putFile(imagepath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            hashMap.put("ava",mURI);
                                            reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    pd.dismiss();
                                                    new AlertDialog.Builder(EditProfile.this)
                                                            .setTitle("Thông báo")
                                                            .setMessage("Bạn đã thay đổi ảnh đại diện thành công")
                                                            .setNegativeButton(android.R.string.yes, null)
                                                            .show();
                                                }
                                            });
                                        }
                                    }
                                });
                            }else{
                                new AlertDialog.Builder(EditProfile.this)
                                        .setTitle("Thông báo")
                                        .setMessage("Bạn chỉ được chọn 1 ảnh")
                                        .setNegativeButton(android.R.string.yes, null)
                                        .show();
                                pd.dismiss();
                            }
                        }
                    }
                });
        postIDImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage(getIDImg);
            }
        });
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu(0, fuser.getUid(),getAva);
            }
        });
    }
    private void getImage(ActivityResultLauncher<Intent> someActivityResultLauncher) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intent);
    }
    private void setEnabled(boolean clickable) {
        name.setEnabled(clickable);
        phone.setEnabled(clickable);
        email.setEnabled(clickable);
    }
    private void popupMenu(int A, String id,ActivityResultLauncher<Intent> getAva) {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View menupopup = layoutInflater.inflate(R.layout.popupmenu, null);
        int width = RelativeLayout.LayoutParams.MATCH_PARENT;
        int heigh = RelativeLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(menupopup, width,heigh,focusable);
        popupWindow.showAtLocation(EditProfile.this.findViewById(R.id.relativeayout), Gravity.BOTTOM,0,0);
        View container = (View) popupWindow.getContentView().getParent();
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams wp = (WindowManager.LayoutParams) container.getLayoutParams();
        wp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wp.dimAmount =0.4f;
        wm.updateViewLayout(container,wp);
        showpicture = menupopup.findViewById(R.id.viewpicture);
        changepicture = menupopup.findViewById(R.id.changepicture);
        if(id.equals(fuser.getUid())){
            showpicture.setVisibility(View.VISIBLE);
            changepicture.setVisibility(View.VISIBLE);
        }else{
            showpicture.setVisibility(View.VISIBLE);
            changepicture.setVisibility(View.GONE);
        }
        if(A==0){
            showpicture.setText("   View Profile Picture");
            changepicture.setText("   Select Profile Picture");
            Drawable viewprofile = getDrawable(R.drawable.ic_outline_account_box_24);
            viewprofile.setBounds(0,0,60,60);
            showpicture.setCompoundDrawables(viewprofile, null, null,null );
            changepicture.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_outline_photo_size_select_actual_24,0,0,0);
            showpicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users")
                            .child(id);
                    reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Users user = snapshot.getValue(Users.class);
                            if(user.getAva()==null){
                                Intent intent1 = new Intent(EditProfile.this, ViewPictureActivity.class);
                                intent1.putExtra("imgurl", "default");
                                startActivity(intent1);
                                popupWindow.dismiss();
                            }else{
                                Intent intent1 = new Intent(EditProfile.this, ViewPictureActivity.class);
                                intent1.putExtra("imgurl", user.getAva());
                                startActivity(intent1);
                                popupWindow.dismiss();
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            });
            changepicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getImage(getAva);
                    popupWindow.dismiss();
                }
            });
        }
    }
}