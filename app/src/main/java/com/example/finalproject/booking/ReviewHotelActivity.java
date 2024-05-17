package com.example.finalproject.booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.finalproject.adapter.ReviewHotelAdapter;
import com.example.finalproject.databinding.ActivityReviewHotelBinding;
import com.example.finalproject.model.Rating;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReviewHotelActivity extends AppCompatActivity {
    private ActivityReviewHotelBinding binding;
    private ArrayList<Rating> ratingArrayList;
    private ReviewHotelAdapter reviewHotelAdapter;
    private String hotelID;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewHotelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseDatabase = FirebaseDatabase.getInstance();

        getDataFromPreviousActivity();
        loadRatings();

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
    }

    private void loadRatings() {
        DatabaseReference reference = firebaseDatabase.getReference("Hotels").child(hotelID).child("Ratings");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ratingArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Rating rating = dataSnapshot.getValue(Rating.class);
                    if (rating != null) {
                        ratingArrayList.add(rating);
                    }
                }
                reviewHotelAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error loading ratings: " + error.getMessage());
            }
        });
    }
}
