package com.example.finalproject.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.finalproject.adapter.HotelAdapter;
import com.example.finalproject.adapter.HotelFacilitiesAdapter;
import com.example.finalproject.adapter.HotelFacilitiesItemAdapter;
import com.example.finalproject.adapter.HotelPoliciesAdapter;
import com.example.finalproject.model.Hotel;
import com.example.finalproject.model.HotelFacilities;
import com.example.finalproject.model.HotelPolicies;
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

    TextView txtHotelName, txtHotelAddress, txtPricePerNight, txtStarRating, txtViewRoom, txtContactHotel, txtHotelPhone, txtHotelGmail;
    String hotelID, hotelName;
    RecyclerView rvFac;
    HotelFacilitiesAdapter hotelFacilitiesAdapter;
    ArrayList<HotelFacilities> hotelFacList;
    FirebaseDatabase firebaseDatabase;

    RecyclerView rvPol;
    List<HotelPolicies> hotelPoliciesList;
    HotelPoliciesAdapter hotelPoliciesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);
        addViews();
        getDataFromPreviousActivity();
        setupImageSlider();
        getFactilities();
        getPolicies();
    }

    private void getPolicies() {
        firebaseDatabase.getReference().child("Hotels").child(hotelID).child("hotelPocities")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int count = 0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (count >= 2)
                                break;
                            HotelPolicies hotelPol = dataSnapshot.getValue(HotelPolicies.class);
                            hotelPoliciesList.add(hotelPol);
                            count++;
                        }
                        hotelPoliciesAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getFactilities() {
        firebaseDatabase.getReference().child("Hotels").child(hotelID).child("hotelFacilities")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            HotelFacilities hotelFac = dataSnapshot.getValue(HotelFacilities.class);
                            hotelFacList.add(hotelFac);
                        }
                        hotelFacilitiesAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    public void getDataFromPreviousActivity() {
        Intent intent = getIntent();
        hotelID = intent.getStringExtra("hotelID");
        txtHotelName.setText(getIntent().getStringExtra("txtHotelName"));
        txtHotelAddress.setText(getIntent().getStringExtra("txtHotelAddress"));
        txtHotelPhone.setText(getIntent().getStringExtra("txtHotelPhone"));
        txtHotelGmail.setText(getIntent().getStringExtra("txtHotelGmail"));

        txtPricePerNight.setText(String.valueOf(getIntent().getFloatExtra("txtPricePerNight", 0.00f)));
        txtStarRating.setText(String.valueOf(getIntent().getFloatExtra("txtStarRating", 0.0f)));

    }

    private void addViews() {

        txtHotelName = findViewById(R.id.txtHotelName);
        txtContactHotel = findViewById(R.id.txtContactHotel);
        txtHotelAddress = findViewById(R.id.txtHotelAddress);
        txtHotelPhone = findViewById(R.id.txtHotelPhone);
        txtHotelGmail = findViewById(R.id.txtHotelGmail);
        txtPricePerNight = findViewById(R.id.txtPricePerNight);
        txtStarRating = findViewById(R.id.txtStarRating);

        hotelName = getIntent().getStringExtra("txtHotelName");
        txtContactHotel.setText(txtContactHotel.getText().toString() + hotelName + " ?");

//      Slider Image
        imgSlider = findViewById(R.id.imgSlider);
        txtViewRoom = findViewById(R.id.txtViewRoom);

//      Factilities List
        rvFac = findViewById(R.id.rvFac);
        hotelFacList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        hotelFacilitiesAdapter = new HotelFacilitiesAdapter(hotelFacList, getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvFac.setLayoutManager(linearLayoutManager);
        rvFac.setNestedScrollingEnabled(false);
        rvFac.setAdapter(hotelFacilitiesAdapter);

//      Policies List
        rvPol = findViewById(R.id.rvPol);
        rvPol.setHasFixedSize(true);
        rvPol.setLayoutManager(new LinearLayoutManager(this));

        hotelPoliciesList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        hotelPoliciesAdapter = new HotelPoliciesAdapter(hotelPoliciesList);
        rvPol.setAdapter(hotelPoliciesAdapter);

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

    public void openFacilitiesDetail(View view) {
        Intent intent = new Intent(getApplicationContext(), HotelFacilitiesDetailActivity.class);
        intent.putExtra("hotelID", hotelID);
        startActivity(intent);
    }

    public void openPolicies(View view) {
        Intent intent = new Intent(getApplicationContext(), HotelPoliciesActivity.class);
        intent.putExtra("hotelID", hotelID);
        startActivity(intent);
    }
}