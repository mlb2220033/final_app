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
import com.example.finalproject.model.MyUtils;
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
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users");
        myRef.child(""+firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String dob = "" + snapshot.child("dob").getValue();
                    String email = "" + snapshot.child("email").getValue();
                    String name = "" + snapshot.child("full name").getValue();
                    String phoneCode = "" + snapshot.child("phone code").getValue();
                    String phoneNumber = "" + snapshot.child("phone number").getValue();
                    String profileImageUrl = "" + snapshot.child("profileImageUrl").getValue();
                    String timestamp = "" + snapshot.child("timestamp").getValue();
                    String userType = "" + snapshot.child("userType").getValue();
                    String location = "" + snapshot.child("location").getValue();

                    String phone = phoneCode + " " + phoneNumber;

                    if (timestamp.equals("null")) {
                        timestamp = "0";
                    }

                    String formattedDate = MyUtils.formattedTimestampData(Long.parseLong(timestamp));

                    /*//binding.txtEmail.setText(email);
                    binding.txtName.setText(name);
                    binding.txtDOB.setText(dob);
                    binding.txtPhone.setText(phone);
                    binding.txtMemberSince.setText("Member since " +formattedDate);
                    binding.txtLocation.setText(location);*/

                    if (userType.equals(MyUtils.USER_TYPE_EMAIL)) {
                        boolean isVerified = firebaseAuth.getCurrentUser().isEmailVerified();

                        if (isVerified){
                            //binding.verifyAccount.setVisibility(View.VISIBLE);
                            //binding.txtverification.setText("Verified");
                        } else {
                            //binding.verifyAccount.setVisibility(View.VISIBLE);
                            //binding.txtverification.setText("Not Verified");
                        }
                    } else {
                        //binding.verifyAccount.setVisibility(View.GONE);
                        //binding.txtverification.setText("Verified");
                    }

                    try {
                        Glide.with(ProfileActivity.this)
                                .load(profileImageUrl)
                                .placeholder(R.drawable.ic_avatar)
                                .into(binding.txtProfile);

                    } catch (Exception e){
                        Log.e(TAG,"onDataChange: ",e);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle database error
            }
        });


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
