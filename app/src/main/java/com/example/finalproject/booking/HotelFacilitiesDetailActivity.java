package com.example.finalproject.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.finalproject.R;
import com.example.finalproject.adapter.HotelFacilitiesAdapter;
import com.example.finalproject.adapter.HotelFacilitiesItemAdapter;
import com.example.finalproject.adapter.NestedAdapter;
import com.example.finalproject.model.HotelFacilities;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HotelFacilitiesDetailActivity extends AppCompatActivity {
    RecyclerView rvFacDetail;
    List<HotelFacilities> hotelFacilitiesList;
    HotelFacilitiesItemAdapter hotelFacItemAdapter;
    FirebaseDatabase firebaseDatabase;
    String hotelID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_facilities_detail);
        addViews();
        getDataFromPreviousActivity();
        getFacItem();
    }

    private void getDataFromPreviousActivity() {
        Intent intent = getIntent();
        hotelID = intent.getStringExtra("hotelID");
    }

    private void getFacItem() {
        firebaseDatabase.getReference().child("Hotels").child(hotelID).child("hotelFacilities")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            HotelFacilities hotelFac = dataSnapshot.getValue(HotelFacilities.class);
                            Log.d("Facilities", hotelFac.toString());
                            hotelFacilitiesList.add(hotelFac);
                        }
                        hotelFacItemAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void addViews() {
        rvFacDetail = findViewById(R.id.rvFacDetail);
        rvFacDetail.setHasFixedSize(true);
        rvFacDetail.setLayoutManager(new LinearLayoutManager(this));

        hotelFacilitiesList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        hotelFacItemAdapter = new HotelFacilitiesItemAdapter(hotelFacilitiesList);
        rvFacDetail.setAdapter(hotelFacItemAdapter);
    }
}