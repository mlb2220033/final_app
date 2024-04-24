package com.example.finalproject.booking;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityLoginBinding;
import com.example.finalproject.model.MyUtils;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText edtPassword;
    ImageView imgShowHidePwdLogin;
    private ActivityLoginBinding binding;
    private static final String TAG = "LOGIN_TAG";
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        imgShowHidePwdLogin = findViewById(R.id.imgShowHidePwdLogin); // Add this line
        addEvents();

        Toast.makeText(LoginActivity.this, "You can Login now", Toast.LENGTH_SHORT).show();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void addEvents() {
        imgShowHidePwdLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    // If pwd is visible then Hide
                    edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgShowHidePwdLogin.setImageResource(R.drawable.eye_hide);
                } else {
                    edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgShowHidePwdLogin.setImageResource(R.drawable.eye_show);
                }
            }
        });
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        binding.txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
    private String txtEmail, txtPwd;

    private void validateData(){
        txtEmail = binding.edtEmail.getText().toString().trim();
        txtPwd = binding.edtPassword.getText().toString();
        if (TextUtils.isEmpty(txtEmail)) {
            Toast.makeText(this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
            binding.edtEmail.setError("Email is required");
            binding.edtEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()) {
            Toast.makeText(this, "Please Re-Enter Your Email", Toast.LENGTH_SHORT).show();
            binding.edtEmail.setError("Valid email is required");
            binding.edtEmail.requestFocus();

        } else if (TextUtils.isEmpty(txtPwd)) {
            Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
            binding.edtPassword.setError("Password is required");
            binding.edtPassword.requestFocus();
        } else {
            loginUser();
        }
    }

    private void loginUser() {
        progressDialog.setMessage("Logging In...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(txtEmail, txtPwd)
            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Log.d(TAG, "onSuccess: Logged in...");
                    progressDialog.dismiss();

                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "onFailure", e);
                    progressDialog.dismiss();
                    MyUtils.toast(LoginActivity.this, "Failded due to"+e.getMessage());
                }
            });
    }

    public void signUpClicked(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
