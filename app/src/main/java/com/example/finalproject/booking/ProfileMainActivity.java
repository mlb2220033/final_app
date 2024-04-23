package com.example.finalproject.booking;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;



import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityHomeBinding;
import com.example.finalproject.databinding.ActivityProfileMainBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.collection.BuildConfig;

public class ProfileMainActivity extends FireBaseActivity {
    private ActivityProfileMainBinding binding;
    RelativeLayout relaProfile, relaLogout,relaShare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProfileMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addViews();
        addEvents();
    }

    private void addViews() {
        relaProfile = findViewById(R.id.relaProfile);
        relaLogout = findViewById(R.id.relaLogout);
        relaShare = findViewById(R.id.relaShare);
    }

    private void addEvents() {
        relaProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relaProfile.setBackgroundResource(R.drawable.pro_occlick);
                Intent intent = new Intent(ProfileMainActivity.this, ProfileActivity.class);
                startActivity(intent);

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
                relaShare.setBackgroundResource(R.drawable.pro_occlick);
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

        confirmExit.findViewById(R.id.ConfirmExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });

        if(alertDialog != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        }
        alertDialog.show();
    }
}