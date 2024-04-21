package com.example.finalproject.booking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.finalproject.BottomSheetDialog.BottomSheetFilter;
import com.example.finalproject.R;
import com.example.finalproject.adapter.HotelAdapter;
import com.example.finalproject.model.Hotel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ResultSearchActivity extends AppCompatActivity {
    ImageView imgFilter;
    RecyclerView rvHotel;
    HotelAdapter hotelAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);
        addViews();
        addEvents();
    }
    private void addEvents() {
        imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetFilter dialog = new BottomSheetFilter();
                dialog.show(getSupportFragmentManager(),null);
            }
        });
    }
    private void addViews() {
        imgFilter = findViewById(R.id.imgFilter);
        rvHotel = findViewById(R.id.rvHotel);
        rvHotel.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Hotel> options =
                new FirebaseRecyclerOptions.Builder<Hotel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Hotels"), Hotel.class)
                        .build();
        hotelAdapter = new HotelAdapter(options);
        rvHotel.setAdapter(hotelAdapter);
    }
    @Override
    protected void onStart() {
        hotelAdapter.startListening();
        super.onStart();
    }
    @Override
    protected void onStop() {
        hotelAdapter.startListening();
        super.onStop();
    }
}