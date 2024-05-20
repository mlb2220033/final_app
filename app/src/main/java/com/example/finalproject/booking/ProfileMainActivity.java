package com.example.finalproject.booking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.messaging.FirebaseMessaging;


import com.bumptech.glide.Glide;
import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityHomeBinding;
import com.example.finalproject.databinding.ActivityProfileMainBinding;
import com.example.finalproject.model.Constants;
import com.example.finalproject.model.MyUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private TextView tvNotificationStatus;
    private SwitchCompat fcmSwitch;
    private static final String enabledMessage = "Notifications are enabled";
    private static final String disabledMessage = "Notifications are disabled";


    private FirebaseAuth firebaseAuth;
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;
    private boolean isChecked = false;
    RelativeLayout relaProfile, relaLogout,relaShare,relaChangePwd,relaMyBooking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProfileMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tvNotificationStatus = findViewById(R.id.tvNotificationStatus);
        fcmSwitch = findViewById(R.id.fcmSwitch);
        
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();

        //init shared preferences
        sp = getSharedPreferences( "SETTINGS_SP", MODE_PRIVATE) ;

        //check last selected option; true/false
        isChecked = sp.getBoolean( "FCM_ENABLED",false);
        fcmSwitch.setChecked(isChecked);
        if (isChecked){
            //was enable
            tvNotificationStatus.setText(enabledMessage);
        } else {
            //was disable
            tvNotificationStatus.setText(disabledMessage);
        }

        //add switch check change listener to enable disable notifications
        fcmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    //checked, enable notifications
                    subscribeToTopic();
                } else {
                    //unchecked, disable notifications
                    unSubscribeToTopic();
                }
            }
        });

        addViews();
        addEvents();
        loadMyInfo();
    }

    private void subscribeToTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.FCM_TOPIC)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //subscribed successfully
                        //save setting ins shared preferences
                        spEditor = sp.edit();
                        spEditor.putBoolean("FCM_ENABLED",true);
                        spEditor.apply();

                        Toast.makeText( ProfileMainActivity.this, ""+enabledMessage, Toast.LENGTH_SHORT).show();
                        tvNotificationStatus.setText(enabledMessage);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed subscribing
                        Toast.makeText(ProfileMainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void unSubscribeToTopic() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.FCM_TOPIC)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //unsubscribed
                        spEditor = sp.edit();
                        spEditor.putBoolean("FCM_ENABLED",false);
                        spEditor.apply();

                        Toast.makeText( ProfileMainActivity.this, ""+disabledMessage, Toast.LENGTH_SHORT).show();
                        tvNotificationStatus.setText(disabledMessage);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed unsubscribing
                        Toast.makeText(ProfileMainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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