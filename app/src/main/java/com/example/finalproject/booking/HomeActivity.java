package com.example.finalproject.booking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.finalproject.model.ItemsModel;
import com.example.finalproject.model.RecomModel;
import com.example.finalproject.model.SliderBanner;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends FireBaseActivity {
    private ActivityHomeBinding binding;
    LinearLayout Profile, Favorite, Chat;
    LinearLayout edtSearch;
    ImageView imgSearchAdvanced;

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
    }

    private void addViews() {
        Profile = findViewById(R.id.Profile);
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
        DatabaseReference myRef= database.getReference("Recom");
        binding.progressBarItem.setVisibility(View.VISIBLE);
        ArrayList<RecomModel> items = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue:snapshot.getChildren()){
                        items.add(issue.getValue(RecomModel.class));
                    }
                    if(!items.isEmpty()){
                        LinearLayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
                        layoutManager.setOrientation(RecyclerView.VERTICAL);
                        binding.recycleViewRecom.setLayoutManager(layoutManager);
                        binding.recycleViewRecom.setAdapter(new RecomAdapter(items));
                    }
                    binding.progressBarItem.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initPopular() {
        DatabaseReference myReference= database.getReference("Items");
        binding.progressBarPopular.setVisibility(View.VISIBLE);
        ArrayList<ItemsModel> items = new ArrayList<>();

        myReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue:snapshot.getChildren()){
                        items.add(issue.getValue(ItemsModel.class));
                    }
                    if(!items.isEmpty()){
                        LinearLayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
                        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
                        binding.recycleViewPopular.setLayoutManager(layoutManager);
                        binding.recycleViewPopular.setAdapter(new PopularAdapter(items));
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