package com.example.finalproject.booking;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.finalproject.R;
import com.example.finalproject.adapter.LocationAdapter;
import com.example.finalproject.model.Location;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchLocationActivity extends AppCompatActivity {
    EditText edtLocationInput;
    ImageView imgBack;
    RecyclerView rvLocation;
    List<Location> list;
    LocationAdapter locationAdapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);

        addViews();
        addEvents();

    }

    private void addEvents() {
        edtLocationInput.addTextChangedListener(new TextWatcher() {
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
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Location");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (searchText.isEmpty()) {
                    list.clear();
                } else {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Location location = dataSnapshot.getValue(Location.class);
                        if (location.getCity().toLowerCase().contains(searchText.toLowerCase()) ||
                                location.getDistrict().toLowerCase().contains(searchText.toLowerCase()) ||
                                location.getWard().toLowerCase().contains(searchText.toLowerCase())) {
                            list.add(location);
                        }
                    }
                }
                locationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addViews() {
        edtLocationInput = findViewById(R.id.edtLocationInput);
        imgBack = findViewById(R.id.imgBack);
        rvLocation = findViewById(R.id.rvLocation);

        edtLocationInput.requestFocus();

        list = new ArrayList<>();
        locationAdapter = new LocationAdapter(SearchLocationActivity.this, list);
        rvLocation.setLayoutManager(new LinearLayoutManager(SearchLocationActivity.this));
//        rvLocation.addItemDecoration(new DividerItemDecoration(rvLocation.getContext(),DividerItemDecoration.VERTICAL));
        rvLocation.setAdapter(locationAdapter);

    }
}