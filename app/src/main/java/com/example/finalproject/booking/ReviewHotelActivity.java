package com.example.finalproject.booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import java.util.List;

public class ReviewHotelActivity extends AppCompatActivity {

    private ActivityReviewHotelBinding binding;
    private ArrayList<Rating> ratingArrayList;
    private ReviewHotelAdapter reviewHotelAdapter;
    private String hotelID;
    private FirebaseDatabase firebaseDatabase;
    private String userId;
    TextView txtStarReview, txtRatingNumber;
    List<Float> selectedStar;
    LinearLayout txt5star, txt4star, txt3star, txt2star, txt1star, llNoResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewHotelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addView();
        addEvents();
        getDataFromPreviousActivity();
        loadRatings(null);
        checkHotelStatus();
        loadAverageRating();
    }

    private void addEvents() {
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

        setStarClickListener(txt5star, 5f);
        setStarClickListener(txt4star, 4f);
        setStarClickListener(txt3star, 3f);
        setStarClickListener(txt2star, 2f);
        setStarClickListener(txt1star, 1f);
    }

    private void setStarClickListener(LinearLayout layout, final float starRating) {
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected = toggleStarSelection(layout);
                if (isSelected) {
                    if (!selectedStar.contains(starRating)) {
                        selectedStar.add(starRating);
                    }
                } else {
                    selectedStar.remove(starRating);
                }
                loadRatings(selectedStar);
            }
        });
    }

    private boolean toggleStarSelection(LinearLayout layout) {
        boolean isSelected = !layout.isSelected();
        layout.setSelected(isSelected);
        setStarBackground(layout, isSelected);
        return isSelected;
    }

    private void setStarBackground(LinearLayout layout, boolean isSelected) {
        TextView textView = (TextView) layout.getChildAt(0);
        float starValue = Float.parseFloat(textView.getText().toString());
        if (isSelected) {
            layout.setBackgroundResource(R.drawable.button_hover_click);
            textView.setTextColor(getResources().getColor(R.color.Neutral_100));
            if (!selectedStar.contains(starValue)) {
                selectedStar.add(starValue);
            }
        } else {
            layout.setBackgroundResource(R.drawable.button_design);
            textView.setTextColor(getResources().getColor(R.color.Neutral_900));
            if (selectedStar.contains(starValue)) {
                selectedStar.remove(starValue);
            }
        }
    }

    private void addView() {
        ratingArrayList = new ArrayList<>();
        reviewHotelAdapter = new ReviewHotelAdapter(this, ratingArrayList);
        binding.rvComments.setAdapter(reviewHotelAdapter);
        binding.rvComments.setLayoutManager(new LinearLayoutManager(this));

        firebaseDatabase = FirebaseDatabase.getInstance();
        txtRatingNumber = findViewById(R.id.txtRatingNumber);
        txtStarReview = findViewById(R.id.txtStarReview);

        selectedStar = new ArrayList<>();

        txt5star = findViewById(R.id.txt5star);
        txt4star = findViewById(R.id.txt4star);
        txt3star = findViewById(R.id.txt3star);
        txt2star = findViewById(R.id.txt2star);
        txt1star = findViewById(R.id.txt1star);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
        }
    }

    private void loadAverageRating() {
        DatabaseReference reference = firebaseDatabase.getReference("Hotels").child(hotelID).child("Ratings");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ratingArrayList.clear();
                float totalStars = 0;
                int totalRatings = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Rating rating = dataSnapshot.getValue(Rating.class);
                    if (rating != null) {
                        ratingArrayList.add(rating);
                        totalStars += rating.getStarRating();
                        totalRatings++;
                    }
                }

                float averageRating = totalRatings > 0 ? totalStars / totalRatings : 0;
                txtStarReview.setText(String.format("%.1f", averageRating));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getDataFromPreviousActivity() {
        Intent intent = getIntent();
        hotelID = intent.getStringExtra("hotelID");
    }

    private void loadRatings(List<Float> starFilters) {
        DatabaseReference reference = firebaseDatabase.getReference("Hotels").child(hotelID).child("Ratings");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ratingArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Rating rating = dataSnapshot.getValue(Rating.class);
                    if (rating != null) {
                        if (starFilters == null || starFilters.isEmpty() || starFilters.contains(rating.getStarRating())) {
                            Log.d("Firebase", "Rating: " + rating.getStarRating() + ", Comment: " + rating.getComment());
                            ratingArrayList.add(rating);
                        }
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
