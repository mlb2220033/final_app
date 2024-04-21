package com.example.finalproject.booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;
import com.example.finalproject.booking.HomeActivity;
import com.example.finalproject.booking.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void signUpClicked(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
    public void openHomeScreen(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
//        Intent intent = new Intent(this, PaymentActivity.class);
//        Intent intent = new Intent(this, ResultSearchActivity.class);
//        Intent intent = new Intent(this, ViewBookingActivity.class);
        startActivity(intent);
    }
    // comment
}