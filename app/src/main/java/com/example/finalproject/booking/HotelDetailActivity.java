package com.example.finalproject.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.finalproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HotelDetailActivity extends AppCompatActivity {

    ImageSlider imgSlider;

    TextView txtHotelName, txtHotelAddress, txtPricePerNight,txtStarRating, txtViewRoom;
    String hotelID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);
        addViews();
//        addEvents();
        getDataFromPreviousActivity();
        setupImageSlider();
    }


    private void getDataFromPreviousActivity() {
        Intent intent = getIntent();
        hotelID = intent.getStringExtra("hotelID");
        txtHotelName.setText(getIntent().getStringExtra("txtHotelName"));
        txtHotelAddress.setText(getIntent().getStringExtra("txtHotelAddress"));
        txtPricePerNight.setText(String.valueOf(getIntent().getFloatExtra("txtPricePerNight", 0.00f)));
        txtStarRating.setText(String.valueOf(getIntent().getFloatExtra("txtStarRating", 0.0f)));
    }

    private void addViews() {
        txtHotelName = findViewById(R.id.txtHotelName);
        txtHotelAddress = findViewById(R.id.txtHotelAddress);
        txtPricePerNight = findViewById(R.id.txtPricePerNight);
        txtStarRating = findViewById(R.id.txtStarRating);

        imgSlider = findViewById(R.id.imgSlider);
        txtViewRoom = findViewById(R.id.txtViewRoom);

    }

    private void setupImageSlider() {
        final List<SlideModel> list = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Hotels").child(hotelID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            List<SlideModel> imageList = new ArrayList<>();
                            DataSnapshot hotelImagesData = snapshot.child("hotelImages");
                            for (DataSnapshot imageData : hotelImagesData.getChildren()) {
                                String imageUrl = imageData.getValue(String.class);
                                imageList.add(new SlideModel(imageUrl, ScaleTypes.FIT));
                            }
                            imgSlider.setImageList(imageList, ScaleTypes.FIT);
                            imgSlider.stopSliding();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    public void openViewRoom(View view) {
        Intent intent = new Intent(getApplicationContext(), ViewRoomActivity.class);
        intent.putExtra("hotelID", hotelID);
        startActivity(intent);
    }
}