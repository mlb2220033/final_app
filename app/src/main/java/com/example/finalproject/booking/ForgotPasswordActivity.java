package com.example.finalproject.booking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.util.Log;
import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityForgotPasswordBinding;
import com.example.finalproject.model.MyUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;
    private static final String TAG_FP = "FORGOT_PASSWORD_TAG";

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();

        binding.btnSubmitRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

    }

    private String email = "";
    private void validateData() {
        email = binding.edtEmail.getText().toString();
        Log.d(TAG_FP, "validateData Email: "+email);

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.edtEmail.setError("Invalid Email Pattern");
            binding.edtEmail.requestFocus();
        } else {
            sendPasswordRecoveryInstruction();
        }
    }

    private void sendPasswordRecoveryInstruction(){
        Log.d(TAG_FP, "sendPasswordRecoveryInstruction: ");

        progressDialog.setMessage("Sending password recovery instruction to: "+email);
        progressDialog.show();

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG_FP, "on Success: Instruction Sent");
                        progressDialog.dismiss();
                        MyUtils.toast(ForgotPasswordActivity.this, "Instruction to reset password sent to: "+email);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG_FP, "onFailure", e);
                        progressDialog.dismiss();
                        MyUtils.toast(ForgotPasswordActivity.this, "Failed to send due to "+e.getMessage());
                    }
                });

    }

    public void signInMain(View view) {
        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}