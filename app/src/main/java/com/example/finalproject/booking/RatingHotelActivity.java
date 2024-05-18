package com.example.finalproject.booking;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRatingBar;

import com.example.finalproject.R;

import com.example.finalproject.databinding.ActivityRatingHotelBinding;
import com.example.finalproject.model.MyUtils;
import com.example.finalproject.model.Rating;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class RatingHotelActivity extends AppCompatActivity {
    private ActivityRatingHotelBinding binding;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    ImageView imgBack;
    private static final String TAG_ADD_RATING = "USER_ADD_RATING";
    private float userRate = 0;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRatingHotelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Hotels");
        mAuth = FirebaseAuth.getInstance();

        binding.btnSendReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        AppCompatButton btnSendReview = findViewById(R.id.btnSendReview);
        AppCompatButton btnCancel = findViewById(R.id.btnCancel);
        AppCompatRatingBar ratingBarHotel = findViewById(R.id.ratingBarHotel);
        TextView txtRatingStar = findViewById(R.id.txtRatingStar);
        ImageView imgBack = findViewById(R.id.imgBack);

        imgBack.setOnClickListener(v -> {
            finish();
        });

        btnSendReview.setOnClickListener(v -> {
            validateData();
        });

        btnCancel.setOnClickListener(v -> {
            clearReview();
        });

        ratingBarHotel.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (rating <= 1) {
                txtRatingStar.setText("Very Bad");
            } else if (rating <= 2) {
                txtRatingStar.setText("Not Good");
            } else if (rating <= 3) {
                txtRatingStar.setText("Quite Ok");
            } else if (rating <= 4) {
                txtRatingStar.setText("Very Good");
            } else if (rating <= 5) {
                txtRatingStar.setText("Excellent");
            }
            // Store the selected rating
            userRate = rating;
        });
    }

    private String userName = "", comment = "", ratingBarHotel = "";

    private void validateData() {
        userName = binding.edtUserName.getText().toString().trim();
        comment = binding.edtComment.getText().toString().trim();
        float rating = binding.ratingBarHotel.getRating();

        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            binding.edtUserName.setError("User Name is required");
            binding.edtUserName.requestFocus();
        } else if (TextUtils.isEmpty(comment)) {
            Toast.makeText(this, "Please enter your comment", Toast.LENGTH_SHORT).show();
            binding.edtComment.setError("Comment is required");
            binding.edtComment.requestFocus();
        } else if (userName.length() < 2 || userName.length() > 25) {
            Toast.makeText(this, "Name length should be between 2 and 25 characters", Toast.LENGTH_SHORT).show();
            binding.edtUserName.setError("Fill Available User Name");
            binding.edtUserName.requestFocus();
        } else if (comment.length() < 2 || comment.length() > 25) {
            Toast.makeText(this, "Evaluation length should be between 2 and 25 characters", Toast.LENGTH_SHORT).show();
            binding.edtComment.setError("Fill Available Comment");
            binding.edtComment.requestFocus();
        } else if (rating == 0) {
            Toast.makeText(this, "Please select a rating", Toast.LENGTH_SHORT).show();
        } else {
            addRatingToDatabase();
        }
    }


    private void addRatingToDatabase() {
        progressDialog.setMessage("Adding Rating...");
        progressDialog.show();

        mAuth = FirebaseAuth.getInstance();
        String hotelID = getIntent().getStringExtra("hotelID");

        if (hotelID == null) {
            progressDialog.dismiss();
            Toast.makeText(this, "Hotel ID is null", Toast.LENGTH_SHORT).show();
            return;
        }

        String userID = mAuth.getCurrentUser().getUid();
        String comment = binding.edtComment.getText().toString();
        float rating = binding.ratingBarHotel.getRating();
        String userName = binding.edtUserName.getText().toString();

        DatabaseReference timeRef = FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");
        timeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long offset = snapshot.getValue(Long.class);
                long estimatedServerTimeMs = System.currentTimeMillis() + offset;

                Map<String, Object> newRatingValues = new HashMap<>();
                newRatingValues.put("comment", comment);
                newRatingValues.put("starRating", rating);
                newRatingValues.put("uid", userID);
                newRatingValues.put("userName", userName);
                newRatingValues.put("timeRating", estimatedServerTimeMs);

                DatabaseReference ratingsRef = databaseReference.child(hotelID).child("Ratings").child(userID);
                ratingsRef.setValue(newRatingValues)
                        .addOnSuccessListener(aVoid -> {
                            progressDialog.dismiss();
                            Toast.makeText(RatingHotelActivity.this, "Rating added successfully", Toast.LENGTH_SHORT).show();
                            clearReview();
                        })
                        .addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Toast.makeText(RatingHotelActivity.this, "Failed to add rating: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(RatingHotelActivity.this, "Failed to get server time: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void clearReview() {
        // Clear input fields and reset rating bar
        ((EditText) findViewById(R.id.edtUserName)).setText("");
        ((EditText) findViewById(R.id.edtComment)).setText("");
        ((AppCompatRatingBar) findViewById(R.id.ratingBarHotel)).setRating(0);
        ((TextView) findViewById(R.id.txtRatingStar)).setText("");
    }

}
