package com.Duong.crowdshipping.Controller;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.Duong.crowdshipping.Activity.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Authentication {
    private String email,username,phone;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public Authentication(){

    }
    public void signin(String email, String password, Activity activity,ProgressBar pBar){
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                pBar.setVisibility(View.GONE);
                check(task.isSuccessful(),"signin", activity);
            }
        });
    }
    public void signup(String email, String password, Activity activity, String username, String phone, ProgressBar pBar){
        this.email = email;
        this.username = username;
        this.phone = phone;
        Log.d("SignupActivity", this.username);
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                pBar.setVisibility(View.GONE);
                check(task.isSuccessful(),"signup", activity);
                Log.d("SignupActivity", task.getResult().toString());
            }
        });
    }
    private void check(boolean success, String type, Activity activity){
        if(type == "signin"){
            if(success){
                Intent intent = new Intent(activity.getApplicationContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                activity.finish();
            }else{
                Toast.makeText(activity, "Something wrong with Email or Password", Toast.LENGTH_SHORT).show();
            }
        }else{
            if(success){
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String userid = firebaseUser.getUid();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id",userid);
                hashMap.put("username",username);
                hashMap.put("email",email);
                hashMap.put("phone",phone);

                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(activity, "Something wrong", Toast.LENGTH_SHORT).show();
                        }else{
                            Intent intent = new Intent(activity.getApplicationContext(), HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                            activity.finish();
                        }
                    }
                });
            }else {
                Toast.makeText(activity, "Can't not Register with this email", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String safeEmail(String email){
        return email.replace('@', '-').replace('.','-');
    }
}
