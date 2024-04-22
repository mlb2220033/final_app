package com.example.finalproject.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtFullName, edtUserName, edtPasswordRegister,
            edtRePasswordRegister, edtEmail, edtPhone;
    private ProgressBar progressBar;

    ImageView imgShowHidePwdRegister, imgShowHideRePwdRegister;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        addViews();
        addEvents();

        Toast.makeText(RegisterActivity.this, "You can register now", Toast.LENGTH_SHORT).show();


    }

    private void addViews() {
        edtFullName = findViewById(R.id.edtFullName);
        edtUserName = findViewById(R.id.edtUserName);
        edtPasswordRegister = findViewById(R.id.edtPasswordRegister);
        edtRePasswordRegister = findViewById(R.id.edtRePasswordRegister);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);


        imgShowHidePwdRegister = findViewById(R.id.imgShowHidePwdRegister);
        imgShowHideRePwdRegister = findViewById(R.id.imgShowHideRePwdRegister);
        imgShowHidePwdRegister.setImageResource(R.drawable.eye_hide);
        imgShowHideRePwdRegister.setImageResource(R.drawable.eye_hide);
    }

    private void addEvents() {
        imgShowHidePwdRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtPasswordRegister != null && edtRePasswordRegister != null) {
                    if (edtPasswordRegister.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                        // If password is visible, then hide
                        edtPasswordRegister.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        edtRePasswordRegister.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        imgShowHidePwdRegister.setImageResource(R.drawable.eye_hide);
                        imgShowHideRePwdRegister.setImageResource(R.drawable.eye_hide);
                    } else {
                        edtPasswordRegister.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        edtRePasswordRegister.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        imgShowHidePwdRegister.setImageResource(R.drawable.eye_show);
                        imgShowHideRePwdRegister.setImageResource(R.drawable.eye_show);
                    }
                }
            }
        });

        // Register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txtFullName = edtFullName.getText().toString();
                String txtUserName = edtUserName.getText().toString();
                String txtPwd = edtPasswordRegister.getText().toString();
                String txtRePwd = edtRePasswordRegister.getText().toString();
                String txtEmail = edtEmail.getText().toString();
                String txtPhone = edtPhone.getText().toString();

                if (TextUtils.isEmpty(txtFullName)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your Full Name", Toast.LENGTH_SHORT).show();
                    edtFullName.setError("Full name is required");
                    edtFullName.requestFocus();

                } else if (TextUtils.isEmpty(txtUserName)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your User Name", Toast.LENGTH_SHORT).show();
                    edtUserName.setError("User name is required");
                    edtUserName.requestFocus();

                } else if (TextUtils.isEmpty(txtEmail)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                    edtEmail.setError("Email is required");
                    edtEmail.requestFocus();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()) {
                    Toast.makeText(RegisterActivity.this, "Please Re-Enter Your Email", Toast.LENGTH_SHORT).show();
                    edtEmail.setError("Valid email is required");
                    edtEmail.requestFocus();

                } else if (TextUtils.isEmpty(txtPhone)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show();
                    edtPhone.setError("Phone number is required");
                    edtPhone.requestFocus();

                } else if (txtPhone.length()!=10) {
                    Toast.makeText(RegisterActivity.this, "Please Re-Enter Your Phone Number", Toast.LENGTH_SHORT).show();
                    edtPhone.setError("Phone number should be 10 digits");
                    edtPhone.requestFocus();
                } else if (TextUtils.isEmpty(txtPwd)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                    edtPasswordRegister.setError("Password is required");
                    edtPasswordRegister.requestFocus();

                } else if (txtPwd.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password should be at least 6 digits", Toast.LENGTH_SHORT).show();
                    edtPasswordRegister.setError("Password too weak");
                    edtPasswordRegister.requestFocus();
                } else if (txtPwd.length() > 16) {
                    Toast.makeText(RegisterActivity.this, "Password should be less than 16 digits", Toast.LENGTH_SHORT).show();
                    edtPasswordRegister.setError("Password too long");
                    edtPasswordRegister.requestFocus();

                } else if (TextUtils.isEmpty(txtRePwd)) {
                    Toast.makeText(RegisterActivity.this, "Please Confirm Password", Toast.LENGTH_SHORT).show();
                    edtRePasswordRegister.setError("Password Confirmation is required");
                    edtRePasswordRegister.requestFocus();

                } else if (!txtPwd.equals(txtRePwd)) {
                    Toast.makeText(RegisterActivity.this, "Please fill same password", Toast.LENGTH_SHORT).show();
                    edtRePasswordRegister.setError("Password Confirmation is required");
                    edtRePasswordRegister.requestFocus();

                    // Clear cái pwd sai
                    edtPasswordRegister.clearComposingText();
                    edtRePasswordRegister.clearComposingText();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(txtFullName, txtUserName, txtPwd, txtEmail, txtPhone);
                }
            }
        });
    }

    // Register User using the credentials given
    private void registerUser(String txtFullName, String txtUserName, String txtPwd, String txtEmail, String txtPhone) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(txtEmail, txtPwd).addOnCompleteListener(RegisterActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            // gửi email xác thực
                            firebaseUser.sendEmailVerification();

                            /*//Open User profile sau khi đăng ký thành công

                            Intent intent = new Intent(RegisterActivity.this, UserProfileActivity.class);
                            // To prevent user from returning back to Register Activity on pressing back button after registration
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();*/


                        }
                    }
                });
    }

    public void signInClicked(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);

    }
}


