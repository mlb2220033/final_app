package com.example.finalproject.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.adapter.HotelAdapter;
import com.example.finalproject.adapter.HotelFavoriteAdapter;
import com.example.finalproject.model.DataHolder;
import com.example.finalproject.model.Hotel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FavoriteListActivity extends AppCompatActivity {
    ImageView imgFilter, imgBack, imgCancel;
    RecyclerView rvFavorite;
    HotelFavoriteAdapter hotelAdapter;
    ArrayList<Hotel> favoriteList;
    FirebaseDatabase firebaseDatabase;
    LinearLayout llNoResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        addViews();
        addEvents();
        getFavoriteList();
    }

    private void getFavoriteList() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference().child("Users").child(user.getUid()).child("favorites")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<String> favoriteHotelIds = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            favoriteHotelIds.add(dataSnapshot.getKey());
                        }
                        fetchFavoriteHotels(favoriteHotelIds);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void fetchFavoriteHotels(ArrayList<String> favoriteHotelIds) {
        if (favoriteHotelIds.isEmpty()) {
            llNoResult.setVisibility(View.VISIBLE);
            return;
        }
        for (String hotelId : favoriteHotelIds) {
            firebaseDatabase.getReference().child("Hotels").child(hotelId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Hotel hotel = snapshot.getValue(Hotel.class);
                            if (hotel != null) {
                                favoriteList.add(hotel);
                                hotelAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        }

        hotelAdapter.setOnListEmptyListener(new HotelFavoriteAdapter.OnListEmptyListener() {
            @Override
            public void onListEmpty(boolean isEmpty) {
                if (isEmpty) {
                    llNoResult.setVisibility(View.VISIBLE);
                } else {
                    llNoResult.setVisibility(View.GONE);
                }
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
    }

    private void addViews() {
        imgBack = findViewById(R.id.imgBack);
        rvFavorite = findViewById(R.id.rvFavorite);
        llNoResult = findViewById(R.id.llNoResult);

        favoriteList = new ArrayList<>();
        hotelAdapter = new HotelFavoriteAdapter(favoriteList, getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvFavorite.setLayoutManager(linearLayoutManager);

        rvFavorite.setNestedScrollingEnabled(false);
        rvFavorite.setAdapter(hotelAdapter);
    }
}