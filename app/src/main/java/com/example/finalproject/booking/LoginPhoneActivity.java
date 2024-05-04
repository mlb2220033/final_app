package com.example.finalproject.booking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalproject.R;
import com.example.finalproject.model.MyUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.example.finalproject.databinding.ActivityLoginPhoneBinding;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class LoginPhoneActivity extends AppCompatActivity {

    private  ActivityLoginPhoneBinding binding;
    private static final String TAG_P = "LOGIN_PHONE_TAG";
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.edtLPhoneNumber.setVisibility(View.VISIBLE);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();

        binding.btnSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private void validateData() {
        String phoneCode = binding.phoneCodeNumber.getSelectedCountryCodeWithPlus();
        String phoneNumber = binding.edtLPhoneNumber.getText().toString();
        String phoneNumberWithCode = phoneCode + phoneNumber;

        Log.d(TAG_P, "validateData: Phone Code: " + phoneCode);
        Log.d(TAG_P, "validateData: Phone Number: " + phoneNumber);
        Log.d(TAG_P, "validateData: Phone Number With Code: " + phoneNumberWithCode);

        if (phoneNumber.isEmpty()) {
            binding.edtLPhoneNumber.setError("Enter Phone Number");
            binding.edtLPhoneNumber.requestFocus();
        } else {
            // Pass the phone number to OtpVerificationActivity
            Intent intent = new Intent(LoginPhoneActivity.this, OtpVerificationActivity.class);
            intent.putExtra("phoneNumber", phoneNumber);
            intent.putExtra("phoneCode", phoneCode);
            intent.putExtra("phoneNumberWithCode", phoneNumberWithCode);
            startActivity(intent);
        }
    }

    public void signInClickFromLoginPhone(View view) {
        Intent intent = new Intent(LoginPhoneActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}