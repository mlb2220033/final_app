package com.example.finalproject.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
    ImageView imgFilter;
    RecyclerView rvHotel;
    HotelAdapter hotelAdapter;
    ArrayList<Hotel> recycleList;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);

        addViews();
        addEvents();
        getData();
    }

    private void getData() {
        firebaseDatabase.getReference().child("Hotels").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Hotel hotel = dataSnapshot.getValue(Hotel.class);
                    recycleList.add(hotel);
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
                dialog.show(getSupportFragmentManager(),null);
            }
        });
    }
    private void addViews() {
        imgFilter = findViewById(R.id.imgFilter);
        rvHotel = findViewById(R.id.rvHotel);
        recycleList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        hotelAdapter = new HotelAdapter(recycleList,getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvHotel.setLayoutManager(linearLayoutManager);
        rvHotel.addItemDecoration(new DividerItemDecoration(rvHotel.getContext(),DividerItemDecoration.VERTICAL));
        rvHotel.setNestedScrollingEnabled(false);
        rvHotel.setAdapter(hotelAdapter);

    }
}