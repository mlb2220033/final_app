package com.example.finalproject.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.finalproject.R;
import com.example.finalproject.adapter.HotelSearchAdapter;
import com.example.finalproject.adapter.LocationAdapter;
import com.example.finalproject.model.Hotel;
import com.example.finalproject.model.Location;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchHotelActivity extends AppCompatActivity {
    EditText edtSearchHotel;
    ImageView imgBack;
    RecyclerView rvSearchHotel;
    List<Hotel> list;
    HotelSearchAdapter hotelSearchAdapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_hotel);
        addViews();
        addEvents();
    }

    private void addEvents() {
        edtSearchHotel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterData(editable.toString());
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void filterData(String searchText) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Hotels");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (searchText.isEmpty()) {
                    list.clear();
                } else {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Hotel hotel = dataSnapshot.getValue(Hotel.class);
                        if (hotel.getHotelName().toLowerCase().contains(searchText.toLowerCase())) {
                            list.add(hotel);
                        }
                    }
                }
                hotelSearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addViews() {
        edtSearchHotel = findViewById(R.id.edtSearchHotel);
        imgBack = findViewById(R.id.imgBack);
        rvSearchHotel = findViewById(R.id.rvSearchHotel);

        edtSearchHotel.requestFocus();

        list = new ArrayList<>();
        hotelSearchAdapter = new HotelSearchAdapter(SearchHotelActivity.this, list);
        rvSearchHotel.setLayoutManager(new LinearLayoutManager(SearchHotelActivity.this));
        rvSearchHotel.setAdapter(hotelSearchAdapter);

    }
}