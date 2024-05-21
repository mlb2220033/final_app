package com.example.finalproject.booking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.bumptech.glide.Glide;
import com.example.finalproject.R;
import com.example.finalproject.adapter.PopularAdapter;
import com.example.finalproject.adapter.RecomAdapter;
import com.example.finalproject.adapter.SliderAdapter;
import com.example.finalproject.databinding.ActivityHomeBinding;
import com.example.finalproject.model.DataHolder;
import com.example.finalproject.model.Hotel;
import com.example.finalproject.model.MyUtils;
import com.example.finalproject.model.SliderBanner;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    LinearLayout Profile, Favorite, Chat, Home, Explore;
    LinearLayout edtSearch;
    ImageView imgSearchAdvanced;
    int REQUEST_LOCATION = 1;
    FusedLocationProviderClient fusedLocationClient;
    FirebaseDatabase database;
    TextView txtUserName;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase database
        database = FirebaseDatabase.getInstance();

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        initBanner();
        initPopular();
        initRecom();
        addViews();
        addEvents();
        getUserLocation();
        loadMyInfo();
    }

    private void loadMyInfo() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users");

        myRef.child("" + firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = "" + snapshot.child("full name").getValue();


                    binding.txtUserName.setText("Hi, " + name + "!");

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle database error
            }
        });


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
                            } else {
                                Toast.makeText(HomeActivity.this, "Unable to get last location", Toast.LENGTH_SHORT).show();
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
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void addViews() {
        Home = findViewById(R.id.Home);
        Profile = findViewById(R.id.Profile);
        Favorite = findViewById(R.id.Favorite);
        Explore = findViewById(R.id.Explore);

        edtSearch = findViewById(R.id.edtSearch);
        txtUserName = findViewById(R.id.txtUserName);
        imgSearchAdvanced = findViewById(R.id.imgSearchAdvanced);
        firebaseAuth = FirebaseAuth.getInstance();
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

        Explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BlogActivity.class);
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

    private void initRecom() {
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
                    binding.progressBarItem.setVisibility(View.INVISIBLE);
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
                    binding.progressBarPopular.setVisibility(View.INVISIBLE);
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
        DatabaseReference myReference = database.getReference("Banner");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderBanner> items = new ArrayList<>();
        myReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(SliderBanner.class));
                    }
                    banners(items);
                    binding.progressBarBanner.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Data retrieval cancelled: " + error.getMessage());
            }
        });
    }

    private void banners(ArrayList<SliderBanner> items) {
        binding.viewpagerSlider.setAdapter(new SliderAdapter(items, binding.viewpagerSlider));
        binding.viewpagerSlider.setClipToPadding(false);
        binding.viewpagerSlider.setClipChildren(false);
        binding.viewpagerSlider.setOffscreenPageLimit(5);
        binding.viewpagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        binding.viewpagerSlider.setPageTransformer(compositePageTransformer);
    }
}
