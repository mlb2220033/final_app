package com.example.finalproject.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.finalproject.R;
import com.example.finalproject.adapter.HotelAdapter;
import com.example.finalproject.adapter.HotelFacilitiesAdapter;
import com.example.finalproject.adapter.HotelFacilitiesItemAdapter;
import com.example.finalproject.adapter.HotelPoliciesAdapter;
import com.example.finalproject.model.Hotel;
import com.example.finalproject.model.HotelFacilities;
import com.example.finalproject.model.HotelPolicies;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HotelDetailActivity extends AppCompatActivity {

    ImageSlider imgSlider;

    TextView txtHotelName, txtHotelAddress, txtHotelDescription, txtPricePerNight, txtStarRating,
            txtViewRoom, txtContactHotel, txtHotelPhone, txtHotelGmail,
            txtAllDescription, txtAllFacilities, txtAllPolicies;
    String hotelID, hotelName;
    AppBarLayout appBarLayout;
    RecyclerView rvFac;
    HotelFacilitiesAdapter hotelFacilitiesAdapter;
    ArrayList<HotelFacilities> hotelFacList;
    FirebaseDatabase firebaseDatabase;

    RecyclerView rvPol;
    List<HotelPolicies> hotelPoliciesList;
    HotelPoliciesAdapter hotelPoliciesAdapter;
    MaterialToolbar toolbar;
    private boolean isDescriptionExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);

        addViews();
        addEvents();
        getDataFromPreviousActivity();
        setupImageSlider();
        getFactilities();
        getPolicies();
        setupToolBar();


    }


    public void setupToolBar() {
        appBarLayout = findViewById(R.id.appBarLayout);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShown = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    getSupportActionBar().setTitle(hotelName);
                    isShown = true;
                } else if (isShown) {
                    getSupportActionBar().setTitle(null);
                    isShown = false;
                }
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hotel_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_favour) {
            if (item.getIcon().getConstantState().equals(getResources().getDrawable(R.drawable.ic_dislike).getConstantState())) {
                item.setIcon(getResources().getDrawable(R.drawable.ic_like));
                Toast.makeText(this, "Added to favorite list", Toast.LENGTH_SHORT).show();
            } else {
                item.setIcon(getResources().getDrawable(R.drawable.ic_dislike));
                Toast.makeText(this, "Removed from favorite list", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    private void getPolicies() {
        firebaseDatabase.getReference().child("Hotels").child(hotelID).child("hotelPolicies")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int count = 0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (count >= 2)
                                break;
                            HotelPolicies hotelPol = dataSnapshot.getValue(HotelPolicies.class);
                            hotelPoliciesList.add(hotelPol);
                            count++;
                        }
                        hotelPoliciesAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getFactilities() {
        firebaseDatabase.getReference().child("Hotels").child(hotelID).child("hotelFacilities")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            HotelFacilities hotelFac = dataSnapshot.getValue(HotelFacilities.class);
                            hotelFacList.add(hotelFac);
                        }
                        hotelFacilitiesAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    public void getDataFromPreviousActivity() {
        Intent intent = getIntent();

        hotelID = intent.getStringExtra("hotelID");
        hotelName = getIntent().getStringExtra("txtHotelName");

        txtHotelName.setText(getIntent().getStringExtra("txtHotelName"));
        txtHotelAddress.setText(getIntent().getStringExtra("txtHotelAddress"));
        txtHotelDescription.setText(getIntent().getStringExtra("txtHotelDescription"));
        txtHotelPhone.setText(getIntent().getStringExtra("txtHotelPhone"));
        txtHotelGmail.setText(getIntent().getStringExtra("txtHotelGmail"));

        txtPricePerNight.setText(String.valueOf(getIntent().getFloatExtra("txtPricePerNight", 0.00f)));
        txtStarRating.setText(String.valueOf(getIntent().getFloatExtra("txtStarRating", 0.0f)));


    }

    private void addEvents() {
        txtAllDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDescriptionExpanded) {
                    txtHotelDescription.setMaxLines(3);
                    txtAllDescription.setText("See All Description");
                    isDescriptionExpanded = false;
                } else {
                    txtHotelDescription.setMaxLines(Integer.MAX_VALUE);
                    txtAllDescription.setText("Collapse Description");
                    isDescriptionExpanded = true;
                }
            }
        });
        txtAllFacilities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HotelFacilitiesDetailActivity.class);
                intent.putExtra("hotelID", hotelID);
                startActivity(intent);
            }
        });
        txtAllPolicies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HotelPoliciesActivity.class);
                intent.putExtra("hotelID", hotelID);
                startActivity(intent);
            }
        });


    }

    private void addViews() {

        txtHotelName = findViewById(R.id.txtHotelName);
        txtContactHotel = findViewById(R.id.txtContactHotel);
        txtHotelAddress = findViewById(R.id.txtHotelAddress);
        txtHotelDescription = findViewById(R.id.txtHotelDescription);
        txtHotelPhone = findViewById(R.id.txtHotelPhone);
        txtHotelGmail = findViewById(R.id.txtHotelGmail);
        txtPricePerNight = findViewById(R.id.txtPricePerNight);
        txtStarRating = findViewById(R.id.txtStarRating);
        txtViewRoom = findViewById(R.id.txtViewRoom);

        txtAllDescription = findViewById(R.id.txtAllDescription);
        txtAllFacilities = findViewById(R.id.txtAllFacilities);
        txtAllPolicies = findViewById(R.id.txtAllPolicies);

        hotelName = getIntent().getStringExtra("txtHotelName");
        txtContactHotel.setText(txtContactHotel.getText().toString() + hotelName + "?");

        imgSlider = findViewById(R.id.imgSlider);

//      Factilities List
        rvFac = findViewById(R.id.rvFac);
        hotelFacList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        hotelFacilitiesAdapter = new HotelFacilitiesAdapter(hotelFacList, getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvFac.setLayoutManager(linearLayoutManager);
        rvFac.setNestedScrollingEnabled(false);
        rvFac.setAdapter(hotelFacilitiesAdapter);

//      Policies List
        rvPol = findViewById(R.id.rvPol);
        rvPol.setHasFixedSize(true);
        rvPol.setLayoutManager(new LinearLayoutManager(this));

        hotelPoliciesList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        hotelPoliciesAdapter = new HotelPoliciesAdapter(hotelPoliciesList);
        rvPol.setAdapter(hotelPoliciesAdapter);

    }

    private void setupImageSlider() {
        final List<SlideModel> list = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Hotels").child(hotelID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            List<SlideModel> imageList = new ArrayList<>();
                            DataSnapshot hotelImagesData = snapshot.child("hotelImages");
                            for (DataSnapshot imageData : hotelImagesData.getChildren()) {
                                String imageUrl = imageData.getValue(String.class);
                                imageList.add(new SlideModel(imageUrl, ScaleTypes.FIT));
                            }
                            imgSlider.setImageList(imageList, ScaleTypes.FIT);
                            imgSlider.stopSliding();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    public void openViewRoom(View view) {
        Intent intent = new Intent(getApplicationContext(), ViewRoomActivity.class);
        intent.putExtra("hotelID", hotelID);
        startActivity(intent);
    }
}