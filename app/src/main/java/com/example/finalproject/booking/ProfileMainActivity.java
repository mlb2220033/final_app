package com.example.finalproject.booking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;



import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.bumptech.glide.Glide;
import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityHomeBinding;
import com.example.finalproject.databinding.ActivityProfileMainBinding;
import com.example.finalproject.model.MyUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.BuildConfig;

public class ProfileMainActivity extends FireBaseActivity {
    private ActivityProfileMainBinding binding;

    private static final String TAG ="PROFILE_TAG";

    private ProgressDialog progressDialog;

    private ImageView btnBack, imgVHome;

    RelativeLayout relaProfile, relaLogout,relaShare,relaChangePwd,relaMyBooking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProfileMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();

        addViews();
        addEvents();
        loadMyInfo();
    }

    private void loadMyInfo() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users");

        myRef.child(""+firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = "" + snapshot.child("full name").getValue();
                    String profileImageUrl = "" + snapshot.child("profileImageUrl").getValue();

                    String userType = "" + snapshot.child("userType").getValue();


                    //binding.txtEmail.setText(email);
                    binding.txtName.setText(name);


                    if (userType.equals(MyUtils.USER_TYPE_EMAIL)) {
                        boolean isVerified = firebaseAuth.getCurrentUser().isEmailVerified();

                        if (isVerified){
                            //binding.verifyAccount.setVisibility(View.VISIBLE);
                            binding.txtverification.setText("Verified");
                        } else {
                            //binding.verifyAccount.setVisibility(View.VISIBLE);
                            binding.txtverification.setText("Not Verified");
                        }
                    } else {
                        //binding.verifyAccount.setVisibility(View.GONE);
                        binding.txtverification.setText("Verified");
                    }

                    try {
                        Glide.with(ProfileMainActivity.this)
                                .load(profileImageUrl)
                                .placeholder(R.drawable.ic_avatar)
                                .into(binding.imgAva);

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
        relaProfile = findViewById(R.id.relaProfile);
        relaLogout = findViewById(R.id.relaLogout);
        relaShare = findViewById(R.id.relaShare);
        relaChangePwd = findViewById(R.id.relaChangePwd);
        relaMyBooking = findViewById(R.id.relaMyBooking);
        imgVHome = findViewById(R.id.imgVHome);
    }

    private void addEvents() {

        imgVHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileMainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        final boolean[] isShareClicked = {false};

        relaProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relaProfile.setBackgroundResource(R.drawable.pro_occlick);
                Intent intent = new Intent(ProfileMainActivity.this, ProfileActivity.class);
                startActivity(intent);

        }

        });

        relaMyBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relaMyBooking.setBackgroundResource(R.drawable.pro_occlick);
                Intent intent = new Intent(ProfileMainActivity.this, MyBookingActivity.class);
                startActivity(intent);
            }
        });
        relaChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relaChangePwd.setBackgroundResource(R.drawable.pro_occlick);

                if(firebaseAuth.getCurrentUser() == null){
                    MyUtils.toast(ProfileMainActivity.this,"Login Required...!");
                    // Yêu cầu đăng nhập
                    Intent intent = new Intent(ProfileMainActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    // Đã đăng nhập
                    Intent intent = new Intent(ProfileMainActivity.this, ChangePasswordActivity.class);
                    startActivity(intent);

                }

            }
        });

        relaLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                relaLogout.setBackgroundResource(R.drawable.pro_occlick);
                showDialog();
            }
        });

        relaShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    //shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }

            }
        });
    }

    private void showDialog() {
        // sua nut btn_back ground
        ConstraintLayout DialogSuccessPayment= findViewById(R.id.DialogExit);
        View view  = LayoutInflater.from(ProfileMainActivity.this).inflate(R.layout.exit_dialog, DialogSuccessPayment);
        Button confirmExit = view.findViewById(R.id.ConfirmExit);

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileMainActivity.this);
        builder.setView(view);
        final  AlertDialog alertDialog = builder.create();

        final boolean[] isConfirmed = {false};

        confirmExit.findViewById(R.id.ConfirmExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                // Đánh dấu rằng người dùng đã xác nhận
                isConfirmed[0] = true;
                alertDialog.dismiss();
                finishAffinity();

            }
        });

        if(alertDialog != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        }
        alertDialog.show();
    }
}