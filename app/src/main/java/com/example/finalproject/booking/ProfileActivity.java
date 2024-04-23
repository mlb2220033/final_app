package com.example.finalproject.booking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
//import com.example.finalproject.MyUtils;
import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityProfileBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends FireBaseActivity {
    private ActivityProfileBinding binding;
    private static final String TAG ="PROFILE_TAG";

    private ProgressDialog progressDialog;
    private Context mContext;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        addViews();
        addEvents();
        loadMyInfo();
    }

    private void loadMyInfo() {


    }

    private void addViews() {
        btnBack = findViewById(R.id.btnBack);
    }

    private void addEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProfileMainActivity.class);
                startActivity(intent);
            }
        });
    }
}
