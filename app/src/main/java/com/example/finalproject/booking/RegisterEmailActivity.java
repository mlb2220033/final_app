package com.example.finalproject.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityRegisterEmailBinding;
import com.example.finalproject.model.MyUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterEmailActivity extends AppCompatActivity {
    EditText edtRePassword, edtPassword;
    private ProgressBar progressBar;
    ImageView imgShowHidePwdRegister, imgShowHideRePwdRegister;

    private ActivityRegisterEmailBinding binding;
    private static final String TAG = "REGISTER_TAG";

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        addViews();
        addEvents();
        // Initialize progressBar
        progressBar = findViewById(R.id.progressBar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void addViews() {
        imgShowHidePwdRegister = findViewById(R.id.imgShowHidePwdRegister);
        imgShowHideRePwdRegister = findViewById(R.id.imgShowHideRePwdRegister);
        imgShowHidePwdRegister.setImageResource(R.drawable.eye_hide);
        imgShowHideRePwdRegister.setImageResource(R.drawable.eye_hide);
    }
    private void addEvents() {
        imgShowHidePwdRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility(binding.edtPassword, imgShowHidePwdRegister);
            }
        });

        imgShowHideRePwdRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleRePasswordVisibility(binding.edtRePassword, imgShowHideRePwdRegister);
            }
        });

        // Register
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
        binding.txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterEmailActivity.this, LoginActivity.class));
            }
        });
    }

    private void toggleRePasswordVisibility(EditText edtRePassword, ImageView imgShowHideRePwdRegister) {
        if (edtRePassword != null && edtRePassword.getTransformationMethod() != null) {
            if (edtRePassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                edtRePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imgShowHideRePwdRegister.setImageResource(R.drawable.eye_show);
            } else {
                edtRePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imgShowHideRePwdRegister.setImageResource(R.drawable.eye_hide);
            }
        } else {
            Log.e("RegisterEmailActivity", "EditText or its transformation method is null");
        }
    }
    private void togglePasswordVisibility(EditText edtPassword, ImageView imgShowHidePwdRegister) {
        if (edtPassword != null && edtPassword.getTransformationMethod() != null) {
            if (edtPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imgShowHidePwdRegister.setImageResource(R.drawable.eye_show);
            } else {
                edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imgShowHidePwdRegister.setImageResource(R.drawable.eye_hide);
            }
        } else {
            Log.e("RegisterEmailActivity", "EditText or its transformation method is null");
        }
    }

    private String txtEmail, txtPwd, txtRePwd, txtFullName, txtUserName, txtPhone ;
    private void validateData(){
        txtEmail = binding.edtEmail.getText().toString().trim();
        txtPwd = binding.edtPassword.getText().toString();
        txtRePwd = binding.edtRePassword.getText().toString();
        txtFullName = binding.edtFullName.getText().toString();
        txtUserName = binding.edtUserName.getText().toString();
        txtPhone = binding.edtPhone.getText().toString();

        if (TextUtils.isEmpty(txtFullName)) {
            Toast.makeText(RegisterEmailActivity.this, "Please Enter Your Full Name", Toast.LENGTH_SHORT).show();
            binding.edtFullName.setError("Full name is required");
            binding.edtFullName.requestFocus();

        } else if (TextUtils.isEmpty(txtUserName)) {
            Toast.makeText(RegisterEmailActivity.this, "Please Enter Your User Name", Toast.LENGTH_SHORT).show();
            binding.edtUserName.setError("User name is required");
            binding.edtUserName.requestFocus();

        } else if (TextUtils.isEmpty(txtEmail)) {
            Toast.makeText(RegisterEmailActivity.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
            binding.edtEmail.setError("Email is required");
            binding.edtEmail.requestFocus();

        } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()) {
            Toast.makeText(RegisterEmailActivity.this, "Please Re-Enter Your Email", Toast.LENGTH_SHORT).show();
            binding.edtEmail.setError("Valid email is required");
            binding.edtEmail.requestFocus();

        } else if (TextUtils.isEmpty(txtPhone)) {
            Toast.makeText(RegisterEmailActivity.this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show();
            binding.edtPhone.setError("Phone number is required");
            binding.edtPhone.requestFocus();

        } else if (txtPhone.length()!=10) {
            Toast.makeText(RegisterEmailActivity.this, "Please Re-Enter Your Phone Number", Toast.LENGTH_SHORT).show();
            binding.edtPhone.setError("Phone number should be 10 digits");
            binding.edtPhone.requestFocus();
        } else if (TextUtils.isEmpty(txtPwd)) {
            Toast.makeText(RegisterEmailActivity.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
            binding.edtPassword.setError("Password is required");
            binding.edtPassword.requestFocus();

        } else if (txtPwd.length() < 6) {
            Toast.makeText(RegisterEmailActivity.this, "Password should be at least 6 digits", Toast.LENGTH_SHORT).show();
            binding.edtPassword.setError("Password too weak");
            binding.edtPassword.requestFocus();
        } else if (txtPwd.length() > 16) {
            Toast.makeText(RegisterEmailActivity.this, "Password should be less than 16 digits", Toast.LENGTH_SHORT).show();
            binding.edtPassword.setError("Password too long");
            binding.edtPassword.requestFocus();

        } else if (TextUtils.isEmpty(txtRePwd)) {
            Toast.makeText(RegisterEmailActivity.this, "Please Confirm Password", Toast.LENGTH_SHORT).show();
            binding.edtRePassword.setError("Password Confirmation is required");
            binding.edtRePassword.requestFocus();

        } else if (!txtPwd.equals(txtRePwd)) {
            Toast.makeText(RegisterEmailActivity.this, "Please fill same password", Toast.LENGTH_SHORT).show();
            binding.edtRePassword.setError("Password Confirmation is required");
            binding.edtRePassword.requestFocus();

            // Clear cái pwd sai
            binding.edtPassword.clearComposingText();
            binding.edtRePassword.clearComposingText();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            registerUser(txtFullName, txtUserName, txtPwd, txtEmail, txtPhone);
        }
    }
    // Register User using the credentials given
    private void registerUser(String txtFullName, String txtUserName, String txtPwd, String txtEmail, String txtPhone) {
        progressDialog.setMessage("Creating Account");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(txtEmail, txtPwd)
            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    // User Register success, save info to fb db
                    Log.d(TAG, "onSuccess: Register Success");
                    updateUserInfo();

                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "onFailure", e);
                    progressDialog.dismiss();
                    MyUtils.toast(RegisterEmailActivity.this, "Failed er due to"+e.getMessage());
                }
            });
    }

    private void updateUserInfo() {
        // change progress dialog mess
        progressDialog.setMessage("Saving User Info");
        //get current timestamp
        long timestamp = MyUtils.timestamp();
        String registeredUserEmail = firebaseAuth.getCurrentUser().getEmail();
        String registeredUserUid = firebaseAuth.getUid();
        String registeredUserPhoneNumber = firebaseAuth.getCurrentUser().getPhoneNumber();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("email", registeredUserEmail);
        hashMap.put("full name", binding.edtFullName.getText().toString());
        hashMap.put("username", binding.edtUserName.getText().toString());
        hashMap.put("phone code", "");
        hashMap.put("phone number", registeredUserPhoneNumber);
        hashMap.put("profileImageUrl", "");
        hashMap.put("timestamp", timestamp);
        hashMap.put("token", "");
        hashMap.put("userType", MyUtils.USER_TYPE_EMAIL);
        hashMap.put("uid", registeredUserUid);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(registeredUserUid)
            .setValue(hashMap)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d(TAG, "onSuccess: Info Saved...");
                    progressDialog.dismiss();
                    startActivity(new Intent(RegisterEmailActivity.this, LoginActivity.class));
                    finishAffinity();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "onFailure", e);
                    MyUtils.toast(RegisterEmailActivity.this, "Failed to save due to"+e.getMessage());
                    progressDialog.dismiss();
                }
            });
    }

    public void signInClicked(View view) {
        Intent intent = new Intent(RegisterEmailActivity.this, LoginActivity.class);
        startActivity(intent);

    }
}


