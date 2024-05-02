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

import com.example.finalproject.R;
import com.example.finalproject.adapter.HotelAdapter;
import com.example.finalproject.adapter.RoomAdapter;
import com.example.finalproject.model.Hotel;
import com.example.finalproject.model.Room;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewRoomActivity extends AppCompatActivity {
    RecyclerView rvRoom;
    RoomAdapter roomAdapter;
    ArrayList<Room> roomArrayList;
    String hotelID;
    FirebaseDatabase firebaseDatabase;
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_room);
        addViews();
        addEvents();
        getDataFromPreviousActivity();
        getData();

    }


    private void addEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getDataFromPreviousActivity() {
        Intent intent = getIntent();
        hotelID = intent.getStringExtra("hotelID");
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
    private void addViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvRoom = findViewById(R.id.rvRoom);
        roomArrayList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        roomAdapter = new RoomAdapter(roomArrayList, getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvRoom.setLayoutManager(linearLayoutManager);
        rvRoom.addItemDecoration(new DividerItemDecoration(rvRoom.getContext(), DividerItemDecoration.VERTICAL));
        rvRoom.setNestedScrollingEnabled(false);
        rvRoom.setAdapter(roomAdapter);
    }


}
