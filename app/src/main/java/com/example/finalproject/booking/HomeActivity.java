package com.example.finalproject.booking;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;


import com.example.finalproject.R;
import com.example.finalproject.adapter.PopularAdapter;
import com.example.finalproject.adapter.RecomAdapter;
import com.example.finalproject.adapter.SliderAdapter;
import com.example.finalproject.databinding.ActivityHomeBinding;
import com.example.finalproject.model.DataHolder;
import com.example.finalproject.model.Hotel;
import com.example.finalproject.model.ItemsModel;
import com.example.finalproject.model.RecomModel;
import com.example.finalproject.model.SliderBanner;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends FireBaseActivity {
    private ActivityHomeBinding binding;
    LinearLayout Profile, Favorite, Chat, Home;
    LinearLayout edtSearch;
    ImageView imgSearchAdvanced;
    int REQUEST_LOCATION = 1;
    FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        initBanner();
        initPopular();
        intitRecom();
        addViews();
        addEvents();
        getUserLocation();

    }


    private void getUserLocation() {
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            getLastLocation();
        }
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

    private void addViews() {
        Home = findViewById(R.id.Home);
        Profile = findViewById(R.id.Profile);
        Favorite = findViewById(R.id.Favorite);

        edtSearch = findViewById(R.id.edtSearch);
        imgSearchAdvanced = findViewById(R.id.imgSearchAdvanced);


    }

    private void addEvents() {
        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileMainActivity.class);
                startActivity(intent);
            }
        });

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
        Favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FavoriteListActivity.class);
                startActivity(intent);
            }
        });

        edtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SearchHotelActivity.class);
                startActivity(intent);
            }
        });
        imgSearchAdvanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

    }
    private void intitRecom() {
        DatabaseReference recomRef = database.getReference("Recommends");
        DatabaseReference hotelsRef = database.getReference("Hotels");
        binding.progressBarItem.setVisibility(View.VISIBLE);
        ArrayList<Hotel> hotelRecommendList = new ArrayList<>();

        recomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot recomSnapshot) {
                if (recomSnapshot.exists()) {
                    for (DataSnapshot recomHotel : recomSnapshot.getChildren()) {
                        String hotelID = recomHotel.getKey();
                        boolean isRecom = recomHotel.child("recom").getValue(Boolean.class);

                        if (isRecom) {
                            hotelsRef.child(hotelID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot hotelSnapshot) {
                                    if (hotelSnapshot.exists()) {
                                        Hotel hotel = hotelSnapshot.getValue(Hotel.class);
                                        hotelRecommendList.add(hotel);
                                        updateRecomHotelsList(hotelRecommendList);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e("Firebase", "Data retrieval cancelled: " + error.getMessage());
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Data retrieval cancelled: " + error.getMessage());
            }
        });
    }

    private void updateRecomHotelsList(ArrayList<Hotel> hotelRecommendList) {
        if (!hotelRecommendList.isEmpty()) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            binding.rvRecom.setLayoutManager(layoutManager);
            binding.rvRecom.setAdapter(new RecomAdapter(hotelRecommendList));
        }
        binding.progressBarItem.setVisibility(View.INVISIBLE);
    }



    /*private void intitRecom() {
        DatabaseReference myRef= database.getReference("Recom");
        binding.progressBarItem.setVisibility(View.VISIBLE);
        ArrayList<Hotel> hotelrec = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue:snapshot.getChildren()){
                        hotelrec.add(issue.getValue(Hotel.class));
                    }
                    if(!hotelrec.isEmpty()){
                        LinearLayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
                        layoutManager.setOrientation(RecyclerView.VERTICAL);
                        binding.recycleViewRecom.setLayoutManager(layoutManager);
                        binding.recycleViewRecom.setAdapter(new RecomAdapter(hotelrec));
                    }
                    binding.progressBarItem.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

    /*private void initPopular() {
        DatabaseReference myReference= database.getReference("Items");
        binding.progressBarPopular.setVisibility(View.VISIBLE);
        ArrayList<Hotel> hotelPopularList = new ArrayList<>();

        myReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue:snapshot.getChildren()){
                        hotelPopularList.add(issue.getValue(Hotel.class));
                    }
                    if(!hotelPopularList.isEmpty()){
                        LinearLayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
                        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
                        binding.rvPopular.setLayoutManager(layoutManager);
                        binding.rvPopular.setAdapter(new PopularAdapter(hotelPopularList));
                    }
                    binding.progressBarPopular.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Data retrieval cancelled: " + error.getMessage());
            }
        });
    }*/

    private void initPopular() {
        DatabaseReference popularRef = database.getReference("Populars");
        DatabaseReference hotelsRef = database.getReference("Hotels");
        binding.progressBarPopular.setVisibility(View.VISIBLE);
        ArrayList<Hotel> hotelPopularList = new ArrayList<>();

        popularRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot popularSnapshot) {
                if (popularSnapshot.exists()) {
                    for (DataSnapshot popularHotel : popularSnapshot.getChildren()) {
                        String hotelID = popularHotel.getKey();
                        boolean isPopular = popularHotel.child("popular").getValue(Boolean.class);

                        if (isPopular) {
                            hotelsRef.child(hotelID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot hotelSnapshot) {
                                    if (hotelSnapshot.exists()) {
                                        Hotel hotel = hotelSnapshot.getValue(Hotel.class);
                                        hotelPopularList.add(hotel);
                                        updatePopularHotelsList(hotelPopularList);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e("Firebase", "Data retrieval cancelled: " + error.getMessage());
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Data retrieval cancelled: " + error.getMessage());
            }
        });
    }

    private void updatePopularHotelsList(ArrayList<Hotel> hotelPopularList) {
        if (!hotelPopularList.isEmpty()) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
            layoutManager.setOrientation(RecyclerView.HORIZONTAL);
            binding.rvPopular.setLayoutManager(layoutManager);
            binding.rvPopular.setAdapter(new PopularAdapter(hotelPopularList));
        }
        binding.progressBarPopular.setVisibility(View.INVISIBLE);
    }

    private void initBanner() {
        DatabaseReference myReference= database.getReference("Banner");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderBanner> items = new ArrayList<>();
        myReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue:snapshot.getChildren()){
                        items.add(issue.getValue(SliderBanner.class));
                    }
                    banners(items);
                    binding.progressBarBanner.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Data retrieval cancelled: " + error.getMessage());
            }
        });


    }
    private void banners(ArrayList<SliderBanner> items){
        binding.viewpagerSlider.setAdapter(new SliderAdapter(items,binding.viewpagerSlider));
        binding.viewpagerSlider.setClipToPadding(false);
        binding.viewpagerSlider.setClipChildren(false);
        binding.viewpagerSlider.setOffscreenPageLimit(5);
        binding.viewpagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer=new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        binding.viewpagerSlider.setPageTransformer(compositePageTransformer);


    }
}