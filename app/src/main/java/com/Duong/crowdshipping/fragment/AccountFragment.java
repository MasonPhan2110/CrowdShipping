package com.Duong.crowdshipping.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.Duong.crowdshipping.Login.LoginActivity;
import com.Duong.crowdshipping.R;
import com.Duong.crowdshipping.Activity.EditProfile;
import com.Duong.crowdshipping.Activity.SettingActivity;
import com.google.firebase.auth.FirebaseAuth;


public class AccountFragment extends Fragment {
    RelativeLayout setting,log_out;
    TextView edit_profile;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        setting = view.findViewById(R.id.settting);
        edit_profile = view.findViewById(R.id.edit_profile);
        log_out = view.findViewById(R.id.log_out);
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivity(intent);
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        return view;
    }
}