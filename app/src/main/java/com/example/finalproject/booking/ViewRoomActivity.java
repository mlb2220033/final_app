package com.example.finalproject.booking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.adapter.HotelAdapter;
import com.example.finalproject.adapter.RoomAdapter;
import com.example.finalproject.model.DataHolder;
import com.example.finalproject.model.Hotel;
import com.example.finalproject.model.Room;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewRoomActivity extends AppCompatActivity {
    RecyclerView rvRoom;
    RoomAdapter roomAdapter;
    ArrayList<Room> roomArrayList;
    String hotelID, hotelName;
    FirebaseDatabase firebaseDatabase;
    ImageView imgBack;
    TextView txtHotelName, txtPeriod, txtRoom, txtGuest;
    LinearLayout llRoomGuest;
    int REQUEST_GUEST_ROOM = 2;
    int roomCount = 0;
    int guestsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_room);
        addViews();
        addEvents();
        getDataFromPreviousActivity();
        getData();
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
                long currentTime = System.currentTimeMillis();
                if (selection.first < currentTime || selection.second < currentTime) {
                    Toast.makeText(getApplicationContext(), "Please select dates in the future", Toast.LENGTH_SHORT).show();
                    return;
                }
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
        llRoomGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewRoomActivity.this, GuestAndRoomActivity.class);
                startActivityForResult(intent, REQUEST_GUEST_ROOM);
            }
        });
    }

    private void getDataFromPreviousActivity() {
        Intent intent = getIntent();
        hotelID = intent.getStringExtra("hotelID");
        hotelName = intent.getStringExtra("hotelName");
        txtHotelName.setText(hotelName);
        if (DataHolder.room_numbers != null && DataHolder.guests != null) {
            txtRoom.setText(String.valueOf(DataHolder.room_numbers));
            txtGuest.setText(String.valueOf(DataHolder.guests));
            txtPeriod.setText(DataHolder.duration);

        }
    }

    private void getData() {
        firebaseDatabase.getReference().child("Hotels").child(hotelID).child("hotelRoom")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Room room = dataSnapshot.getValue(Room.class);
                            roomArrayList.add(room);
                        }
                        roomAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_GUEST_ROOM && resultCode == Activity.RESULT_OK) {
            roomCount = data.getIntExtra("roomCount", 0);
            int adultsCount = data.getIntExtra("adultsCount", 0);
            int childrenCount = data.getIntExtra("childrenCount", 0);

            guestsCount = adultsCount + childrenCount;

            txtGuest.setText(String.valueOf(guestsCount));
            txtRoom.setText(String.valueOf(roomCount));
        }
    }

    private void addViews() {
        imgBack = findViewById(R.id.imgBack);

        txtHotelName = findViewById(R.id.txtHotelName);
        txtPeriod = findViewById(R.id.txtPeriod);
        txtRoom = findViewById(R.id.txtRoom);


        txtGuest = findViewById(R.id.txtGuest);

        llRoomGuest = findViewById(R.id.llRoomGuest);

        rvRoom = findViewById(R.id.rvRoom);
        roomArrayList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        roomAdapter = new RoomAdapter(roomArrayList, getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvRoom.setLayoutManager(linearLayoutManager);
        rvRoom.setNestedScrollingEnabled(false);
        rvRoom.setAdapter(roomAdapter);
    }


}
