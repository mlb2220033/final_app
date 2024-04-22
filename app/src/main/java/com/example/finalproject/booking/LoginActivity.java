package com.example.finalproject.booking;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;
import com.example.finalproject.booking.HomeActivity;
import com.example.finalproject.booking.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    EditText edtLPassword;
    ImageView imgShowHidePwdLogin;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addViews();
        addEvents();
    }

    private void addEvents() {
        imgShowHidePwdLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtLPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    //If pwd is  visible then Hide
                    edtLPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgShowHidePwdLogin.setImageResource(R.drawable.eye_hide);
                } else {
                    edtLPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgShowHidePwdLogin.setImageResource(R.drawable.eye_show);
                }
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//            Intent intent = new Intent(LoginActivity.this, PaymentActivity.class);
//            Intent intent = new Intent(LoginActivity.this, ResultSearchActivity.class);
//            Intent intent = new Intent(LoginActivity.this, ViewBookingActivity.class);
                    startActivity(intent);
            }
        });
    }

    private void addViews() {
        edtLPassword = findViewById(R.id.edtLPassword);

        imgShowHidePwdLogin =findViewById(R.id.imgShowHidePwdLogin);
        imgShowHidePwdLogin.setImageResource(R.drawable.eye_hide);

        btnLogin = findViewById(R.id.btnLogin);
    }

    public void signUpClicked(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    // comment
}