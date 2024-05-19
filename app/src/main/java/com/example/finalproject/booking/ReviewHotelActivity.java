package com.example.finalproject.booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.finalproject.R;
import com.example.finalproject.adapter.ReviewHotelAdapter;
import com.example.finalproject.databinding.ActivityReviewHotelBinding;

import com.example.finalproject.model.Rating;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ReviewHotelActivity extends AppCompatActivity {

    private ActivityReviewHotelBinding binding;
    private ArrayList<Rating> ratingArrayList;
    private ReviewHotelAdapter reviewHotelAdapter;
    private String hotelID;
    private FirebaseDatabase firebaseDatabase;
    private String userId;
    TextView txtStarReview, txtRatingNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewHotelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseDatabase = FirebaseDatabase.getInstance();
        txtRatingNumber = findViewById(R.id.txtRatingNumber);
        txtStarReview = findViewById(R.id.txtStarReview);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
        }

        getDataFromPreviousActivity();
        loadRatings();
        checkHotelStatus();

        binding.rvComments.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter
        ratingArrayList = new ArrayList<>();
        reviewHotelAdapter = new ReviewHotelAdapter(this, ratingArrayList);
        binding.rvComments.setAdapter(reviewHotelAdapter);

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewHotelActivity.this, RatingHotelActivity.class);
                intent.putExtra("hotelID", hotelID);
                startActivity(intent);
            }
        });

    }

    private void getDataFromPreviousActivity() {
        Intent intent = getIntent();
        hotelID = intent.getStringExtra("hotelID");
        txtStarReview.setText(intent.getStringExtra("txtStarReview"));


    }

    private void loadRatings() {
        DatabaseReference reference = firebaseDatabase.getReference("Hotels").child(hotelID).child("Ratings");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Rating rating = dataSnapshot.getValue(Rating.class);
                    if (rating != null) {
                        Log.d("Firebase", "Rating: " + rating.getStarRating() + ", Comment: " + rating.getComment());
                        ratingArrayList.add(rating);
                    } else {
                        Log.e("Firebase", "Null rating object encountered");
                    }
                }

                int size = ratingArrayList.size();
                txtRatingNumber.setText("Out of " + size + " ratings");

                reviewHotelAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error loading ratings: " + error.getMessage());
            }
        });
    }

    private void checkHotelStatus() {
        DatabaseReference userReference = firebaseDatabase.getReference("Users").child(userId).child("booking-history");
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot bookingSnapshot : dataSnapshot.getChildren()) {
                    String status = bookingSnapshot.child("status").getValue(String.class);
                    String hotelId = bookingSnapshot.child("hotel_id").getValue(String.class);
                    if ("Completed".equals(status) && hotelId != null && hotelId.equals(hotelID)) {
                        binding.btnRating.setVisibility(View.VISIBLE);
                        // Kiểm tra từng đặt phòng một và chỉ hiển thị khi có ít nhất một đặt phòng hoàn thành
                        break;
                    } else {
                        binding.btnRating.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}