package com.example.finalproject.booking;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityViewDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ViewDetailsActivity extends FireBaseActivity {
    private ActivityViewDetailsBinding binding;
    private ImageView btnBack;
    private TextView tvName, tvLocation, tvCheckIn, tvCheckOut, tvDuration, tvCost, tvDetails, tvStatus;
    private FirebaseAuth firebaseAuth;
    private String hotelID;
    private Long timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        addEvents();


        Intent intent = getIntent();
        if (intent != null) {
            hotelID = intent.getStringExtra("hotel_id");
            timestamp = intent.getLongExtra("time_stamp", 0);
            Log.d("ViewDetailsActivity", "hotelID: " + hotelID);
            Log.d("ViewDetailsActivity", "Timestamp: " + timestamp);
            loadBookingDetails();
            loadHotelInfo();
        } else {
            Toast.makeText(this, "Intent is null", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadHotelInfo() {
        DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("Hotels").child(hotelID);
        bookingRef
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String Name = ""+dataSnapshot.child("hotelName").getValue();
                        String Location = ""+dataSnapshot.child("hotelAddress").getValue();
                        tvName.setText(Name);
                        tvLocation.setText(Location);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
                );
    }

    private void initView() {
        btnBack = binding.btnBack;
        tvName = binding.tvName;
        tvLocation = binding.tvLocation;
        tvCheckIn = binding.tvCheckIn;
        tvCheckOut = binding.tvCheckOut;
        tvDuration = binding.tvDuration;
        tvCost = binding.tvCost;
        tvDetails = binding.tvDetails;
        tvStatus = binding.tvStatus;
    }

    private void addEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadBookingDetails() {
        DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("booking-history");


        Log.d("TES","ABC"+ bookingRef);

        bookingRef.orderByChild("hotel_id").equalTo(hotelID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot bookingSnapshot : dataSnapshot.getChildren()) {
                    Long bookingTimestamp = bookingSnapshot.child("time_stamp").getValue(Long.class);
                    if (bookingTimestamp != null && bookingTimestamp.equals(timestamp)) {
                        // Lấy thông tin đặt phòng từ dataSnapshot
                        Float cost = bookingSnapshot.child("cost").getValue(Float.class);
                        String status = bookingSnapshot.child("status").getValue(String.class);
                        Long checkIn = bookingSnapshot.child("check_in").getValue(Long.class);
                        Long checkOut = bookingSnapshot.child("check_out").getValue(Long.class);
                        Integer roomCount = bookingSnapshot.child("room_count").getValue(Integer.class);
                        String roomType = bookingSnapshot.child("type_room").getValue(String.class);
                        Integer dayCounts = bookingSnapshot.child("day_count").getValue(Integer.class);

                        // Hiển thị thông tin đặt phòng
                        if (status != null) {
                            switch (status) {
                                case "Paid":
                                    tvStatus.setTextColor(ContextCompat.getColor(ViewDetailsActivity.this, R.color.Semantic_500));
                                    break;
                                case "Confirmed":
                                    tvStatus.setTextColor(Color.parseColor("#4CAF50"));
                                    break;
                                case "Cancelled":
                                    tvStatus.setTextColor(ContextCompat.getColor(ViewDetailsActivity.this, R.color.Semantic_700));
                                    break;
                            }
                            tvStatus.setText(status);
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                        String checkInString = sdf.format(new Date(checkIn));
                        String checkOutString = sdf.format(new Date(checkOut));

                        tvCheckIn.setText(checkInString);
                        tvCheckOut.setText(checkOutString);
                        tvDetails.setText(roomCount + ", " + roomType);
                        tvCost.setText(String.format(Locale.getDefault(), "VND %,.0f", cost));
                        tvDuration.setText(String.valueOf(dayCounts));

                        return; // Kết thúc khi đã tìm thấy đặt phòng phù hợp
                    }
                }
                // Không tìm thấy đặt phòng phù hợp
                Toast.makeText(ViewDetailsActivity.this, "Booking not found", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi truy xuất dữ liệu thất bại
                Toast.makeText(ViewDetailsActivity.this, "Failed to load booking details: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




}
