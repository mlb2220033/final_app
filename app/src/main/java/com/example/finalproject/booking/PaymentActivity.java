package com.example.finalproject.booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalproject.R;
import com.example.finalproject.model.DataHolder;
import com.example.finalproject.model.Discount;
import com.example.finalproject.model.Hotel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class PaymentActivity extends AppCompatActivity {
    private DatabaseReference ref;
    private FirebaseDatabase db;
    private FirebaseAuth auth;
    private Hotel hotel;
    private Float subTotal, vat, total, discount = (float) 0;
    private Long date_numbers;
    private List<Discount> discounts;
    Button btnConfirm, btnApplyDiscount;
    TextView txtSubtotal, txtDiscount, txtVAT, txtTotal, txtRoomNumbers, txtNightNumbers;
    EditText edtInputDiscount, edtFullNamePayment, edtPhonePayment, edtAddressPayment,
            edtCheckIn, edtCheckOut, edtTypeRoom;
    ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addView();

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        getUserInfo();
        getListDiscount();
        getHotelSelected();
        getRoomSelected();

        btnApplyDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnApplyDiscount.getText().toString().equals("Apply")) {
                    if (edtInputDiscount.getText().toString().isEmpty()) {
                        Toast.makeText(PaymentActivity.this, "discount field cannot be left blank", Toast.LENGTH_LONG).show();
                    } else {
                        btnApplyDiscount.setText("Clear");
                        for (Discount element : discounts) {
                            if (element.getCode().contentEquals(edtInputDiscount.getText())) {
                                Toast.makeText(PaymentActivity.this, "Apply discount code successfully", Toast.LENGTH_LONG).show();
                                discount = element.getCost();
                                totalCalculate();
                                break;
                            }
                        }
                        if (discount == 0) {
                            Toast.makeText(PaymentActivity.this, "Invalid discount", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    btnApplyDiscount.setText("Apply");
                    edtInputDiscount.setText("");
                    discount = (float) 0;
                    totalCalculate();
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtFullNamePayment.getText().toString().isEmpty()) {
                    Toast.makeText(PaymentActivity.this, "Fullname field cannot be left blank", Toast.LENGTH_LONG).show();
                } else if (edtPhonePayment.getText().toString().isEmpty()) {
                    Toast.makeText(PaymentActivity.this, "Phone number field cannot be left blank", Toast.LENGTH_LONG).show();
                } else if (edtAddressPayment.getText().toString().isEmpty()) {
                    Toast.makeText(PaymentActivity.this, "Address field cannot be left blank", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(PaymentActivity.this, ViewBookingActivity.class);
                    intent.putExtra("hotel_item", hotel);
                    intent.putExtra("subTotal", subTotal);
                    intent.putExtra("vat", vat);
                    intent.putExtra("total", total);
                    intent.putExtra("discount", discount);
                    intent.putExtra("full_name", edtFullNamePayment.getText().toString());
                    intent.putExtra("phone", edtPhonePayment.getText().toString());
                    intent.putExtra("address", edtAddressPayment.getText().toString());
                    startActivity(intent);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addView() {
        btnConfirm=findViewById(R.id.btnUpdate);
        btnApplyDiscount=findViewById(R.id.btnApplyDiscount);
        btnBack=findViewById(R.id.btnBack);
        txtSubtotal=findViewById(R.id.txtSubtotal);
        txtDiscount=findViewById(R.id.txtDiscount);
        txtVAT=findViewById(R.id.txtVAT);
        txtTotal=findViewById(R.id.txtTotal);
        txtNightNumbers=findViewById(R.id.txtNightNumbers);
        txtRoomNumbers=findViewById(R.id.txtRoomNumbers);
        edtInputDiscount=findViewById(R.id.edtInputDiscount);
        edtFullNamePayment=findViewById(R.id.edtFullNamePayment);
        edtAddressPayment=findViewById(R.id.edtAddressPayment);
        edtPhonePayment=findViewById(R.id.edtPhonePayment);
        edtCheckIn=findViewById(R.id.edtCheckIn);
        edtCheckOut=findViewById(R.id.edtCheckOut);
        edtTypeRoom=findViewById(R.id.edtTypeRoom);

        discounts = new ArrayList<>();
    }

    private void getHotelSelected() {
        ref = db.getReference("Hotels");
        ref.child(DataHolder.hotel_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    hotel = task.getResult().getValue(Hotel.class);
                }
            }
        });
    }

    private void getRoomSelected() {
        edtCheckIn.setText(convertDateFromTimeStamp(DataHolder.check_in));
        edtCheckOut.setText(convertDateFromTimeStamp(DataHolder.check_out));
        edtTypeRoom.setText(DataHolder.type_room);
        totalCalculate();
    }

    private void getUserInfo() {
        ref = db.getReference("Users");
        String uid = auth.getUid();
        if (!uid.isEmpty()) {
            ref.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("full name").getValue(String.class);
                        String phoneNumber = snapshot.child("phone number").getValue(String.class);

                        edtFullNamePayment.setText(name);
                        edtPhonePayment.setText(phoneNumber);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        edtAddressPayment.setText(DataHolder.hotel_address);
    }

    private void getListDiscount() {
        ref = db.getReference("Discount");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Discount discount = dataSnapshot.getValue(Discount.class);
                    discounts.add(discount);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void totalCalculate() {
        Float price_per_night = DataHolder.room_price;
        date_numbers = (DataHolder.check_out - DataHolder.check_in) / 1000 / 60 / 60 / 24;
        Float date_count = date_numbers.floatValue();
        subTotal = date_count * price_per_night * DataHolder.room_numbers;
        vat = (float) (0.1 * subTotal);

        total = subTotal - discount + vat;

        txtSubtotal.setText(String.format("%,.0f", subTotal));
        txtDiscount.setText(String.format("%,.0f", discount));
        txtVAT.setText(String.format("%,.0f", vat));
        txtTotal.setText(String.format("%,.0f", total));
        txtRoomNumbers.setText(DataHolder.room_numbers + " rooms");
        txtNightNumbers.setText(String.format("%.0f", date_count) + " nights");
    }

    private String convertDateFromTimeStamp(Long time) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
}