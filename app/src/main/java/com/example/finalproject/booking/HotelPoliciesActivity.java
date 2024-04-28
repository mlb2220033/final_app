package com.example.finalproject.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.finalproject.R;
import com.example.finalproject.adapter.HotelFacilitiesAdapter;
import com.example.finalproject.adapter.HotelPoliciesAdapter;
import com.example.finalproject.adapter.HotelPoliciesItemAdapter;
import com.example.finalproject.model.HotelFacilities;
import com.example.finalproject.model.HotelPolicies;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HotelPoliciesActivity extends AppCompatActivity {
    RecyclerView rvPol;
    List<HotelPolicies> hotelPoliciesList;
    HotelPoliciesItemAdapter hotelPoliciesAdapter;
    String hotelID;
    FirebaseDatabase firebaseDatabase;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_policies);
        addViews();
        addEvents();
        getDataFromPreviousActivity();
        getPolicies();
    }

    private void addEvents() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void getPolicies() {
        firebaseDatabase.getReference().child("Hotels").child(hotelID).child("hotelPocities")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            HotelPolicies hotelPol = dataSnapshot.getValue(HotelPolicies.class);
                            hotelPoliciesList.add(hotelPol);
                        }
                        hotelPoliciesAdapter.notifyDataSetChanged();

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


    private void addViews() {

        imgBack = findViewById(R.id.imgBack);

        rvPol = findViewById(R.id.rvPol);
        rvPol.setHasFixedSize(true);
        rvPol.setLayoutManager(new LinearLayoutManager(this));

        hotelPoliciesList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        hotelPoliciesAdapter = new HotelPoliciesItemAdapter(hotelPoliciesList);
        rvPol.setAdapter(hotelPoliciesAdapter);

    }

}