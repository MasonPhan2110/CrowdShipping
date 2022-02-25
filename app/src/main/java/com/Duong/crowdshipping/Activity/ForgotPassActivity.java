package com.Duong.crowdshipping.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Duong.crowdshipping.Login.LoginActivity;
import com.Duong.crowdshipping.Login.RegisterActivity;
import com.Duong.crowdshipping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassActivity extends AppCompatActivity {
    RelativeLayout backBtn;
    EditText email;
    TextView next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        Window window = ForgotPassActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ForgotPassActivity.this.getResources().getColor(R.color.green));
        window.setNavigationBarColor(ForgotPassActivity.this.getResources().getColor(R.color.green));

        next = findViewById(R.id.next);
        backBtn = findViewById(R.id.backBtn);
        email = findViewById(R.id.email);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgotPassActivity.super.onBackPressed();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty()) {
                    Toast.makeText(ForgotPassActivity.this, "Email can't be blank", Toast.LENGTH_SHORT).show();
                }else{
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    String emailAddress = email.getText().toString();
                    auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                new AlertDialog.Builder(ForgotPassActivity.this)
                                        .setTitle("Thông báo")
                                        .setMessage("Đã gửi Email khôi phục lại mật khẩu")

                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        // A null listener allows the button to dismiss the dialog and take no further action.
                                        .setNegativeButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                ForgotPassActivity.super.onBackPressed();
                                            }
                                        })
                                        .show();
                            }
                        }
                    });
                }
            }
        });
    }
}