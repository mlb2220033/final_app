package com.example.finalproject.booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalproject.R;
import com.example.finalproject.adapter.HotelAdminAdapter;
import com.example.finalproject.databinding.ActivityDashboardAdminBinding;
import com.example.finalproject.model.Hotel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardAdminActivity extends AppCompatActivity {

    private ActivityDashboardAdminBinding binding;
    private FirebaseAuth firebaseAuth;
    private ArrayList<Hotel> hotelArrayList;
    private HotelAdminAdapter hotelAdminAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDashboardAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        loadHotels();

        //handle click, logout
        binding.btnLogoutAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
            }
        });

        binding.btnAddHotelAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardAdminActivity.this, HotelAddActivity.class));
            }
        });
    }

    private void loadHotels() {
        hotelArrayList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hotels");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hotelArrayList.clear();
                String adminID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Hotel hotel = ds.getValue(Hotel.class);
                    // Kiểm tra xem khách sạn thuộc về Admin hiện tại không
                    if (hotel != null && adminID != null && hotel.getAdminID() != null && hotel.getAdminID().equals(adminID)) {
                        hotelArrayList.add(hotel);
                    }
                }
                hotelAdminAdapter = new HotelAdminAdapter(DashboardAdminActivity.this, hotelArrayList);
                binding.hotelsRv.setAdapter(hotelAdminAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý sự kiện onCancelled
            }
        });
    }



    private void checkUser() {
        //get Current User

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser==null){
            //not logged in, go to login screen
            startActivity(new Intent(this, LoginActivity.class));

            finish();
        } else {
            String email =firebaseUser.getEmail();
            binding.txtSubTitle.setText(email);

        }
    }
}