package com.example.finalproject.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.finalproject.R;
import com.example.finalproject.adapter.HotelAdapter;
import com.example.finalproject.model.Hotel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ResultSearchActivity extends AppCompatActivity {
    ImageView imgFilter, imgBack, imgCancel;
    TextView txtLocation, txtPeriod, txtGuest, txtRoom;
    RecyclerView rvHotel;
    HotelAdapter hotelAdapter;
    ArrayList<Hotel> recycleList;
    FirebaseDatabase firebaseDatabase;
    String Location, hotelName;
    List<Float> hotelStar;
    List<Float> selectedStar;
    boolean isHighToLowChecked = false;
    boolean isLowToHighChecked = false;
    EditText edtHotelName;
    Button btnApply, btnReset;
    RadioButton radioHigh2Low, radioLow2High;
    LinearLayout txt5star, txt4star, txt3star, txt2star, txt1star;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);

        addViews();
        addEvents();
        getDataFromPrevious();
        getDataHotelSearch();

    }

    private void getDataFromPrevious() {
        Intent intent = getIntent();

        Location = getIntent().getStringExtra("txtLocation");

        txtLocation.setText(getIntent().getStringExtra("txtLocation"));
        txtPeriod.setText(getIntent().getStringExtra("txtPeriod"));
        txtRoom.setText(String.valueOf(intent.getIntExtra("roomCount", 0)));
        txtGuest.setText(String.valueOf(intent.getIntExtra("guestsCount", 0)));

    }

    private void getDataHotelSearch() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference().child("Hotels").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Hotel hotel = dataSnapshot.getValue(Hotel.class);
                    if (hotel.getHotelAddress().toLowerCase().contains(Location.toLowerCase())) {
                        recycleList.add(hotel);
                    }
                }
                hotelAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addEvents() {
        imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_filter);

        edtHotelName = dialog.findViewById(R.id.edtHotelName);
        btnApply = dialog.findViewById(R.id.btnApply);
        btnReset = dialog.findViewById(R.id.btnReset);
        imgCancel = dialog.findViewById(R.id.imgCancel);
        radioHigh2Low = dialog.findViewById(R.id.radioHigh2Low);
        radioLow2High = dialog.findViewById(R.id.radioLow2High);

        txt5star = dialog.findViewById(R.id.txt5star);
        txt4star = dialog.findViewById(R.id.txt4star);
        txt3star = dialog.findViewById(R.id.txt3star);
        txt2star = dialog.findViewById(R.id.txt2star);
        txt1star = dialog.findViewById(R.id.txt1star);

        restoreDialogPrevious();

        setStarClickListener(txt5star, 5f, hotelStar);
        setStarClickListener(txt4star, 4f, hotelStar);
        setStarClickListener(txt3star, 3f, hotelStar);
        setStarClickListener(txt2star, 2f, hotelStar);
        setStarClickListener(txt1star, 1f, hotelStar);

        radioHigh2Low.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isHighToLowChecked = true;
                    isLowToHighChecked = false;
                }
            }
        });

        radioLow2High.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isHighToLowChecked = false;
                    isLowToHighChecked = true;
                }
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hotelName = edtHotelName.getText().toString();
                searchHotels(hotelName, hotelStar);
                dialog.dismiss();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycleList.clear();
                hotelName = "";
                selectedStar.clear();
                hotelStar.clear();
                isHighToLowChecked = false;
                isLowToHighChecked = false;

                getDataHotelSearch();
                dialog.dismiss();
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Drawable backgroundDrawable = getResources().getDrawable(R.drawable.round_top_background);
        dialog.getWindow().setBackgroundDrawable(backgroundDrawable);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void restoreDialogPrevious() {
        edtHotelName.setText(hotelName);
        for (Float star : selectedStar) {
            switch (star.intValue()) {
                case 5:
                    setStarBackground(txt5star, true);
                    break;
                case 4:
                    setStarBackground(txt4star, true);
                    break;
                case 3:
                    setStarBackground(txt3star, true);
                    break;
                case 2:
                    setStarBackground(txt2star, true);
                    break;
                case 1:
                    setStarBackground(txt1star, true);
                    break;
            }
        }
        txt5star.setSelected(selectedStar.contains(5f));
        txt4star.setSelected(selectedStar.contains(4f));
        txt3star.setSelected(selectedStar.contains(3f));
        txt2star.setSelected(selectedStar.contains(2f));
        txt1star.setSelected(selectedStar.contains(1f));

        radioHigh2Low.setChecked(isHighToLowChecked);
        radioLow2High.setChecked(isLowToHighChecked);
    }

    private void setStarClickListener(LinearLayout layout, final float starRating, final List<Float> hotelStarList) {
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected = toggleStarSelection(layout);
                if (isSelected) {
                    if (!hotelStarList.contains(starRating)) {
                        hotelStarList.add(starRating);
                    }
                } else {
                    hotelStarList.remove(starRating);
                }
            }
        });
    }

    private boolean toggleStarSelection(LinearLayout layout) {
        boolean isSelected = !layout.isSelected();
        layout.setSelected(isSelected);
        setStarBackground(layout, isSelected);
        return isSelected;
    }

    private void setStarBackground(LinearLayout layout, boolean isSelected) {
        TextView textView = (TextView) layout.getChildAt(0);
        float starValue = Float.parseFloat(textView.getText().toString());
        if (isSelected) {
            layout.setBackgroundResource(R.drawable.button_hover_click);
            textView.setTextColor(getResources().getColor(R.color.Neutral_100));
            if (!selectedStar.contains(starValue)) {
                selectedStar.add(starValue);
            }
        } else {
            layout.setBackgroundResource(R.drawable.button_design);
            textView.setTextColor(getResources().getColor(R.color.Neutral_900));
            if (selectedStar.contains(starValue)) {
                selectedStar.remove(starValue);
            }
        }
    }

    private synchronized void searchHotels(String hotelName, List<Float> hotelStar) {
        recycleList.clear();
        firebaseDatabase.getReference().child("Hotels").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Hotel hotel = dataSnapshot.getValue(Hotel.class);
                    boolean isNameMatched = !hotelName.isEmpty() && hotel.getHotelName().toLowerCase().contains(hotelName.toLowerCase());
                    boolean isStarMatched = hotelStar.contains(hotel.getStarRating());

                    if (isNameMatched || isStarMatched) {
                        recycleList.add(hotel);
                    } else if (hotelName.isEmpty() && hotelStar.isEmpty()) {
                        recycleList.add(hotel);
                    }
                }

                if (radioHigh2Low.isChecked()) {
                    Collections.sort(recycleList, new Comparator<Hotel>() {
                        @Override
                        public int compare(Hotel hotel1, Hotel hotel2) {
                            return Float.compare(hotel2.getPricePerNight(), hotel1.getPricePerNight());
                        }
                    });
                } else if (radioLow2High.isChecked()) {
                    Collections.sort(recycleList, new Comparator<Hotel>() {
                        @Override
                        public int compare(Hotel hotel1, Hotel hotel2) {
                            return Float.compare(hotel1.getPricePerNight(), hotel2.getPricePerNight());
                        }
                    });
                }
                hotelAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    void addViews() {
        imgFilter = findViewById(R.id.imgFilter);
        imgBack = findViewById(R.id.imgBack);
        txtLocation = findViewById(R.id.txtLocation);
        txtPeriod = findViewById(R.id.txtPeriod);
        txtGuest = findViewById(R.id.txtGuest);
        txtRoom = findViewById(R.id.txtRoom);
        rvHotel = findViewById(R.id.rvHotel);

        recycleList = new ArrayList<>();
        hotelAdapter = new HotelAdapter(recycleList, getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvHotel.setLayoutManager(linearLayoutManager);
        rvHotel.addItemDecoration(new DividerItemDecoration(rvHotel.getContext(), DividerItemDecoration.VERTICAL));
        rvHotel.setNestedScrollingEnabled(false);
        rvHotel.setAdapter(hotelAdapter);

        hotelStar = new ArrayList<>();
        selectedStar = new ArrayList<>();
    }
}