package com.example.finalproject.booking;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import com.example.finalproject.model.DataHolder;
import com.example.finalproject.model.Hotel;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.finalproject.databinding.ActivityViewBookingBinding;

import com.example.finalproject.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class ViewBookingActivity extends AppCompatActivity {

    ImageView btnBack;
    Button btnConfirm, btnShowImageGallery;
    ImageView imgHotelBKDetail;
    TextView txtHotelNameBKDetail, txtHotelAddressBKDetail, txtPriceBKDetail,
            txtVATBKDetail, txtTotalPriceBKDetail, txtAfterApplyDiscount,
            txtDiscountBKDetail, txtFullNameBKDetail, txtPhoneBKDetail,
            txtCheckInBKDetail, txtCheckOutBKDetail, txtGuestNumbBKDetail, txtRoomTypeBKDetail;
    Hotel hotel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_booking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        addView();
        setupUIWithDataPassing();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnShowImageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewBookingActivity.this, ViewImageGalleryActivity.class);
                intent.putExtra("hotel_id", hotel.getHotelID());
                startActivity(intent);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewBookingActivity.this, Payment2Activity.class);
                startActivity(intent);
            }
        });
    }

    private void addView() {
        btnBack = findViewById(R.id.btnBack);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnShowImageGallery = findViewById(R.id.btnShowImageGallery);
        imgHotelBKDetail = findViewById(R.id.imgHotelBKDetail);
        txtHotelNameBKDetail = findViewById(R.id.txtHotelNameBKDetail);
        txtHotelAddressBKDetail = findViewById(R.id.txtHotelAddressBKDetail);
        txtFullNameBKDetail = findViewById(R.id.txtFullNameBKDetail);
        txtPhoneBKDetail = findViewById(R.id.txtPhoneBKDetail);
        txtPriceBKDetail = findViewById(R.id.txtPriceBKDetail);
        txtVATBKDetail = findViewById(R.id.txtVATBKDetail);
        txtTotalPriceBKDetail = findViewById(R.id.txtTotalPriceBKDetail);
        txtAfterApplyDiscount = findViewById(R.id.txtAfterApplyDiscount);
        txtDiscountBKDetail = findViewById(R.id.txtDiscountBKDetail);
        txtCheckInBKDetail = findViewById(R.id.txtCheckInBKDetail);
        txtCheckOutBKDetail = findViewById(R.id.txtCheckOutBKDetail);
        txtGuestNumbBKDetail = findViewById(R.id.txtGuestNumbBKDetail);
        txtRoomTypeBKDetail = findViewById(R.id.txtRoomTypeBKDetail);
    }

    private void setupUIWithDataPassing() {
        hotel = getIntent().getSerializableExtra("hotel_item", Hotel.class);
        float subTotal = getIntent().getFloatExtra("subTotal", 0);
        Float vat = getIntent().getFloatExtra("vat", 0);
        Float total = getIntent().getFloatExtra("total", 0);
        float discount = getIntent().getFloatExtra("discount", 0);
        String fullname = getIntent().getStringExtra("full_name");
        String phone = getIntent().getStringExtra("phone");
        String address = getIntent().getStringExtra("address");

        Float afterApplyDiscount = subTotal - discount;

        if (hotel != null) {
            Picasso.get().load(hotel.getHotelImage()).into(imgHotelBKDetail);
            txtHotelNameBKDetail.setText(hotel.getHotelName());
            txtHotelAddressBKDetail.setText(hotel.getHotelAddress());
        }
        if (discount != 0) {
            txtPriceBKDetail.setVisibility(View.VISIBLE);
            txtPriceBKDetail.setText(String.format("VNĐ %,.0f", subTotal));
            txtPriceBKDetail.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            txtPriceBKDetail.setVisibility(View.INVISIBLE);
        }
        txtFullNameBKDetail.setText(fullname);
        txtPhoneBKDetail.setText(phone);
        txtVATBKDetail.setText(String.format("VND %,.0f", vat));
        txtTotalPriceBKDetail.setText(String.format("VND %,.0f", total));
        txtAfterApplyDiscount.setText(String.format("VND %,.0f", afterApplyDiscount));
        txtDiscountBKDetail.setText(String.format("VND %,.0f", discount));
        txtCheckInBKDetail.setText(convertDateFromTimeStamp(DataHolder.check_in));
        txtCheckOutBKDetail.setText(convertDateFromTimeStamp(DataHolder.check_out));
        txtGuestNumbBKDetail.setText(DataHolder.guests + " Guests");
        txtRoomTypeBKDetail.setText(DataHolder.type_room);
    }

    private String convertDateFromTimeStamp(Long time) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM, EEE", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
}