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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.example.finalproject.BottomSheetDialog.BottomSheetFilter;
import com.example.finalproject.R;
import com.example.finalproject.adapter.HotelAdapter;
import com.example.finalproject.model.Hotel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ResultSearchActivity extends AppCompatActivity {
    ImageView imgFilter, imgBack;
    TextView txtLocation, txtPeriod, txtGuest, txtRoom;
    RecyclerView rvHotel;
    HotelAdapter hotelAdapter;
    ArrayList<Hotel> recycleList;
    FirebaseDatabase firebaseDatabase;
    String Location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);

        addViews();
        addEvents();
        getDataFromPrevious();
        getData();
    }

    private void getDataFromPrevious() {
        Intent intent = getIntent();

        Location = getIntent().getStringExtra("txtLocation");

        txtLocation.setText(getIntent().getStringExtra("txtLocation"));
        txtPeriod.setText(getIntent().getStringExtra("txtPeriod"));
        txtRoom.setText(String.valueOf(intent.getIntExtra("roomCount", 0))); // Chuyển giá trị int sang String để hiển thị trên TextView
        txtGuest.setText(String.valueOf(intent.getIntExtra("guestsCount", 0))); // Chuyển giá trị int sang String để hiển thị trên TextView

    }

    private void getData() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference().child("Hotels").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Hotel hotel = dataSnapshot.getValue(Hotel.class);
                    if (hotel.getHotelAddress().toLowerCase().contains(Location.toLowerCase())) {
                        recycleList.add(hotel);
                    }
                }
                hotelAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addEvents() {
        imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetFilter dialog = new BottomSheetFilter();
                dialog.show(getSupportFragmentManager(), null);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void addViews() {
        imgFilter = findViewById(R.id.imgFilter);
        imgBack = findViewById(R.id.imgBack);
        txtLocation = findViewById(R.id.txtLocation);
        txtPeriod = findViewById(R.id.txtPeriod);
        txtGuest = findViewById(R.id.txtGuest);
        txtRoom = findViewById(R.id.txtRoom);


        rvHotel = findViewById(R.id.rvHotel);
        recycleList = new ArrayList<>();
        hotelAdapter = new HotelAdapter(recycleList, getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvHotel.setLayoutManager(linearLayoutManager);
        rvHotel.addItemDecoration(new DividerItemDecoration(rvHotel.getContext(), DividerItemDecoration.VERTICAL));
        rvHotel.setNestedScrollingEnabled(false);
        rvHotel.setAdapter(hotelAdapter);

    }
}