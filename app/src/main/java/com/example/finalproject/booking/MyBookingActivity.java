package com.example.finalproject.booking;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.finalproject.R;
import com.example.finalproject.adapter.ActiveBookingAdapter;
import com.example.finalproject.adapter.HistoryBookingAdapter;
import com.example.finalproject.databinding.ActivityMyBookingBinding;
import com.example.finalproject.model.BookingHistory;
import com.example.finalproject.model.BookingHistoryComparator;
import com.example.finalproject.model.Constants;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class MyBookingActivity extends FireBaseActivity {
    private ActivityMyBookingBinding binding;
    private static final String TAG ="MY_BOOKING_TAG";

    private TextView tabActive, tabHistory;
    private ImageView btnBack;

    private RelativeLayout activeRL, historyRL;
    private RecyclerView rvActive,rvHistory;

    private ArrayList<BookingHistory> bookingList;

    private ArrayList<BookingHistory> historyList;
    private ActiveBookingAdapter activeBookingAdapter;
    private HistoryBookingAdapter historyBookingAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addViews();
        checkUser();
        showActiveBooking();
        addEvents();
    }

    

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(MyBookingActivity.this, LoginActivity.class));
            finish();
        }
        else {
            loadMyInfo();
        }
    }

    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                            loadActiveBooking();
                            loadHistoryBooking();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadActiveBooking() {
        //init active list
        bookingList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseAuth.getUid()).child("booking-history");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookingList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    BookingHistory bookingHistory = ds.getValue(BookingHistory.class);
                    if (bookingHistory != null && (bookingHistory.getStatus().equals("Paid")
                            || bookingHistory.getStatus().equals("Confirmed")
                            || bookingHistory.getStatus().equals("Cancelled"))) {
                        bookingList.add(bookingHistory);
                    }
                }
                // Sắp xếp danh sách theo thời gian giảm dần
                Collections.sort(bookingList, new BookingHistoryComparator());
                setupActiveRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });
    }

    private void setupActiveRecyclerView() {
        // Khởi tạo Adapter và thiết lập cho RecyclerView
        activeBookingAdapter = new ActiveBookingAdapter(MyBookingActivity.this, bookingList);
        rvActive.setAdapter(activeBookingAdapter);

        // Khởi tạo LayoutManager cho RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MyBookingActivity.this);
        rvActive.setLayoutManager(layoutManager);
    }



    private void loadHistoryBooking() {
        //init history list
        historyList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseAuth.getUid()).child("booking-history");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                historyList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    BookingHistory bookingHistory = ds.getValue(BookingHistory.class);
                    if (bookingHistory != null && bookingHistory.getStatus().equals("Completed")) {
                        historyList.add(bookingHistory);
                    }
                }
                // Sắp xếp danh sách theo thời gian giảm dần
                Collections.sort(historyList, new BookingHistoryComparator());
                setupHistoryRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });
    }

    private void setupHistoryRecyclerView() {
        // Khởi tạo Adapter và thiết lập cho RecyclerView
        historyBookingAdapter = new HistoryBookingAdapter(MyBookingActivity.this, historyList);
        rvHistory.setAdapter(historyBookingAdapter);

        // Khởi tạo LayoutManager cho RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MyBookingActivity.this);
        rvHistory.setLayoutManager(layoutManager);
    }


    private void addViews() {
        tabActive = findViewById(R.id.tvViewDetail);
        tabHistory = findViewById(R.id.tabHistory);
        activeRL = findViewById(R.id.activeRL);
        historyRL = findViewById(R.id.historyRL);
        rvActive = findViewById(R.id.rvActive);
        rvHistory = findViewById(R.id.rvHistory);
        btnBack = findViewById(R.id.btnBack);
    }

    private void addEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyBookingActivity.this, ProfileMainActivity.class);
                startActivity(intent);
            }
        });
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


