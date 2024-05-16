package com.example.finalproject.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HotelMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView txtHotelName;
    ImageView imgBack;
    GoogleMap myMap;
    FirebaseDatabase firebaseDatabase;
    String hotelID, hotelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_map);
        addViews();
        getDataFromPreviousActivity();
        addEvents();

    }

    private void addEvents() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getDataFromPreviousActivity() {
        Intent intent = getIntent();

        hotelID = intent.getStringExtra("hotelID");
        hotelName = intent.getStringExtra("hotelName");
        txtHotelName.setText(hotelName);

    }

    private void addViews() {
        imgBack = findViewById(R.id.imgBack);
        txtHotelName = findViewById(R.id.txtHotelName);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        FirebaseDatabase.getInstance().getReference().child("Hotels").child(hotelID).child("hotelMarkers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            double latitude = snapshot.child("latitude").getValue(Double.class);
                            double longitude = snapshot.child("longitude").getValue(Double.class);
                            LatLng hotelLocation = new LatLng(latitude, longitude);

                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker);
                            MarkerOptions markerOptions = new MarkerOptions().position(hotelLocation).title(hotelName).icon(icon);
                            myMap.addMarker(markerOptions);

                            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hotelLocation, 15));
                        } else {
                            Toast.makeText(HotelMapActivity.this, "Hotel location data not available", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }
}