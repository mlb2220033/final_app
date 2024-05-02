package com.example.finalproject.booking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalproject.R;

import com.example.finalproject.databinding.ActivityOtpVerificationBinding;
import com.example.finalproject.model.MyUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class OtpVerificationActivity extends AppCompatActivity {
    private ActivityOtpVerificationBinding binding;
    private static final String TAG_P = "LOGIN_PHONE_TAG";
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private String mVerificationId;

    private boolean resendEnabled = false;
    private int resendTime = 10;

    private int selectedEdtPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();

        // Retrieve phone number from EditText
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        phoneCode = getIntent().getStringExtra("phoneCode");

        // Check if phoneNumber is not empty before starting verification
        if (!TextUtils.isEmpty(phoneNumber)) {
            phoneLoginCallBack();
            startPhoneNumberVerification();
        } else {
            // Handle the case where phoneNumber is empty
            // You can show an error message or take appropriate action
            Toast.makeText(this, "Phone number is empty", Toast.LENGTH_SHORT).show();
        }

        binding.edtOTP1.addTextChangedListener(textWatcher);
        binding.edtOTP2.addTextChangedListener(textWatcher);
        binding.edtOTP3.addTextChangedListener(textWatcher);
        binding.edtOTP4.addTextChangedListener(textWatcher);
        binding.edtOTP5.addTextChangedListener(textWatcher);
        binding.edtOTP6.addTextChangedListener(textWatcher);

        showKeyboard(binding.edtOTP1);



        binding.txtResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Disable the button to prevent multiple clicks
                binding.txtResendOTP.setEnabled(false);
                // Change text color to red when disabled
                binding.txtResendOTP.setTextColor(Color.RED);

                // Start countdown timer for 10 seconds
                new CountDownTimer(10000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        // Do nothing on tick
                    }

                    public void onFinish() {
                        // Enable the button after countdown finishes
                        binding.txtResendOTP.setEnabled(true);
                        // Change text color to blue
                        binding.txtResendOTP.setTextColor(Color.BLUE);
                        // Set click listener to resend OTP
                        binding.txtResendOTP.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Call method to resend OTP
                                resendVerificationCode(forceResendingToken);
                            }
                        });
                    }
                }.start();

                // Call method to resend OTP
                resendVerificationCode(forceResendingToken);
            }
        });




        binding.btnVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String generateOtp = binding.edtOTP1.getText().toString()+ binding.edtOTP2.getText().toString()+binding.edtOTP3.getText().toString()
                        +binding.edtOTP4.getText().toString()+binding.edtOTP5.getText().toString()+binding.edtOTP6.getText().toString();

                if(generateOtp.isEmpty()){
                    binding.edtOTP6.setError("Enter OTP");
                    binding.zoneOTP.requestFocus();
                } else if (generateOtp.length() < 6){
                    binding.edtOTP6.setError("OTP length must be 6 characters");
                    binding.zoneOTP.requestFocus();
                } else {
                    //data is valid, start verify
                    verifyPhoneNumberWithCode(generateOtp);
                }
            }
        });
    }

    private String phoneCode = "", phoneNumber = "", phoneNumberWithCode = phoneCode + phoneNumber;

    private void startPhoneNumberVerification() {
        progressDialog.setMessage("Sending OTP to: " + phoneNumber);
        progressDialog.show();

        // Setup phone auth option with Phone number, timeout, callback
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallBacks)
                        .build();
        // Start phone verification with PhoneAuthProvider
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void resendVerificationCode(PhoneAuthProvider.ForceResendingToken token){
        Log.d(TAG_P, "Resending verification code...");
        progressDialog.setMessage("Resending OTP to " + phoneNumberWithCode);
        progressDialog.show();

        //setup phone auth option withPhone number, timeout, callback
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNumberWithCode)
                        .setTimeout(5L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallBacks)
                        .setForceResendingToken(token)
                        .build();

        // Start phone verification with PhoneAuthorizations
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyPhoneNumberWithCode(String generateOtp){
        Log.d(TAG_P, "verifyPhoneNumberWithCode: "+generateOtp);

        progressDialog.setMessage("Verifying OTP... ");
        progressDialog.show();

        //PhoneAuthCredential with verify id and OTp to sign in user with signInWithPhoneAuthCredential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, generateOtp);
        signInWithPhoneAuthCredential(credential);
    }

    private void phoneLoginCallBack() {
        Log.d(TAG_P, "phoneLoginCallBack: ");

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, token);
                Log.d(TAG_P,"onCodeSent: ");

                mVerificationId = verificationId;
                forceResendingToken = token;

                progressDialog.dismiss();
                MyUtils.toast(OtpVerificationActivity.this, "OTP sent to"+phoneNumberWithCode);

            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG_P,"onVerificationCompleted: ");
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d(TAG_P,"onVerificationFailed: ");

                progressDialog.dismiss();

                MyUtils.toast(OtpVerificationActivity.this, "Failed to verify due to "+e.getMessage());
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        Log.d(TAG_P,"signInWithPhoneAuthCredential: ");

        progressDialog.setMessage("Logging In...");
        progressDialog.show();

        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.e(TAG_P, "onSuccess");
                        if(authResult.getAdditionalUserInfo().isNewUser()){
                            Log.d(TAG_P, "onSuccess: Account Created...");
                            // New User, acc created

                            updateUserInfo();

                        } else {
                            Log.d(TAG_P, "onSuccess: Logged In...");
                            // New User, acc created
                            startActivity(new Intent(OtpVerificationActivity.this, HomeActivity.class));
                            finishAffinity();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG_P, "onFailure", e);
                        progressDialog.dismiss();

                        MyUtils.toast(OtpVerificationActivity.this, "Failed to verify due to "+e.getMessage());



                    }
                });
    }

    private void updateUserInfo(){
        Log.d(TAG_P, "updateUserInfo");

        progressDialog.setMessage("Saving User Info...");
        progressDialog.show();

        long timestamp = MyUtils.timestamp();
        String registeredUserUid = firebaseAuth.getUid();
        String name = firebaseAuth.getCurrentUser().getDisplayName();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("email", "");
        hashMap.put("full name", "");
        hashMap.put("username", "");
        hashMap.put("phone code", ""+phoneCode);
        hashMap.put("phone number", ""+phoneNumber);
        hashMap.put("profileImageUrl", "");
        hashMap.put("timestamp", timestamp);
        hashMap.put("token", "");
        hashMap.put("userType", MyUtils.USER_TYPE_PHONE);
        hashMap.put("uid", registeredUserUid);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(registeredUserUid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG_P, "onSuccess: Info Saved...");
                        progressDialog.dismiss();
                        startActivity(new Intent(OtpVerificationActivity.this, HomeActivity.class));
                        finishAffinity();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG_P, "onFailure", e);
                        MyUtils.toast(OtpVerificationActivity.this, "Failed to save due to" + e.getMessage());
                        progressDialog.dismiss();
                    }
                });

    }

    private void showKeyboard(EditText edtOTP){
        edtOTP.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(edtOTP, InputMethodManager.SHOW_IMPLICIT);


    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.length() >0){
                if (selectedEdtPosition == 0){
                    selectedEdtPosition = 1;
                    showKeyboard(binding.edtOTP2);
                } else if (selectedEdtPosition == 1) {
                    selectedEdtPosition = 2;
                    showKeyboard(binding.edtOTP3);
                } else if (selectedEdtPosition == 2) {
                    selectedEdtPosition = 3;
                    showKeyboard(binding.edtOTP4);
                } else if (selectedEdtPosition == 3) {
                    selectedEdtPosition = 4;
                    showKeyboard(binding.edtOTP5);
                } else if (selectedEdtPosition == 4) {
                    selectedEdtPosition = 5;
                    showKeyboard(binding.edtOTP6);
                }
            }
        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_DEL){
            if (selectedEdtPosition == 5){
                selectedEdtPosition =4;
                showKeyboard(binding.edtOTP5);
            } else if (selectedEdtPosition == 4) {
                selectedEdtPosition =3;
                showKeyboard(binding.edtOTP4);
            } else if (selectedEdtPosition == 3) {
                selectedEdtPosition =2;
                showKeyboard(binding.edtOTP3);
            } else if (selectedEdtPosition == 2) {
                selectedEdtPosition =1;
                showKeyboard(binding.edtOTP2);
            } else if (selectedEdtPosition == 1) {
                selectedEdtPosition =0;
                showKeyboard(binding.edtOTP1);
            }
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }

    public void signInClickedFromPhoneOTP(View view) {
        Intent intent = new Intent(OtpVerificationActivity.this, LoginPhoneActivity.class);
        startActivity(intent);
    }
}