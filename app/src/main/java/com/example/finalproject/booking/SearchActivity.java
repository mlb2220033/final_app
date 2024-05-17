package com.example.finalproject.booking;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;


import com.example.finalproject.R;
import com.example.finalproject.model.DataHolder;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

public class SearchActivity extends AppCompatActivity {

    TextView txtLocation, txtPeriod, txtGuestRoom, txtSearch, txtMessage, txtNear;
    ImageView imgBack, imgNear;
    int REQUEST_LOCATION = 1;
    int REQUEST_GUEST_ROOM = 2;
    int roomCount = 0;
    int guestsCount = 0;
    FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        addViews();
        addEvents();
        dateRangePicker();
    }

    private void dateRangePicker() {
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTheme(R.style.ThemeOverlay_App_MaterialCalendar);
        builder.setTitleText("Select your range");
        MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();
        txtPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "Tag_picker");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                txtPeriod.setText(materialDatePicker.getHeaderText());
                DataHolder.check_in = selection.first;
                DataHolder.check_out = selection.second;
                DataHolder.duration = txtPeriod.getText().toString();
            }
        });
    }


    private void addEvents() {

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtNear.setVisibility(View.GONE);
                Intent intent = new Intent(SearchActivity.this, SearchLocationActivity.class);
                startActivityForResult(intent, REQUEST_LOCATION);
            }
        });

        txtGuestRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, GuestAndRoomActivity.class);
                startActivityForResult(intent, REQUEST_GUEST_ROOM);
            }
        });

        txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = txtLocation.getText().toString().trim();
                String period = txtPeriod.getText().toString().trim();
                String guestRoom = txtGuestRoom.getText().toString().trim();


                if (!location.equals("Location") && !period.equals("Period") && !guestRoom.equals("Guest")) {
                    Intent intent = new Intent(SearchActivity.this, ResultSearchActivity.class);
                    intent.putExtra("txtLocation", location);
                    intent.putExtra("roomCount", roomCount);
                    intent.putExtra("guestsCount", guestsCount);
                    intent.putExtra("txtPeriod", period);

                    startActivity(intent);
                } else {
                    txtMessage.setVisibility(View.VISIBLE);
                    txtMessage.setText("Please fill in all fields");
                }
            }
        });


        imgNear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtLocation.setText("Hotel Nearby");
                txtNear.setVisibility(View.VISIBLE);
                txtNear.setText("Hotels will be found according to your location");
                if (ContextCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SearchActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                } else {
                    getLastLocation();
                }
            }
        });
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                Location location = task.getResult();
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();

                                DataHolder.longitude = String.valueOf(longitude);
                                DataHolder.latitude = String.valueOf(latitude);
                            }
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LOCATION && resultCode == Activity.RESULT_OK) {
            String searchLocation = data.getStringExtra("searchLocation");
            txtLocation.setText(searchLocation);

        } else if (requestCode == REQUEST_GUEST_ROOM && resultCode == Activity.RESULT_OK) {
            roomCount = data.getIntExtra("roomCount", 0);
            int adultsCount = data.getIntExtra("adultsCount", 0);
            int childrenCount = data.getIntExtra("childrenCount", 0);

            guestsCount = adultsCount + childrenCount;

            String guestRoomText = roomCount + " Rooms, " + adultsCount + " Adults, " + childrenCount + " Children";
            txtGuestRoom.setText(guestRoomText);

        }
    }


    private void addViews() {
        txtLocation = findViewById(R.id.txtLocation);
        txtPeriod = findViewById(R.id.txtPeriod);
        txtGuestRoom = findViewById(R.id.txtGuestRoom);
        txtSearch = findViewById(R.id.txtSearch);
        txtMessage = findViewById(R.id.txtMessage);
        txtNear = findViewById(R.id.txtNear);
        imgBack = findViewById(R.id.imgBack);
        imgNear = findViewById(R.id.imgNear);
    }
}



