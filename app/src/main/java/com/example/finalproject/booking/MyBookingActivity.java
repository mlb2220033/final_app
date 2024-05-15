package com.example.finalproject.booking;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityMyBookingBinding;
import com.example.finalproject.databinding.ActivityProfileBinding;

public class MyBookingActivity extends FireBaseActivity {
    private ActivityMyBookingBinding binding;
    private static final String TAG ="MY_BOOKING_TAG";

    private TextView tabActive, tabHistory;

    private RelativeLayout activeRL, historyRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addViews();
        showActiveBooking();
        addEvents();
    }

    private void addViews() {
        tabActive = findViewById(R.id.tabActive);
        tabHistory = findViewById(R.id.tabHistory);
        activeRL = findViewById(R.id.activeRL);
        historyRL = findViewById(R.id.historyRL);
    }

    private void addEvents() {
        tabActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Load Active Booking
                showActiveBooking();

            }
        });

        tabHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // load History Booking
                showHistoryBooking();

            }
        });
    }

    private void showHistoryBooking() {
        historyRL.setVisibility(View.VISIBLE);
        activeRL.setVisibility(View.GONE);

        tabHistory.setTextColor(getResources().getColor(R.color.black));
        tabHistory.setBackgroundResource(R.drawable.shape_mybooking1);

        tabActive.setTextColor(getResources().getColor(R.color.Neutral_100));
        tabActive.setBackgroundColor(Color.parseColor("#25FFFFFF"));

    }

    private void showActiveBooking() {
        activeRL.setVisibility(View.VISIBLE);
        historyRL.setVisibility(View.GONE);

        tabActive.setTextColor(getResources().getColor(R.color.black));
        tabActive.setBackgroundResource(R.drawable.shape_mybooking1);

        tabHistory.setTextColor(getResources().getColor(R.color.Neutral_100));
        tabHistory.setBackgroundColor(Color.parseColor("#25FFFFFF"));
    }


}