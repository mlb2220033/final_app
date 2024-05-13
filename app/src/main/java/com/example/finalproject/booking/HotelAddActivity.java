package com.example.finalproject.booking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityHotelAddBinding;
import com.example.finalproject.databinding.ActivityLoginBinding;
import com.example.finalproject.model.MyUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class HotelAddActivity extends AppCompatActivity {
    private ActivityHotelAddBinding binding;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private static final String TAG_ADD = "ADMIN_ADD_HOTEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHotelAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();

        binding.btnSubmitAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

    }

    private String
            hotelName="", hotelDescription="", hotelEmail="", hotelPhone="",
            hotelAddress="", hotelPrice="", hotelImageMain="";
    private void validateData() {
        //validate data

        //get data
        hotelName = binding.edtHotelNameAdded.getText().toString().trim();
        hotelDescription = binding.edtHotelDescriptionAdded.getText().toString();
        hotelEmail = binding.edtHotelEmailAdded.getText().toString();
        hotelPhone = binding.edtHotelPhoneAdded.getText().toString();
        hotelAddress = binding.edtHotelAddressAdded.getText().toString();
        hotelPrice = binding.edtHotelPricePerNightAdded.getText().toString();
        hotelImageMain = binding.edtHotelImageAdded.getText().toString();
        if (TextUtils.isEmpty(hotelName)) {
            Toast.makeText(this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
            binding.edtHotelNameAdded.setError("Email is required");
            binding.edtHotelNameAdded.requestFocus();
        } else if (TextUtils.isEmpty(hotelDescription)) {
            Toast.makeText(this, "Please Re-Enter Your Email", Toast.LENGTH_SHORT).show();
            binding.edtHotelDescriptionAdded.setError("Valid email is required");
            binding.edtHotelDescriptionAdded.requestFocus();
        } else if (TextUtils.isEmpty(hotelEmail)) {
            Toast.makeText(HotelAddActivity.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
            binding.edtHotelEmailAdded.setError("Email is required");
            binding.edtHotelEmailAdded.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(hotelEmail).matches()) {
            Toast.makeText(HotelAddActivity.this, "Please Re-Enter Your Email", Toast.LENGTH_SHORT).show();
            binding.edtHotelEmailAdded.setError("Valid email is required");
            binding.edtHotelEmailAdded.requestFocus();
        } else if (TextUtils.isEmpty(hotelPhone)) {
            Toast.makeText(HotelAddActivity.this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show();
            binding.edtHotelPhoneAdded.setError("Phone number is required");
            binding.edtHotelPhoneAdded.requestFocus();
        } else if (hotelPhone.length()!=10) {
            Toast.makeText(HotelAddActivity.this, "Please Re-Enter Your Phone Number", Toast.LENGTH_SHORT).show();
            binding.edtHotelPhoneAdded.setError("Phone number should be 10 digits");
            binding.edtHotelPhoneAdded.requestFocus();
        } else if (TextUtils.isEmpty(hotelAddress)) {
            Toast.makeText(this, "Please Enter Your Address", Toast.LENGTH_SHORT).show();
            binding.edtHotelAddressAdded.setError("Address is required");
            binding.edtHotelAddressAdded.requestFocus();
        } else if (TextUtils.isEmpty(hotelPrice)) {
            Toast.makeText(HotelAddActivity.this, "Please Enter The Lowest Price", Toast.LENGTH_SHORT).show();
            binding.edtHotelPricePerNightAdded.setError("Price is required");
            binding.edtHotelPricePerNightAdded.requestFocus();
        } else if (TextUtils.isEmpty(hotelImageMain)) {
            Toast.makeText(HotelAddActivity.this, "Please Enter Hotel Image URL", Toast.LENGTH_SHORT).show();
            binding.edtHotelImageAdded.setError("Url is required");
            binding.edtHotelImageAdded.requestFocus();
        } else {
            addHotelFirebase();
        }
    }

    private void addHotelFirebase() {
        progressDialog.setMessage("Adding hotel...");
        progressDialog.show();

        // Generate unique hotel ID
        String hotelID = FirebaseDatabase.getInstance().getReference("Hotels").push().getKey();
        //get timestamp
        long timestamp = MyUtils.timestamp();
        hotelName = binding.edtHotelNameAdded.getText().toString().trim();
        hotelDescription = binding.edtHotelDescriptionAdded.getText().toString();
        hotelEmail = binding.edtHotelEmailAdded.getText().toString();
        hotelPhone = binding.edtHotelPhoneAdded.getText().toString();
        hotelAddress = binding.edtHotelAddressAdded.getText().toString();
        hotelPrice = binding.edtHotelPricePerNightAdded.getText().toString();
        hotelImageMain = binding.edtHotelImageAdded.getText().toString();

        // Khởi tạo một ArrayList để lưu trữ các facilities
        ArrayList<HashMap<String, Object>> facilitiesList = new ArrayList<>();
        // Tạo HashMap cho mỗi facility và thêm vào ArrayList
        HashMap<String, Object> facility1 = new HashMap<>();
        facility1.put("facIcon", "https://cdn2.iconfinder.com/data/icons/css-vol-2/24/laptop-1024.png");
        facility1.put("facName", "Business Facilities");
        ArrayList<String> nestedListF = new ArrayList<>();
        nestedListF.add("Meeting facilities");
        nestedListF.add("Conference room");
        nestedListF.add("Projector");
        facility1.put("nestedList", nestedListF);
        facilitiesList.add(facility1);
        HashMap<String, Object> hashMap = new HashMap<>();

        // Khởi tạo một ArrayList để lưu trữ các URL hình ảnh
        ArrayList<String> imagesList = new ArrayList<>();
        // Thêm các URL hình ảnh vào ArrayList
        imagesList.add("");
        imagesList.add("");
        imagesList.add("");
        imagesList.add("");
        imagesList.add("");

        // Khởi tạo một ArrayList để lưu trữ các chính sách
        ArrayList<HashMap<String, Object>> policiesList = new ArrayList<>();
        // Tạo và thêm chính sách vào ArrayList
        HashMap<String, Object> policy1 = new HashMap<>();
        ArrayList<String> nestedListP = new ArrayList<>();
        nestedListP.add("Check-in time: From 14:00");
        nestedListP.add("Check-out time: Before 12:00");
        policy1.put("nestedList", nestedListP);
        policy1.put("polIcon", "https://cdn3.iconfinder.com/data/icons/date-and-time-48/24/Alarm_Check_in-lc-1024.png");
        policy1.put("polName", "Check-in/check-out time");
        policiesList.add(policy1);

        // Tạo và thêm chính sách thứ hai vào ArrayList
        HashMap<String, Object> policy2 = new HashMap<>();
        ArrayList<String> nestedList2 = new ArrayList<>();
        nestedList2.add("Pets are not allowed in the accommodation");
        policy2.put("nestedList", nestedList2);
        policy2.put("polIcon", "https://cdn2.iconfinder.com/data/icons/hotel-208/128/_no_pets_dog_forbidden_not_allowed_prohibition_shapes_and_symbols_signaling_animals-1024.png");
        policy2.put("polName", "Pets");
        policiesList.add(policy2);

        // Tạo và thêm chính sách thứ ba vào ArrayList
        HashMap<String, Object> policy3 = new HashMap<>();
        ArrayList<String> nestedList3 = new ArrayList<>();
        nestedList3.add("Breakfast at the accommodation will be provided from 6:30 to 9:30");
        policy3.put("nestedList", nestedList3);
        policy3.put("polIcon", "https://cdn3.iconfinder.com/data/icons/solid-amenities-icon-set/64/Breakfast_2-1024.png");
        policy3.put("polName", "Breakfast");
        policiesList.add(policy3);

        hashMap.put("Liked", false);
        hashMap.put("PricePerNight", Integer.parseInt(hotelPrice));
        hashMap.put("startRating", "");
        hashMap.put("hotelAddress", hotelAddress);
        hashMap.put("hotelDescription", hotelDescription);
        hashMap.put("hotelFacilities", facilitiesList);
        hashMap.put("hotelGmail", hotelEmail);
        hashMap.put("hotelID", hotelID);
        hashMap.put("hotelImage", hotelImageMain);
        hashMap.put("hotelImages", imagesList);
        hashMap.put("hotelName", hotelName); 
        hashMap.put("hotelPhone", hotelPhone);
        hashMap.put("hotelPolicies", policiesList );
        hashMap.put("timestamp", timestamp);
        String adminID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        hashMap.put("adminID", adminID);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Hotels");
        ref.child(hotelID)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG_ADD, "onSuccess: Added...");
                        progressDialog.dismiss();
                        startActivity(new Intent(HotelAddActivity.this, DashboardAdminActivity.class));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG_ADD, "onFailure", e);
                        MyUtils.toast(HotelAddActivity.this, "Failed to save due to"+e.getMessage());
                        progressDialog.dismiss();
                    }
                });




    }
}