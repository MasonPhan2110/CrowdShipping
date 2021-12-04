package com.Duong.crowdshipping.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Duong.crowdshipping.Controller.Authentication;
import com.Duong.crowdshipping.Extend.BaseActivity;
import com.Duong.crowdshipping.R;

public class RegisterActivity extends BaseActivity {
    EditText email,phone,pass, username;
    TextView signup;
    RelativeLayout backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Window window = RegisterActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(RegisterActivity.this.getResources().getColor(R.color.green));
        window.setNavigationBarColor(RegisterActivity.this.getResources().getColor(R.color.green));

        //Edit text
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        phone = findViewById(R.id.phone);
        pass = findViewById(R.id.pass);
        //Text view
        signup = findViewById(R.id.signup);
        backBtn = findViewById(R.id.backBtn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Authentication fauth = new Authentication();
                fauth.signup(email.getText().toString(),pass.getText().toString(),RegisterActivity.this,username.getText().toString(),phone.getText().toString());
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.super.onBackPressed();
            }
        });
    }
    @Override
    protected void onShowKeyboard(int keyboardHeight) {
        super.onShowKeyboard(keyboardHeight);
        Log.d("AAA","Show");

    }

    @Override
    protected void onHideKeyboard() {
        super.onHideKeyboard();
        Log.d("AAA","Hide");

    }
}